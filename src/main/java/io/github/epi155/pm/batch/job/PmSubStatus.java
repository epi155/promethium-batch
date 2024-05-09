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

@Slf4j
class PmSubStatus<Q> implements SubStatus<Q> {
    private static final String STEP_NAME;
    private static final String JOB_NAME;

    static {
        STEP_NAME = JCL.getInstance().stepName();
        JOB_NAME = JCL.getInstance().jobName();
    }

    protected final Deque<Integer> stack = new LinkedList<>();
    private final JCL jcl;
    private final Q q;
    private final String jobName;
    private final JobCount jobCount;
    private final JobTrace jobTrace;
    private int maxcc;

    public PmSubStatus(int rc, JCL jcl, Q q, String jobName, JobTrace jobTrace) {
        this.maxcc = rc;
        this.jcl = jcl;
        this.q = q;
        this.jobName = jobName;
        this.jobCount = new JobCount(jobName);
        this.jobTrace = jobTrace;
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
    public SubStatus<Q> push() {
        stack.push(maxcc);
        jobTrace.add("push()", maxcc);
        return this;
    }

    @Override
    public SubStatus<Q> pop() {
        if (!stack.isEmpty()) {
            maxcc = stack.pop();
            jobTrace.add("pop()", maxcc);
        }
        return this;
    }

    @Override
    public SubStatus<Q> peek() {
        if (!stack.isEmpty()) {
            maxcc = stack.peek();
            jobTrace.add("peek()", maxcc);
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
            c.error(cause == null ? e : cause);
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
            if (jobTrace != null) jobTrace.add(c.name(), returnCode, tiStart, tiEnd);
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
    public <C extends StatsCount> SubStatus<Q> execPgm(C c, BiFunction<Q, C, Integer> pgm) {
        wrapper(c, () -> pgm.apply(q, c));
        return this;
    }

    @Override
    public <C extends StatsCount> SubStatus<Q> execPgm(C c, BiConsumer<Q, C> pgm) {
        wrapper(c, () -> {
            pgm.accept(q, c);
            return jcl.rcOk();
        });
        return this;
    }

    @Override
    public SubStatus<Q> execPgm(String stepName, ToIntFunction<Q> pgm) {
        return execPgm(new BareCount(stepName), (qx, cx) -> {
            return pgm.applyAsInt(qx);
        });
    }

    @Override
    public SubStatus<Q> execPgm(String stepName, Consumer<Q> pgm) {
        return execPgm(new BareCount(stepName), (qx, cx) -> {
            pgm.accept(qx);
        });
    }

    @Override
    public <C extends StatsCount> SubStatus<Q> nextPgm(C c, BiFunction<Q, C, Integer> pgm) {
        if (isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
            if (jobTrace != null) jobTrace.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> SubStatus<Q> nextPgm(C c, BiConsumer<Q, C> pgm) {
        if (isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
            if (jobTrace != null) jobTrace.add(c.name());
        }
        return this;
    }

    @Override
    public SubStatus<Q> nextPgm(String stepName, ToIntFunction<Q> pgm) {
        if (isSuccess()) {
            return execPgm(stepName, pgm);
        } else {
            jobCount.add(stepName);
            if (jobTrace != null) jobTrace.add(stepName);
        }
        return this;
    }

    @Override
    public SubStatus<Q> nextPgm(String stepName, Consumer<Q> pgm) {
        if (isSuccess()) {
            return execPgm(stepName, pgm);
        } else {
            jobCount.add(stepName);
            if (jobTrace != null) jobTrace.add(stepName);
        }
        return this;
    }

    @Override
    public <C extends StatsCount> SubStatus<Q> elsePgm(C c, BiFunction<Q, C, Integer> pgm) {
        if (!isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
            if (jobTrace != null) jobTrace.add(c.name());
        }
        return this;
    }

    @Override
    public <C extends StatsCount> SubStatus<Q> elsePgm(C c, BiConsumer<Q, C> pgm) {
        if (!isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
            if (jobTrace != null) jobTrace.add(c.name());
        }
        return this;
    }

    @Override
    public SubStatus<Q> elsePgm(String stepName, ToIntFunction<Q> pgm) {
        if (!isSuccess()) {
            return execPgm(stepName, pgm);
        } else {
            jobCount.add(stepName);
            if (jobTrace != null) jobTrace.add(stepName);
        }
        return this;
    }

    @Override
    public SubStatus<Q> elsePgm(String stepName, Consumer<Q> pgm) {
        if (!isSuccess()) {
            return execPgm(stepName, pgm);
        } else {
            jobCount.add(stepName);
            if (jobTrace != null) jobTrace.add(stepName);
        }
        return this;
    }
}
