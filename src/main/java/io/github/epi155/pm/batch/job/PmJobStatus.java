package io.github.epi155.pm.batch.job;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.*;

@Slf4j
class PmJobStatus extends PmProcStatus implements JobStatus {
    private static final String JOB_NAME;

    static {
        JOB_NAME = JCL.getInstance().jobName();
    }

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
        jobCount.add("push()", maxcc);
        return this;
    }

    @Override
    public JobStatus pop() {
        if (!stack.isEmpty()) {
            maxcc = stack.pop();
            jobCount.add("pop()", maxcc);
        }
        return this;
    }

    @Override
    public JobStatus peek() {
        if (!stack.isEmpty()) {
            maxcc = stack.peek();
            jobCount.add("peek()", maxcc);
        }
        return this;
    }

    @Override
    public ParallelStatus parallel(int nThreads) {
        return new ParallelStatus() {
            private final Semaphore semaphore = new Semaphore(nThreads);
            private final List<Future<Integer>> promises = new LinkedList<>();

            private <Q> JobStatus submit(Iterable<Q> qs, ToIntFunction<Q> fcn) {
                final ExecutorService loopService = Executors.newFixedThreadPool(nThreads);
                try {
                    for (Q q : qs) {
                        if (!PmJobStatus.this.isSuccess())
                            break;
                        try {
                            while (!semaphore.tryAcquire(5, TimeUnit.MILLISECONDS)) {
                                probePromise(true);
                            }
                            Future<Integer> promise = loopService.submit(() -> {
                                MDC.put(JOB_NAME, jobName);
                                try {
                                    return fcn.applyAsInt(q);
                                } finally {
                                    MDC.clear();
                                }
                            });
                            promises.add(promise);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    probePromise(false);
                } finally {
                    loopService.shutdown();
                }
                return PmJobStatus.this;
            }

            private void probePromise(boolean hot) {
                do {
                    int n = iteratePromise();
                    if (hot || promises.isEmpty())
                        return;
                    try {
                        // if task is interrupted: sleep do nothing
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        try {
                            // now task IS NOT interrupted and sleep() DO SLEEP
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        Thread.currentThread().interrupt();
                    }
                } while (true);
            }

            private int iteratePromise() {
                Iterator<Future<Integer>> it = promises.iterator();
                int k = 0;
                while (it.hasNext()) {
                    Future<Integer> promise = it.next();
                    if (promise.isDone()) {
                        try {
                            int rc = promise.get();
                            PmJobStatus.this.maxcc(rc);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {    // dead branch
                            PmJobStatus.this.maxcc(jcl.rcErrorJob());
                        } finally {
                            semaphore.release();
                        }
                        it.remove();
                    } else {
                        k++;
                    }
                }
                return k;
            }

            @Override
            public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P qs, Function<Q, C> c, BiFunction<Q, C, Integer> pgm) {
                return submit(qs, q -> {
                    C count = c.apply(q);
                    return runStep(count, () -> pgm.apply(q, count));
                });
            }

            @Override
            public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P qs, Function<Q, C> c, BiConsumer<Q, C> pgm) {
                return submit(qs, q -> {
                    C count = c.apply(q);
                    return runStep(count, () -> {
                        pgm.accept(q, count);
                        return jcl.rcOk();
                    });
                });
            }

            @Override
            public <P extends Iterable<Q>, Q> JobStatus forEachProc(P qs, Function<Q, String> name, UnaryOperator<SubStatus<Q>> proc) {
                return submit(qs, q -> runSub(q, name.apply(q), proc));
            }
        };
    }

    public <P, C extends StatsCount> JobStatus nextPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <P> JobStatus nextPgm(P p, String stepName, ToIntFunction<P> pgm) {
        return nextPgm(p, new BareCount(stepName), (xp, xc) -> {
            return pgm.applyAsInt(xp);
        });
    }

    public <C extends StatsCount> JobStatus nextPgm(C c, ToIntFunction<C> pgm) {
        if (isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public JobStatus nextPgm(String stepName, IntSupplier pgm) {
        return nextPgm(new BareCount(stepName), (xc) -> {
            return pgm.getAsInt();
        });
    }

    public <P, C extends StatsCount> JobStatus nextPgm(P p, C c, BiConsumer<P, C> pgm) {
        if (isSuccess()) {
            return execPgm(p, c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public <P> JobStatus nextPgm(P p, String stepName, Consumer<P> pgm) {
        return nextPgm(p, new BareCount(stepName), (xp, xc) -> {
            pgm.accept(xp);
        });
    }

    public <C extends StatsCount> JobStatus nextPgm(C c, Consumer<C> pgm) {
        if (isSuccess()) {
            return execPgm(c, pgm);
        } else {
            jobCount.add(c.name());
        }
        return this;
    }

    @Override
    public JobStatus nextPgm(String stepName, Runnable pgm) {
        return nextPgm(new BareCount(stepName), (xc) -> {
            pgm.run();
        });
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
        ProcStatus procStatus = new PmProcStatus(jcl.rcOk(), jcl, procName, new JobTrace() {
            @Override
            public void add(String name, int returnCode, Instant tiStart, Instant tiEnd) {
                jobCount.add(procName + "." + name, returnCode, tiStart, tiEnd);
            }

            @Override
            public void add(String name, int returnCode) {
                jobCount.add(procName + "." + name, returnCode);
            }

            @Override
            public void add(String name) {
                jobCount.add(procName + "." + name);
            }
        });
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
        ProcStatus procStatus = new PmProcStatus(jcl.rcOk(), jcl, procName, new JobTrace() {
            @Override
            public void add(String name, int returnCode, Instant tiStart, Instant tiEnd) {
                jobCount.add(procName + "." + name, returnCode, tiStart, tiEnd);
            }

            @Override
            public void add(String name, int returnCode) {
                jobCount.add(procName + "." + name, returnCode);
            }

            @Override
            public void add(String name) {
                jobCount.add(procName + "." + name);
            }
        });
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
    public <P extends Iterable<Q>, Q> JobStatus forEachProc(P p, Function<Q, String> name, UnaryOperator<SubStatus<Q>> proc) {
        for (Q q : p) {
            nextSub(q, name.apply(q), proc);
        }
        return this;
    }

    private <Q> JobStatus nextSub(Q q, String subName, UnaryOperator<SubStatus<Q>> sub) {
        if (isSuccess()) {
            wrapSub(q, subName, sub);
        } else {
            jobCount.add(subName);
        }
        return this;
    }

    private <Q> int runSub(Q q, String subName, UnaryOperator<SubStatus<Q>> sub) {
        SubStatus<Q> subStatus = new PmSubStatus<Q>(jcl.rcOk(), jcl, q, subName, new JobTrace() {
            @Override
            public void add(String name, int returnCode, Instant tiStart, Instant tiEnd) {
                jobCount.add(subName + "." + name, returnCode, tiStart, tiEnd);
            }

            @Override
            public void add(String name, int returnCode) {
                jobCount.add(subName + "." + name, returnCode);
            }

            @Override
            public void add(String name) {
                jobCount.add(subName + "." + name);
            }
        });
        Instant tiStart = Instant.now();
        int returnCode = -1;
        try {
            MDC.put(JOB_NAME, subName);
            returnCode = ((PmSubStatus<Q>) sub.apply(subStatus)).complete();
        } finally {
            Instant tiEnd = Instant.now();
            MDC.put(JOB_NAME, jobName);
            jobCount.add(subName, returnCode, tiStart, tiEnd);
        }
        return returnCode;
    }

    private <Q> void wrapSub(Q q, String subName, UnaryOperator<SubStatus<Q>> sub) {
        int returnCode = runSub(q, subName, sub);
        maxcc(returnCode);
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
    public <P> JobStatus elsePgm(P p, String stepName, ToIntFunction<P> pgm) {
        return elsePgm(p, new BareCount(stepName), (xp, xc) -> {
            return pgm.applyAsInt(xp);
        });
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
    public JobStatus elsePgm(String stepName, IntSupplier pgm) {
        return elsePgm(new BareCount(stepName), (xc) -> {
            return pgm.getAsInt();
        });
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
    public <P> JobStatus elsePgm(P p, String stepName, Consumer<P> pgm) {
        return elsePgm(p, new BareCount(stepName), (xp, xc) -> {
            pgm.accept(xp);
        });
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

    @Override
    public JobStatus elsePgm(String stepName, Runnable pgm) {
        return elsePgm(new BareCount(stepName), (xc) -> {
            pgm.run();
        });
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
    public <P> JobStatus forkExecPgm(P p, String stepName, ToIntFunction<P> pgm) {
        return forkExecPgm(p, new BareCount(stepName), (xp, xc) -> {
            return pgm.applyAsInt(xp);
        });
    }

    @Override
    public <C extends StatsCount> JobStatus forkExecPgm(C c, ToIntFunction<C> pgm) {
        return submit(() -> runStep(c, () -> pgm.applyAsInt(c)));
    }

    @Override
    public JobStatus forkExecPgm(String stepName, IntSupplier pgm) {
        return forkExecPgm(new BareCount(stepName), (xc) -> {
            return pgm.getAsInt();
        });
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkExecPgm(P p, C c, BiConsumer<P, C> pgm) {
        return submit(() -> runStep(c, () -> {
            pgm.accept(p, c);
            return jcl.rcOk();
        }));
    }

    @Override
    public <P> JobStatus forkExecPgm(P p, String stepName, Consumer<P> pgm) {
        return forkExecPgm(p, new BareCount(stepName), (xp, xc) -> {
            pgm.accept(xp);
        });
    }

    @Override
    public <C extends StatsCount> JobStatus forkExecPgm(C c, Consumer<C> pgm) {
        return submit(() -> runStep(c, () -> {
            pgm.accept(c);
            return jcl.rcOk();
        }));
    }

    @Override
    public JobStatus forkExecPgm(String stepName, Runnable pgm) {
        return forkExecPgm(new BareCount(stepName), (xc) -> {
            pgm.run();
        });
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
    public <P> JobStatus forkElsePgm(P p, String stepName, ToIntFunction<P> pgm) {
        return forkElsePgm(p, new BareCount(stepName), (xp, xc) -> {
            return pgm.applyAsInt(xp);
        });
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
    public JobStatus forkElsePgm(String stepName, IntSupplier pgm) {
        return forkElsePgm(new BareCount(stepName), (xc) -> {
            return pgm.getAsInt();
        });
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
    public <P> JobStatus forkElsePgm(P p, String stepName, Consumer<P> pgm) {
        return forkElsePgm(p, new BareCount(stepName), (xp, xc) -> {
            pgm.accept(xp);
        });
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
    public JobStatus forkElsePgm(String stepName, Runnable pgm) {
        return forkElsePgm(new BareCount(stepName), (xc) -> {
            pgm.run();
        });
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
    public <P> JobStatus forkNextPgm(P p, String stepName, ToIntFunction<P> pgm) {
        return forkNextPgm(p, new BareCount(stepName), (xp, xc) -> {
            return pgm.applyAsInt(xp);
        });
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
    public JobStatus forkNextPgm(String stepName, IntSupplier pgm) {
        return forkNextPgm(new BareCount(stepName), (xc) -> {
            return pgm.getAsInt();
        });
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
    public <P> JobStatus forkNextPgm(P p, String stepName, Consumer<P> pgm) {
        return forkNextPgm(p, new BareCount(stepName), (xp, xc) -> {
            pgm.accept(xp);
        });
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

    @Override
    public JobStatus forkNextPgm(String stepName, Runnable pgm) {
        return forkNextPgm(new BareCount(stepName), (xc) -> {
            pgm.run();
        });
    }

    @Override
    public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P p, Function<Q, C> c, BiFunction<Q, C, Integer> pgm) {
        if (isSuccess()) {
            for (Q q : p) {
                nextPgm(q, c.apply(q), pgm);
            }
        }
        return this;
    }

    @Override
    public <P extends Iterable<Q>, Q> JobStatus forEachPgm(P p, Function<Q, String> name, ToIntFunction<Q> pgm) {
        if (isSuccess()) {
            for (Q q : p) {
                nextPgm(q, name.apply(q), pgm);
            }
        }
        return this;
    }

    @Override
    public <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P p, Function<Q, C> c, BiConsumer<Q, C> pgm) {
        if (isSuccess()) {
            for (Q q : p) {
                nextPgm(q, c.apply(q), pgm);
            }
        }
        return this;
    }

    @Override
    public <P extends Iterable<Q>, Q> JobStatus forEachPgm(P p, Function<Q, String> name, Consumer<Q> pgm) {
        if (isSuccess()) {
            for (Q q : p) {
                nextPgm(q, name.apply(q), pgm);
            }
        }
        return this;
    }

}
