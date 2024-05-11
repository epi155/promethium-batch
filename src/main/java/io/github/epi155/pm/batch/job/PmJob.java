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
class PmJob implements JobStatus {
    private static final String JOB_NAME;
    private static final String STEP_NAME;
    private static final String BG = "&";
    private static final ThreadLocal<Boolean> isBackgorund = new ThreadLocal<>();

    static {
        JOB_NAME = JCL.getInstance().jobName();
        STEP_NAME = JCL.getInstance().stepName();
    }

    private final JCL jcl;
    private final String jobName;
    private final Deque<Integer> stack = new LinkedList<>();
    private final JobCount jobCount;
    private final JobTrace jobTrace;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final List<Future<Integer>> futures = new LinkedList<>();
    private int maxcc;
    private Integer lastcc;

    private PmJob(int rc, JCL jcl, String jobName) {
        this.maxcc = rc;
        this.jcl = jcl;
        this.jobName = jobName;
        this.jobCount = new JobCount(jobName);
        this.jobTrace = null;
        MDC.put(JOB_NAME, jobName);
        isBackgorund.set(false);
    }

    protected PmJob(String name, JobTrace jobTrace) {
        this.maxcc = 0;
        this.jcl = JCL.getInstance();
        this.jobName = name;
        this.jobCount = new JobCount(jobName);
        this.jobTrace = jobTrace;
    }

    static PmJob of(int rc, JCL jcl, String jobName) {
        return new PmJob(rc, jcl, jobName);
    }

    @Override
    public JobStatus push() {
        stack.push(maxcc);
        add("push()", maxcc);
        return this;
    }

    @Override
    public JobStatus pop() {
        if (!stack.isEmpty()) {
            maxcc = stack.pop();
            add("pop()", maxcc);
        }
        return this;
    }

    @Override
    public JobStatus peek() {
        if (!stack.isEmpty()) {
            maxcc = stack.peek();
            add("peek()", maxcc);
        }
        return this;
    }

    @Override
    public Optional<Integer> returnCode(String stepName) {
        return jobCount.getReturnCode(stepName);
    }

    @Override
    public <P extends Iterable<Q>, Q> JobStatus forEachProc(P qs, Function<Q, String> name, Proc<Q> proc) {
        for (Q q : qs) {
            execProc(q, name.apply(q), proc);
        }
        return this;
    }

    @Override
    public <P> JobStatus execProc(P p, String procName, Proc<P> proc) {
        int returnCode = runProc(procName, s -> proc.call(p, s));
        maxcc(returnCode);
        return this;
    }

    @Override
    public JobStatus execProc(String procName, Proc<Void> proc) {
        return execProc(null, procName, proc);
    }

    @Override
    public CondStatus<JobStatus> cond(int cc, Cond cond) {
        boolean skip = cond.test(lastcc, cc);
        return new JobCondStatus(skip);
    }


//    @Override
//    public ParallelStatus parallel(int nThreads) {
//        return new ParallelStatus() {
//            private final Semaphore semaphore = new Semaphore(nThreads);
//            private final List<Future<Integer>> promises = new LinkedList<>();
//
//            private <Q> JobStatus submit(Iterable<Q> qs, ToIntFunction<Q> fcn) {
//                final ExecutorService loopService = Executors.newFixedThreadPool(nThreads);
//                try {
//                    for (Q q : qs) {
//                        if (!PmJob.this.isSuccess())
//                            break;
//                        try {
//                            while (!semaphore.tryAcquire(5, TimeUnit.MILLISECONDS)) {
//                                probePromise(true);
//                            }
//                            Future<Integer> promise = loopService.submit(() -> {
//                                MDC.put(JOB_NAME, jobName);
//                                try {
//                                    return fcn.applyAsInt(q);
//                                } finally {
//                                    MDC.clear();
//                                }
//                            });
//                            promises.add(promise);
//                        } catch (InterruptedException e) {
//                            Thread.currentThread().interrupt();
//                        }
//                    }
//                    probePromise(false);
//                } finally {
//                    loopService.shutdown();
//                }
//                return PmJob.this;
//            }
//
//            private void probePromise(boolean hot) {
//                do {
//                    int n = iteratePromise();
//                    if (hot || promises.isEmpty())
//                        return;
//                    try {
//                        // if task is interrupted: sleep do nothing
//                        TimeUnit.MILLISECONDS.sleep(10);
//                    } catch (InterruptedException e) {
//                        try {
//                            // now task IS NOT interrupted and sleep() DO SLEEP
//                            TimeUnit.MILLISECONDS.sleep(10);
//                        } catch (InterruptedException ex) {
//                            Thread.currentThread().interrupt();
//                        }
//                        Thread.currentThread().interrupt();
//                    }
//                } while (true);
//            }
//
//            private int iteratePromise() {
//                Iterator<Future<Integer>> it = promises.iterator();
//                int k = 0;
//                while (it.hasNext()) {
//                    Future<Integer> promise = it.next();
//                    if (promise.isDone()) {
//                        try {
//                            int rc = promise.get();
//                            PmJob.this.maxcc(rc);
//                        } catch (InterruptedException e) {
//                            Thread.currentThread().interrupt();
//                        } catch (ExecutionException e) {    // dead branch
//                            PmJob.this.maxcc(jcl.rcErrorJob());
//                        } finally {
//                            semaphore.release();
//                        }
//                        it.remove();
//                    } else {
//                        k++;
//                    }
//                }
//                return k;
//            }
//
//            @Override
//            public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P qs, Function<Q, C> c, BiFunction<Q, C, Integer> pgm) {
//                return submit(qs, q -> {
//                    C count = c.apply(q);
//                    return runStep(count, () -> pgm.apply(q, count));
//                });
//            }
//
//            @Override
//            public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P qs, Function<Q, C> c, BiConsumer<Q, C> pgm) {
//                return submit(qs, q -> {
//                    C count = c.apply(q);
//                    return runStep(count, () -> {
//                        pgm.accept(q, count);
//                        return jcl.rcOk();
//                    });
//                });
//            }
//
//            @Override
//            public <P extends Iterable<Q>, Q> JobStatus forEachProc(P qs, Function<Q, String> name, UnaryOperator<SubStatus<Q>> proc) {
//                return submit(qs, q -> runSub(q, name.apply(q), proc));
//            }
//        };
//    }

    @Override
    public CondStatus<JobStatus> cond(int cc, Cond cond, String stepName) {
        boolean skip = returnCode(stepName).filter(rc -> cond.test(rc, cc)).isPresent();
        return new JobCondStatus(skip);
    }

    public JobStatus exec(@NotNull Consumer<JobStatus> action) {
        action.accept(this);
        return this;
    }

    @Override
    public int maximumConditionCode() {
        return maxcc;
    }

    @Override
    public int complete() {
        jobCount.recap(maxcc);
        if (jobTrace == null)
            MDC.remove(JOB_NAME);
        stack.clear();
        return maxcc;
    }

    private int joinResult(Future<Integer> ele) throws InterruptedException {
        int rc;
        try {
            Integer returnCode = ele.get();
            if (returnCode != null) {
                rc = returnCode;
            } else {    // dead branch
                log.error("Null returnCode");
                rc = jcl.rcErrorJob();
            }
        } catch (ExecutionException e) {    // dead branch
            log.error("Unhandled Error", e);
            rc = jcl.rcErrorStep();
        }
//        maxcc(rc);
        return rc;
    }

    private void sendMessage(int j, int r) {
        if ((j & 0x1ff) != 0) {
            int n = 0;
            while (j != 0) {
                if ((j & 0x01) == 1) n++;
                j >>= 1;
            }
            if (n != 1) return;
        }
        log.info("Waiting {} running step", r);
    }

    protected int runStep(StatsCount c, IntSupplier step) {
        String name = c.name();
        MDC.put(STEP_NAME, fullName(name));
        log.info("Step {} start", name);
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
            log.info("Step {} end: {}", name, DateTimeFormatter.ISO_LOCAL_TIME.format(lapse.addTo(LocalTime.of(0, 0))));
            MDC.remove(STEP_NAME);
            add((isBackgorund.get()) ? name + BG : name, returnCode, tiStart, tiEnd);
        }
        return returnCode;
    }

    protected void maxcc(int returnCode) {
        lastcc = returnCode;
        if (returnCode > maxcc)
            maxcc = returnCode;
    }

    private String fullName(String name) {
        return jobTrace == null ? jobCount.fullName(name) : jobTrace.fullName(name);
    }

    @Override
    public JobStatus join() {
        Integer maxRc = null;
        for (int k = 1; !futures.isEmpty(); k++) {
            val it = futures.iterator();
            int nmAlive = 0;
            try {
                while (it.hasNext()) {
                    val ele = it.next();
                    if (ele.isDone()) {
                        int rc = joinResult(ele);
                        if (maxRc == null || rc > maxRc) maxRc = rc;
                        it.remove();
                    } else {
                        nmAlive++;
                    }
                }
                if (nmAlive > 0) {
                    sendMessage(k, nmAlive);
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (maxRc == null) {
            add("join()");
        } else {
            maxcc(maxRc);
            add("join()", maxRc);
        }
        return this;
    }

    public void add(String name, int rc, Instant tiStart, Instant tiEnd) {
        jobCount.add(name, rc, tiStart, tiEnd);
        if (jobTrace != null) jobTrace.add(name, rc, tiStart, tiEnd);
    }

    private void add(String cmd, int rc) {
        jobCount.add(cmd, rc);
        if (jobTrace != null) jobTrace.add(cmd, rc);
    }

    private void add(String cmd) {
        jobCount.add(cmd);
        if (jobTrace != null) jobTrace.add(cmd);
    }

    protected void wrapper(StatsCount c, IntSupplier step) {
        int returnCode = runStep(c, step);
        maxcc(returnCode);
    }

    @Override
    public <P, C extends StatsCount> JobStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        wrapper(c, () -> pgm.apply(p, c));
        return this;
    }

    @Override
    public <P> JobStatus execPgm(P p, String stepName, ToIntFunction<P> pgm) {
        return execPgm(p, new BareCount(stepName), (xp, xc) -> {
            return pgm.applyAsInt(xp);
        });
    }

    @Override
    public <C extends StatsCount> JobStatus execPgm(C c, ToIntFunction<C> pgm) {
        wrapper(c, () -> pgm.applyAsInt(c));
        return this;
    }

    @Override
    public JobStatus execPgm(String stepName, IntSupplier pgm) {
        return execPgm(new BareCount(stepName), (xc) -> {
            return pgm.getAsInt();
        });
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
    public <P> JobStatus execPgm(P p, String stepName, Consumer<P> pgm) {
        return execPgm(p, new BareCount(stepName), (xp, xc) -> {
            pgm.accept(xp);
        });
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
    public JobStatus execPgm(String stepName, Runnable pgm) {
        return execPgm(new BareCount(stepName), (xc) -> {
            pgm.run();
        });
    }

    private JobStatus submit(Callable<Integer> step) {
        futures.add(executorService.submit(() -> {
            try {
                MDC.put(JOB_NAME, jobName);
                isBackgorund.set(true);
                return step.call();
            } finally {
                MDC.remove(JOB_NAME);
                isBackgorund.remove();
            }
        }));
        return this;
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        return submit(() -> runStep(c, () -> pgm.apply(p, c)));
    }

    @Override
    public <P> JobStatus forkPgm(P p, String stepName, ToIntFunction<P> pgm) {
        return forkPgm(p, new BareCount(stepName), (xp, xc) -> {
            return pgm.applyAsInt(xp);
        });
    }

    @Override
    public <C extends StatsCount> JobStatus forkPgm(C c, ToIntFunction<C> pgm) {
        return submit(() -> runStep(c, () -> pgm.applyAsInt(c)));
    }

    @Override
    public JobStatus forkPgm(String stepName, IntSupplier pgm) {
        return forkPgm(new BareCount(stepName), (xc) -> {
            return pgm.getAsInt();
        });
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkPgm(P p, C c, BiConsumer<P, C> pgm) {
        return submit(() -> runStep(c, () -> {
            pgm.accept(p, c);
            return jcl.rcOk();
        }));
    }

    @Override
    public <P> JobStatus forkPgm(P p, String stepName, Consumer<P> pgm) {
        return forkPgm(p, new BareCount(stepName), (xp, xc) -> {
            pgm.accept(xp);
        });
    }

    @Override
    public <C extends StatsCount> JobStatus forkPgm(C c, Consumer<C> pgm) {
        return submit(() -> runStep(c, () -> {
            pgm.accept(c);
            return jcl.rcOk();
        }));
    }

    @Override
    public JobStatus forkPgm(String stepName, Runnable pgm) {
        return forkPgm(new BareCount(stepName), (xc) -> {
            pgm.run();
        });
    }

    @Override
    public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P p, Function<Q, C> c, BiFunction<Q, C, Integer> pgm) {
        for (Q q : p) {
            execPgm(q, c.apply(q), pgm);
        }
        return this;
    }

    @Override
    public <P extends Iterable<Q>, Q> JobStatus forEachPgm(P p, Function<Q, String> name, ToIntFunction<Q> pgm) {
        for (Q q : p) {
            execPgm(q, name.apply(q), pgm);
        }
        return this;
    }

    @Override
    public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P p, Function<Q, C> c, BiConsumer<Q, C> pgm) {
        for (Q q : p) {
            execPgm(q, c.apply(q), pgm);
        }
        return this;
    }

    @Override
    public <P extends Iterable<Q>, Q> JobStatus forEachPgm(P p, Function<Q, String> name, Consumer<Q> pgm) {
        for (Q q : p) {
            execPgm(q, name.apply(q), pgm);
        }
        return this;
    }

    @Override
    public <P> JobStatus forkProc(P p, String procName, Proc<P> proc) {
        return submit(() -> runProc(procName, s -> proc.call(p, s)));
    }

    private Integer runProc(String procName, ToIntFunction<JobStatus> fcn) {
        MDC.put(STEP_NAME, fullName(procName));
        JobStatus s = new PmJob(jobName, new PmJobTrace(fullName(procName)));
        log.info("Proc {} start", procName);
        Instant tiStart = Instant.now();
        int returnCode = Integer.MAX_VALUE;
        try {
            returnCode = fcn.applyAsInt(s);
        } catch (Exception e) {
            log.error("Unhandled Error", e);
            returnCode = jcl.rcErrorJob();
        } finally {
            Instant tiEnd = Instant.now();
            Duration lapse = Duration.between(tiStart, tiEnd);
            log.info("Proc {} end: {}", procName, DateTimeFormatter.ISO_LOCAL_TIME.format(lapse.addTo(LocalTime.of(0, 0))));
            MDC.remove(STEP_NAME);
            add((isBackgorund.get()) ? procName + BG : procName, returnCode, tiStart, tiEnd);
        }
        return returnCode;
    }

    @Override
    public JobStatus forkProc(String procName, Proc<Void> proc) {
        return forkProc(null, procName, proc);
    }

    private class PmJobTrace implements JobTrace {
        private final String prefix;
        private final JobTrace trace;

        private PmJobTrace(String prefix) {
            this.prefix = prefix;
            this.trace = jobTrace == null ? jobCount : jobTrace;
        }

        @Override
        public void add(String name, int returnCode, Instant tiStart, Instant tiEnd) {
            trace.add(fullName(name), returnCode, tiStart, tiEnd);
        }

        @Override
        public void add(String name, int returnCode) {
            trace.add(fullName(name), returnCode);
        }

        @Override
        public void add(String name) {
            trace.add(fullName(name));
        }

        @Override
        public String fullName(String name) {
            return prefix + "." + name;
        }
    }

    private class JobCondStatus implements CondStatus<JobStatus> {
        private final boolean skip;
        private final JobTrace trace;

        private JobCondStatus(boolean skip) {
            this.skip = skip;
            this.trace = jobTrace == null ? jobCount : jobTrace;
        }

        @Override
        public <P, C extends StatsCount> JobStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
            if (skip) {
                trace.add(c.name());
                return PmJob.this;
            } else {
                return PmJob.this.execPgm(p, c, pgm);
            }
        }

        @Override
        public <P> JobStatus execPgm(P p, String stepName, ToIntFunction<P> pgm) {
            if (skip) {
                trace.add(stepName);
                return PmJob.this;
            } else {
                return PmJob.this.execPgm(p, stepName, pgm);
            }
        }

        @Override
        public <C extends StatsCount> JobStatus execPgm(C c, ToIntFunction<C> pgm) {
            if (skip) {
                trace.add(c.name());
                return PmJob.this;
            } else {
                return PmJob.this.execPgm(c, pgm);
            }
        }

        @Override
        public JobStatus execPgm(String stepName, IntSupplier pgm) {
            if (skip) {
                trace.add(stepName);
                return PmJob.this;
            } else {
                return PmJob.this.execPgm(stepName, pgm);
            }
        }

        @Override
        public <P, C extends StatsCount> JobStatus execPgm(P p, C c, BiConsumer<P, C> pgm) {
            if (skip) {
                trace.add(c.name());
                return PmJob.this;
            } else {
                return PmJob.this.execPgm(p, c, pgm);
            }
        }

        @Override
        public <P> JobStatus execPgm(P p, String stepName, Consumer<P> pgm) {
            if (skip) {
                trace.add(stepName);
                return PmJob.this;
            } else {
                return PmJob.this.execPgm(p, stepName, pgm);
            }
        }

        @Override
        public <C extends StatsCount> JobStatus execPgm(C c, Consumer<C> pgm) {
            if (skip) {
                trace.add(c.name());
                return PmJob.this;
            } else {
                return PmJob.this.execPgm(c, pgm);
            }
        }

        @Override
        public JobStatus execPgm(String stepName, Runnable pgm) {
            if (skip) {
                trace.add(stepName);
                return PmJob.this;
            } else {
                return PmJob.this.execPgm(stepName, pgm);
            }
        }

        @Override
        public <P> JobStatus execProc(P p, String procName, Proc<P> proc) {
            if (skip) {
                trace.add(procName);
                return PmJob.this;
            } else {
                return PmJob.this.execProc(p, procName, proc);
            }
        }

        @Override
        public JobStatus execProc(String procName, Proc<Void> proc) {
            if (skip) {
                trace.add(procName);
                return PmJob.this;
            } else {
                return PmJob.this.execProc(procName, proc);
            }
        }

        @Override
        public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P qs, Function<Q, C> c, BiFunction<Q, C, Integer> pgm) {
            if (skip) {
                for (Q q : qs) {
                    trace.add(c.apply(q).name());
                }
                return PmJob.this;
            } else {
                return PmJob.this.forEachPgm(qs, c, pgm);
            }
        }

        @Override
        public <P extends Iterable<Q>, Q> JobStatus forEachPgm(P qs, Function<Q, String> name, ToIntFunction<Q> pgm) {
            if (skip) {
                for (Q q : qs) {
                    trace.add(name.apply(q));
                }
                return PmJob.this;
            } else {
                return PmJob.this.forEachPgm(qs, name, pgm);
            }
        }

        @Override
        public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P qs, Function<Q, C> c, BiConsumer<Q, C> pgm) {
            if (skip) {
                for (Q q : qs) {
                    trace.add(c.apply(q).name());
                }
                return PmJob.this;
            } else {
                return PmJob.this.forEachPgm(qs, c, pgm);
            }
        }

        @Override
        public <P extends Iterable<Q>, Q> JobStatus forEachPgm(P qs, Function<Q, String> name, Consumer<Q> pgm) {
            if (skip) {
                for (Q q : qs) {
                    trace.add(name.apply(q));
                }
                return PmJob.this;
            } else {
                return PmJob.this.forEachPgm(qs, name, pgm);
            }
        }

        @Override
        public <P, C extends StatsCount> JobStatus forkPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
            if (skip) {
                trace.add(c.name());
                return PmJob.this;
            } else {
                return PmJob.this.forkPgm(p, c, pgm);
            }
        }

        @Override
        public <P> JobStatus forkPgm(P p, String stepName, ToIntFunction<P> pgm) {
            if (skip) {
                trace.add(stepName);
                return PmJob.this;
            } else {
                return PmJob.this.forkPgm(p, stepName, pgm);
            }
        }

        @Override
        public <C extends StatsCount> JobStatus forkPgm(C c, ToIntFunction<C> pgm) {
            if (skip) {
                trace.add(c.name());
                return PmJob.this;
            } else {
                return PmJob.this.forkPgm(c, pgm);
            }
        }

        @Override
        public JobStatus forkPgm(String stepName, IntSupplier pgm) {
            if (skip) {
                trace.add(stepName);
                return PmJob.this;
            } else {
                return PmJob.this.forkPgm(stepName, pgm);
            }
        }

        @Override
        public <P, C extends StatsCount> JobStatus forkPgm(P p, C c, BiConsumer<P, C> pgm) {
            if (skip) {
                trace.add(c.name());
                return PmJob.this;
            } else {
                return PmJob.this.forkPgm(p, c, pgm);
            }
        }

        @Override
        public <P> JobStatus forkPgm(P p, String stepName, Consumer<P> pgm) {
            if (skip) {
                trace.add(stepName);
                return PmJob.this;
            } else {
                return PmJob.this.forkPgm(p, stepName, pgm);
            }
        }

        @Override
        public <C extends StatsCount> JobStatus forkPgm(C c, Consumer<C> pgm) {
            if (skip) {
                trace.add(c.name());
                return PmJob.this;
            } else {
                return PmJob.this.forkPgm(c, pgm);
            }
        }

        @Override
        public JobStatus forkPgm(String stepName, Runnable pgm) {
            if (skip) {
                trace.add(stepName);
                return PmJob.this;
            } else {
                return PmJob.this.forkPgm(stepName, pgm);
            }
        }

        @Override
        public <P extends Iterable<Q>, Q> JobStatus forEachProc(P qs, Function<Q, String> name, Proc<Q> proc) {
            if (skip) {
                for (Q q : qs) {
                    trace.add(name.apply(q));
                }
                return PmJob.this;
            } else {
                return PmJob.this.forEachProc(qs, name, proc);
            }
        }

        @Override
        public <P> JobStatus forkProc(P p, String procName, Proc<P> proc) {
            if (skip) {
                trace.add(procName);
                return PmJob.this;
            } else {
                return PmJob.this.forkProc(p, procName, proc);
            }
        }

        @Override
        public JobStatus forkProc(String procName, Proc<Void> proc) {
            return forkProc(null, procName, proc);
        }
    }
}
