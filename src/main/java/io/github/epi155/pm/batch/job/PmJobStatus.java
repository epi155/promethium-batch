package io.github.epi155.pm.batch.job;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.*;

@Slf4j
class PmJobStatus extends PmProcStatus implements JobStatus {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final List<Future<Integer>> futures = new LinkedList<>();

    private PmJobStatus(int rc, JCL jcl, String jobName) {
        super(rc, jcl, jobName);
        MDC.put(JOB_NAME, jobName);
    }

    static PmJobStatus of(int rc, JCL jcl, String jobName) {
        return new PmJobStatus(rc, jcl, jobName);
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
    public int complete() {
        jobCount.recap(maxcc);
        MDC.remove(JOB_NAME);
        stack.clear();
        return maxcc;
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

    @Override
    public JobStatus forkExecProc(String procName, UnaryOperator<ProcStatus> proc) {
        ProcStatus procStatus = new PmProcStatus(jcl.rcOk(), jcl, procName);
        return submit(procName, () -> ((PmProcStatus) proc.apply(procStatus)).complete());
    }

    @Override
    public JobStatus forkNextProc(String procName, UnaryOperator<ProcStatus> proc) {
        if (isSuccess()) {
            return forkExecProc(procName, proc);
        } else {
            jobCount.add(procName);
        }
        return this;
    }

    @Override
    public JobStatus forkElseProc(String procName, UnaryOperator<ProcStatus> proc) {
        if (!isSuccess()) {
            return forkExecProc(procName, proc);
        } else {
            jobCount.add(procName);
        }
        return this;
    }

    @Override
    public JobStatus execProc(String procName, UnaryOperator<ProcStatus> proc) {
        ProcStatus procStatus = new PmProcStatus(jcl.rcOk(), jcl, procName);
        Instant tiStart = Instant.now();
        int returnCode = -1;
        try {
            MDC.put(JOB_NAME, procName);
            returnCode = ((PmProcStatus) proc.apply(procStatus)).complete();
            maxcc(returnCode);
        } finally {
            Instant tiEnd = Instant.now();
            MDC.put(JOB_NAME, jobName);
            jobCount.add(procName, returnCode, tiStart, tiEnd);
        }
        return this;
    }

    @Override
    public JobStatus nextProc(String procName, UnaryOperator<ProcStatus> proc) {
        if (isSuccess()) {
            return execProc(procName, proc);
        } else {
            jobCount.add(procName);
        }
        return this;
    }

    @Override
    public JobStatus elseProc(String procName, UnaryOperator<ProcStatus> proc) {
        if (!isSuccess()) {
            return execProc(procName, proc);
        } else {
            jobCount.add(procName);
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
        futures.add(executorService.submit(() -> {
            try {
                MDC.put(JOB_NAME, jobName);
                return step.call();
            } finally {
                MDC.remove(JOB_NAME);
            }
        }));
        return this;
    }

    private JobStatus submit(String procName, Callable<Integer> step) {
        futures.add(executorService.submit(() -> {
            Instant tiStart = Instant.now();
            Integer returnCode = null;
            try {
                MDC.put(JOB_NAME, procName);
                returnCode = step.call();
                return returnCode;
            } finally {
                Instant tiEnd = Instant.now();
                MDC.remove(JOB_NAME);
                jobCount.add(procName, returnCode == null ? -1 : returnCode, tiStart, tiEnd);
            }
        }));
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
