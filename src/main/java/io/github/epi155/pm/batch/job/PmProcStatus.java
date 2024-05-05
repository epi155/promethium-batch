package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.step.BatchException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.*;

import static io.github.epi155.pm.batch.job.StatsCount.JOB_NAME;
import static io.github.epi155.pm.batch.job.StatsCount.STEP_NAME;

@Slf4j
class PmProcStatus implements ProcStatus {
    protected final JCL jcl;
    protected final String jobName;
    protected final Deque<Integer> stack = new LinkedList<>();
    protected final JobCount jobCount;
    protected int maxcc;

    protected PmProcStatus(int rc, JCL jcl, String jobName) {
        this.maxcc = rc;
        this.jcl = jcl;
        this.jobName = jobName;
        this.jobCount = new JobCount(jobName);
    }

    @Override
    public boolean isSuccess() {
        return jcl.rcOk() <= maxcc && maxcc <= jcl.rcWarning();
    }

    @Override
    public Optional<Integer> returnCode(String stepName) {
        return jobCount.getReturnCode(stepName);
    }

    @Override
    public ProcStatus push() {
        stack.push(maxcc);
        return this;
    }

    @Override
    public ProcStatus pop() {
        if (!stack.isEmpty()) {
            maxcc = stack.pop();
        }
        return this;
    }

    @Override
    public ProcStatus peek() {
        if (!stack.isEmpty()) {
            maxcc = stack.peek();
        }
        return this;
    }

    protected int runStep(StatsCount c, IntSupplier step) {
        MDC.put(STEP_NAME, c.name());
        log.info("Step {} start", c.name());
        Instant tiStart = Instant.now();
        int returnCode = Integer.MAX_VALUE;
        try {
            returnCode = step.getAsInt();
        } catch (BatchException e) {
            log.error("Execution Error", e);
            returnCode = e.getReturnCode();
            Throwable cause = e.getCause();
            c.error(cause==null ? e : cause);
        } catch (Exception e) {
            log.error("Unhandled Error", e);
            returnCode = jcl.rcErrorStep();
            c.error(e);
        } finally {
            c.recap(returnCode);  // log returnCode and custom statistics
            Instant tiEnd = Instant.now();
            Duration lapse = Duration.between(tiStart, tiEnd);
            log.info("Step {} end: {}", c.name(), DateTimeFormatter.ISO_LOCAL_TIME.format(lapse.addTo(LocalTime.of(0, 0))));
            MDC.remove(STEP_NAME);
            jobCount.add(c.name(), returnCode, tiStart, tiEnd);
        }
        return returnCode;
    }


    protected void wrapper(StatsCount c, IntSupplier step) {
        int returnCode = runStep(c, step);
        maxcc(returnCode);
    }

    protected void maxcc(int returnCode) {
        if (returnCode > maxcc)
            maxcc = returnCode;
    }

    protected int complete() {
        jobCount.recap(maxcc);
        MDC.remove(JOB_NAME);
        stack.clear();
        return maxcc;
    }


    @Override
    public <P, C extends StatsCount> ProcStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        wrapper(c, () -> pgm.apply(p, c));
        return this;
    }

    @Override
    public <C extends StatsCount> ProcStatus execPgm(C c, ToIntFunction<C> pgm) {
        wrapper(c, () -> pgm.applyAsInt(c));
        return this;
    }

    @Override
    public <P, C extends StatsCount> ProcStatus execPgm(P p, C c, BiConsumer<P, C> pgm) {
        wrapper(c, () -> {
            pgm.accept(p, c);
            return jcl.rcOk();
        });
        return this;
    }

    @Override
    public <C extends StatsCount> ProcStatus execPgm(C c, Consumer<C> pgm) {
        wrapper(c, () -> {
            pgm.accept(c);
            return jcl.rcOk();
        });
        return this;
    }

    public <P, C extends StatsCount> ProcStatus nextPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    public <C extends StatsCount> ProcStatus nextPgm(C c, ToIntFunction<C> pgm) {
        if (isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    public <P, C extends StatsCount> ProcStatus nextPgm(P p, C c, BiConsumer<P, C> pgm) {
        if (isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    public <C extends StatsCount> ProcStatus nextPgm(C c, Consumer<C> pgm) {
        if (isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }


    @Override
    public <P, C extends StatsCount> ProcStatus elsePgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (!isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> ProcStatus elsePgm(C c, ToIntFunction<C> pgm) {
        if (!isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <P, C extends StatsCount> ProcStatus elsePgm(P p, C c, BiConsumer<P, C> pgm) {
        if (!isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> ProcStatus elsePgm(C c, Consumer<C> pgm) {
        if (!isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }
}
