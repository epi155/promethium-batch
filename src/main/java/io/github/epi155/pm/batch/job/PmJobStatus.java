package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.step.BatchException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.*;

@Slf4j
class PmJobStatus implements JobStatus {
    private static final String STEP_NAME = "stepName";
    private static final String JOB_NAME = "jobName";
    private final JCL jcl;
    private final String jobName;
    private final Deque<Integer> stack = new LinkedList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final List<Future<Integer>> futures = new LinkedList<>();
    private final JobCount jobCount;
    private int maxcc;

    private PmJobStatus(int rc, JCL jcl, String jobName) {
        this.maxcc = rc;
        this.jcl = jcl;
        this.jobName = jobName;
        this.jobCount = new JobCount(jobName);
        MDC.put(JOB_NAME, jobName);
    }

    static PmJobStatus of(int rc, JCL jcl, String jobName) {
        return new PmJobStatus(rc, jcl, jobName);
    }

    private void wrapper(StatsCount c, IntSupplier step) {
        int returnCode = runStep(c, step);
        maxcc(returnCode);
    }

    private void maxcc(int returnCode) {
        if (returnCode > maxcc)
            maxcc = returnCode;
    }

    private int runStep(StatsCount c, IntSupplier step) {
        boolean removeJobName = false;
        if (MDC.get(JOB_NAME) == null) {
            MDC.put(JOB_NAME, jobName);
            removeJobName = true;
        }
        MDC.put(STEP_NAME, c.name());
        log.info("Step {} start", c.name());
        Instant tiStart = Instant.now();
        int returnCode = Integer.MAX_VALUE;
        try {
            returnCode = step.getAsInt();
        } catch (BatchException e) {
            log.error("Execution Error", e);
            returnCode = e.getReturnCode();
        } catch (Exception e) {
            log.error("Unhandled Error", e);
            returnCode = jcl.rcErrorStep();
        } finally {
            c.recap(returnCode);  // log returnCode and custom statistics
            Instant tiEnd = Instant.now();
            Duration lapse = Duration.between(tiStart, tiEnd);
            log.info("Step {} end: {}", c.name(), DateTimeFormatter.ISO_LOCAL_TIME.format(lapse.addTo(LocalTime.of(0, 0))));
            MDC.remove(STEP_NAME);
            if (removeJobName) MDC.remove(JOB_NAME);
            jobCount.add(c.name(), returnCode, tiStart, tiEnd);
        }
        return returnCode;
    }

    public <P, C extends StatsCount> JobStatus nextPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    public <C extends StatsCount> JobStatus nextPgm(C c, ToIntFunction<C> pgm) {
        if (isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    public <P, C extends StatsCount> JobStatus nextPgm(P p, C c, BiConsumer<P, C> pgm) {
        if (isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    public <C extends StatsCount> JobStatus nextPgm(C c, Consumer<C> pgm) {
        if (isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    public JobStatus map(@NotNull UnaryOperator<JobStatus> map) {
        return map.apply(this);
    }

    @Override
    public int maximumConditionCode() {
        return maxcc;
    }

    @Override
    public Optional<Integer> returnCode(String stepName) {
        return jobCount.getReturnCode(stepName);
    }

    @Override
    public int complete() {
        jobCount.recap(maxcc);
        MDC.remove(JOB_NAME);
        stack.clear();
        return maxcc;
    }

    @Override
    public boolean isSuccess() {
        return jcl.rcOk() <= maxcc && maxcc <= jcl.rcWarning();
    }

    @Override
    public JobStatus join() {
        for (int k = 1; !futures.isEmpty(); k++) {
            val it = futures.iterator();
            int nmAlive = 0;
            try {
                while (it.hasNext()) {
                    val ele = it.next();
                    if (ele.isDone()) {
                        joinResult(ele);
                        it.remove();
                    } else {
                        nmAlive++;
                    }
                }
                if (nmAlive > 0) {
                    sendMessage(k, nmAlive);
                    TimeUnit.MILLISECONDS.sleep(40);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return this;
    }

    private void joinResult(Future<Integer> ele) throws InterruptedException {
        try {
            Integer returnCode = ele.get();
            if (returnCode != null) {
                maxcc(returnCode);
            } else {    // dead branch
                log.error("Null returnCode");
                maxcc(jcl.rcErrorJob());
            }
        } catch (ExecutionException e) {    // dead branch
            log.error("Unhandled Error", e);
            maxcc(jcl.rcErrorStep());
        }
    }

    private void sendMessage(int j, int r) {
        if ((j & 0x03ff) != 0) {
            int n = 0;
            while (j != 0) {
                if ((j & 0x01) == 1) n++;
                j >>= 1;
            }
            if (n != 1) return;
        }
        log.info("Waiting {} running step", r);
    }


    @Override
    public JobStatus push() {
        stack.push(maxcc);
        return this;
    }

    @Override
    public JobStatus pop() {
        if (!stack.isEmpty()) {
            maxcc = stack.pop();
        }
        return this;
    }

    @Override
    public JobStatus peek() {
        if (!stack.isEmpty()) {
            maxcc = stack.peek();
        }
        return this;
    }

    @Override
    public <P, C extends StatsCount> JobStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        wrapper(c, () -> pgm.apply(p, c));
        return this;
    }

    @Override
    public <C extends StatsCount> JobStatus execPgm(C c, ToIntFunction<C> pgm) {
        wrapper(c, () -> pgm.applyAsInt(c));
        return this;
    }

    @Override
    public <P, C extends StatsCount> JobStatus execPgm(P p, C c, BiConsumer<P, C> pgm) {
        wrapper(c, () -> {
            pgm.accept(p, c);
            return jcl.rcOk();
        });
        return this;
    }

    @Override
    public <C extends StatsCount> JobStatus execPgm(C c, Consumer<C> pgm) {
        wrapper(c, () -> {
            pgm.accept(c);
            return jcl.rcOk();
        });
        return this;
    }

    @Override
    public <P, C extends StatsCount> JobStatus elsePgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (!isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> JobStatus elsePgm(C c, ToIntFunction<C> pgm) {
        if (!isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <P, C extends StatsCount> JobStatus elsePgm(P p, C c, BiConsumer<P, C> pgm) {
        if (!isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> JobStatus elsePgm(C c, Consumer<C> pgm) {
        if (!isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    private JobStatus submit(Callable<Integer> step) {
        futures.add(executorService.submit(step));
        return this;
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkExecPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        return submit(() -> runStep(c, () -> pgm.apply(p, c)));
    }

    @Override
    public <C extends StatsCount> JobStatus forkExecPgm(C c, ToIntFunction<C> pgm) {
        return submit(() -> runStep(c, () -> pgm.applyAsInt(c)));
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkExecPgm(P p, C c, BiConsumer<P, C> pgm) {
        return submit(() -> runStep(c, () -> {
            pgm.accept(p, c);
            return jcl.rcOk();
        }));
    }

    @Override
    public <C extends StatsCount> JobStatus forkExecPgm(C c, Consumer<C> pgm) {
        return submit(() -> runStep(c, () -> {
            pgm.accept(c);
            return jcl.rcOk();
        }));
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkElsePgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (!isSuccess()) {
            return forkExecPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> JobStatus forkElsePgm(C c, ToIntFunction<C> pgm) {
        if (!isSuccess()) {
            return forkExecPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkElsePgm(P p, C c, BiConsumer<P, C> pgm) {
        if (!isSuccess()) {
            return forkExecPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> JobStatus forkElsePgm(C c, Consumer<C> pgm) {
        if (!isSuccess()) {
            return forkExecPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkNextPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (isSuccess()) {
            return forkExecPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> JobStatus forkNextPgm(C c, ToIntFunction<C> pgm) {
        if (isSuccess()) {
            return forkExecPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkNextPgm(P p, C c, BiConsumer<P, C> pgm) {
        if (isSuccess()) {
            return forkExecPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> JobStatus forkNextPgm(C c, Consumer<C> pgm) {
        if (isSuccess()) {
            return forkExecPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }
}
