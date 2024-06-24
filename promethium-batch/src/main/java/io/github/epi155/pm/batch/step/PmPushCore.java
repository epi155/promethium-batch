package io.github.epi155.pm.batch.step;

import io.github.epi155.pm.batch.job.JCL;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.MDC;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.epi155.pm.batch.job.BatchStepException.placeOf;

@Slf4j
abstract class PmPushCore<I> implements LoopSource<I> {
    static final String JOB_NAME;
    static final String STEP_NAME;

    static {
        JOB_NAME = JCL.getInstance().jobName();
        STEP_NAME = JCL.getInstance().stepName();
    }

    Runnable beforeAction;
    long time = 30;
    TimeUnit unit = TimeUnit.SECONDS;
    Predicate<? super I> terminateTest = null;

    static <O, T extends AutoCloseable> Consumer<? super O> consumerOf(SinkResource<T, O> sink, T t) {
        return o -> sink.accept(t, o);
    }

    @Override
    public LoopSourceLayer<I> terminate(Predicate<? super I> test) {
        this.terminateTest = test;
        return this;
    }

    @Override
    public LoopSourceStd<I> before(Runnable action) {
        this.beforeAction = action;
        return this;
    }

    private void shutdown(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(time, unit)) {
                log.warn("### shutdown timeout expired, I terminate still active tasks");
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    void setShutdownTimeout(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }


    /**
     * monitors list of future's writer listener (invoked from task scheduler)
     *
     * @param main     main thread (to be interrupted on listener exception)
     * @param statuses list of futures
     */
    private void monitor(Thread main, List<Future<?>> statuses) {
        for (val status : statuses) {
            if (status.isDone()) {
                try {
                    status.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String place = placeOf(cause.getStackTrace());
                    log.warn("W.### Error detected in the listener: {} [{}]", cause.getMessage(), place);
                    main.interrupt();
                    return;
                }
            }
        }
        log.debug("W.--- The state of the listeners is healthy");
    }

    private void probeStatuses(List<Future<?>> statuses, boolean hot) {
        int k = 0;
        do {
            int n = iterateStatus(statuses.iterator());
            if (hot || statuses.isEmpty()) {
                return;
            }
            sendMessage(++k, n);
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

    private void sendMessage(int j, int r) {
        if ((j & 0x0fff) != 0) {
            int n = 0;
            while (j != 0) {
                if ((j & 0x01) == 1) n++;
                j >>= 1;
            }
            if (n != 1) return;
        }
        log.debug("*.--- Waiting {} running task", r);
    }

    private int iterateStatus(Iterator<Future<?>> it) {
        int k = 0;
        while (it.hasNext()) {
            Future<?> status = it.next();
            if (status.isDone() /*|| status.isCancelled()*/) {
                try {
                    status.get();
                } catch (InterruptedException e) {
                    log.debug("*.>>> task was interrupted. (dead branch?)");
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String place = placeOf(cause.getStackTrace());
                    log.warn("*.### Error detected in task execution: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                }
                it.remove();
            } else {
                k++;
            }
        }
        return k;
    }

    abstract class DoAsyncParallelFair<R> {
        private final Function<? super I, ? extends Future<? extends R>> transformer;
        private final Thread main;
        private final BlockingQueue<Future<? extends R>> queue;
        private final Semaphore sm;

        DoAsyncParallelFair(int maxThread, Function<? super I, ? extends Future<? extends R>> asyncTransformer) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.transformer = asyncTransformer;
            this.main = Thread.currentThread();
            this.queue = new ArrayBlockingQueue<>(maxThread, true);
            this.sm = new Semaphore(maxThread);
        }

        public void start() {
            try {
                openResources();
                log.info("s.=== completed successfully.");
            } catch (BatchException e) {
                log.error("s.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("s.### abnormal program end.", e);
                throw new BatchException(e);
            }
        }

        protected abstract void openResources() throws Exception;

        protected void doWork(Iterator<I> iterator, Consumer<R> action) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            String jobName = MDC.get(JOB_NAME);
            String stepName = MDC.get(STEP_NAME);
            try {
                Future<?> future = service1.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    try {
                        while (iterator.hasNext()) {
                            I i = iterator.next();
                            if (terminateTest != null && terminateTest.test(i)) {
                                log.warn("s.>>> Loop terminated on condition");
                                break;
                            }
                            if (beforeAction != null) beforeAction.run();
                            sm.acquire();
                            addToQueue(transformer.apply(i));
                        }
                        if (Thread.currentThread().isInterrupted()) {
                            log.warn("s.>>> Loop interrupted ...");
                        } else {
                            log.debug("s.--- All task submitted ...");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (RuntimeException e) {
                        log.warn("s.>>> Internal error.");
                        throw e;
                    } finally {
                        log.debug("s.--- waiting for the end of the tasks ...");
                        awaitEmptyQueue();
                        if (!Thread.currentThread().isInterrupted()) {
                            log.debug("s.--- interrupting the write listener");
                            main.interrupt();
                        }
                        MDC.clear();
                    }
                });
                try {
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        Future<? extends R> fo = queue.take();
                        doWrite(action, fo, future);
                    }
                } catch (InterruptedException e) {
                    log.debug("s.--- write listener interrupted");
//                    Thread.currentThread().interrupt();
                }
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String place = placeOf(cause.getStackTrace());
                    log.warn("s.### Error detected in reader: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                service1.shutdown();
            }
        }

        private void doWrite(Consumer<R> action, Future<? extends R> fo, Future<?> future) throws InterruptedException {
            try {
                R oo = fo.get();
                action.accept(oo);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                String place = placeOf(cause.getStackTrace());
                log.warn("s.### Error detected in task execution: {} [{}]", cause.getMessage(), place);
                future.cancel(true);
                throw new BatchException(cause);
            } finally {
                sm.release();
            }
        }

        private void addToQueue(Future<? extends R> promise) {
            try {
                queue.put(promise);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void awaitEmptyQueue() {
            while (!queue.isEmpty()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    abstract class DoParallelFair<R> {
        private final int maxThread;
        private final Function<? super I, ? extends R> transformer;
        private final Thread main;
        private final BlockingQueue<Future<? extends R>> queue;

        DoParallelFair(int maxThread, Function<? super I, ? extends R> transformer) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.maxThread = maxThread;
            this.transformer = transformer;
            this.main = Thread.currentThread();
            this.queue = new ArrayBlockingQueue<>(maxThread, true);
        }

        public void start() {
            try {
                openResources();
                log.info("S.=== completed successfully.");
            } catch (BatchException e) {
                log.error("S.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("S.### abnormal program end.", e);
                throw new BatchException(e);
            }
        }

        protected abstract void openResources() throws Exception;

        protected void doWork(Iterator<I> iterator, Consumer<R> action) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            String jobName = MDC.get(JOB_NAME);
            String stepName = MDC.get(STEP_NAME);
            try {
                Future<?> future = service1.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    ExecutorService service2 = Executors.newFixedThreadPool(maxThread);
                    Semaphore sm = new Semaphore(maxThread);
                    try {
                        while (iterator.hasNext()) {
                            I i = iterator.next();
                            if (terminateTest != null && terminateTest.test(i)) {
                                log.warn("S.>>> Loop terminated on condition");
                                break;
                            }
                            if (beforeAction != null) beforeAction.run();
                            try {
                                sm.acquire();
                                Future<? extends R> promise = service2.submit(() -> {
                                    MDC.put(JOB_NAME, jobName);
                                    MDC.put(STEP_NAME, stepName);
                                    try {
                                        return transformer.apply(i);
                                    } finally {
                                        sm.release();
                                        MDC.clear();
                                    }
                                });
                                addToQueue(promise);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                        if (Thread.currentThread().isInterrupted()) {
                            log.warn("S.>>> Loop interrupted, shutdown taskExecutor ...");
                        } else {
                            log.debug("S.--- All task submitted, shutdown taskExecutor ...");
                        }
                    } catch (RuntimeException e) {
                        log.warn("S.>>> Internal error.");
                        throw e;
                    } finally {
                        shutdown(service2);
                        log.debug("S.--- waiting for the end of the tasks ...");
                        awaitEmptyQueue();
                        MDC.clear();
                        if (!Thread.currentThread().isInterrupted()) {
                            log.debug("S.--- interrupting the write listener");
                            main.interrupt();
                        }
                    }
                });
                try {
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        Future<? extends R> fo = queue.take();
                        doWrite(action, fo, future);
                    }
                } catch (InterruptedException e) {
                    log.debug("S.--- write listener interrupted");
//                    Thread.currentThread().interrupt();
                }
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String place = placeOf(cause.getStackTrace());
                    log.warn("S.### Error detected in reader: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                service1.shutdown();
            }
        }

        private void doWrite(Consumer<R> action, Future<? extends R> fo, Future<?> future) throws InterruptedException {
            try {
                R oo = fo.get();
                action.accept(oo);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                String place = placeOf(cause.getStackTrace());
                log.warn("S.### Error detected in task execution: {} [{}]", cause.getMessage(), place);
                future.cancel(true);
                throw new BatchException(cause);
            }
        }

        private void addToQueue(Future<? extends R> promise) {
            try {
                queue.put(promise);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void awaitEmptyQueue() {
            while (!queue.isEmpty()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    abstract class DoParallelWrite {
        private final int maxThread;
        private final ExecutorService writerService;

        protected DoParallelWrite(int maxThread) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.maxThread = maxThread;
            this.writerService = Executors.newCachedThreadPool();
        }

        public void start() {
            try {
                doOpen();
            } catch (BatchException e) {
                log.error("W.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("W.### abnormal program end.", e);
                throw new BatchException(e);
            } finally {
                writerService.shutdown();
            }
        }

        private void doOpen() throws Exception {
            try {
                openResources();
                log.info("W.=== completed successfully.");
            } catch (InterruptedException e) {
                log.debug("W.>>> thread interrupted");
            } finally {
                log.debug("W.--- resources closed.");
            }
        }

        protected <T extends AutoCloseable, O> PmQueueWriter<O> openQueue(SinkResource<T, O> sink, T t) {
            return PmQueueWriter.of(maxThread, writerService, sink, t);
        }

        protected void doWork(
                List<Future<?>> writerStatuses,
                Iterator<I> iterator, Consumer<I> task) {
            Thread main = Thread.currentThread();
            ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
            schedule.scheduleAtFixedRate(() -> monitor(main, writerStatuses), 5, 5, TimeUnit.SECONDS);
            try {
                List<Future<?>> statuses = new LinkedList<>();
                ExecutorService taskService = Executors.newFixedThreadPool(maxThread);
                Semaphore sm = new Semaphore(maxThread);
                try {
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("W.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        Future<?> status = start(taskService, sm, () -> task.accept(i));
                        if (status == null) break;
                        statuses.add(status);
                        probeStatuses(statuses, true);
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        log.warn("W.>>> Loop interrupted, shutdown taskExecutor ...");
                    } else {
                        log.debug("W.--- All task submitted, shutdown taskExecutor ...");
                    }
                } catch (RuntimeException e) {
                    log.warn("W.>>> Internal error.");
                    throw e;
                } finally {
                    shutdown(taskService);  // abnormal end on timeout
                }
                log.debug("W.--- pending futures {}", statuses.size());
                probeStatuses(statuses, false);
                log.debug("W.--- tasks terminated, flush & close ...");
            } finally {
                log.debug("W.--- the listener's monitor will be shut down ...");
                schedule.shutdown();
            }
        }

        protected abstract void openResources() throws Exception;

        private Future<?> start(ExecutorService taskService, Semaphore sm, Runnable runnable) {
            try {
                log.trace("··· task ready to be submitted");
                sm.acquire();
                log.trace("··· task going to be submitted");
                String jobName = MDC.get(JOB_NAME);
                String stepName = MDC.get(STEP_NAME);
                return taskService.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    try {
                        log.trace("··· task entry.");
                        runnable.run();
                    } finally {
                        sm.release();
                        log.trace("··· task exit.");
                        MDC.clear();
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }

    abstract class DoParallelRaw<R> {
        private final int maxThread;
        private final Function<? super I, ? extends R> transformer;
        private final Thread main;
        private final BlockingQueue<R> queue;

        DoParallelRaw(int maxThread, Function<? super I, ? extends R> transformer) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.maxThread = maxThread;
            this.transformer = transformer;
            this.main = Thread.currentThread();
            this.queue = new ArrayBlockingQueue<>(maxThread, true);
        }

        public void start() {
            try {
                openResources();
                log.info("E.=== completed successfully.");
            } catch (BatchException e) {
                log.error("E.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("E.### abnormal program end.", e);
                throw new BatchException(e);
            }
        }

        protected abstract void openResources() throws Exception;

        protected void doWork(Iterator<I> iterator, Consumer<R> action) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            String jobName = MDC.get(JOB_NAME);
            String stepName = MDC.get(STEP_NAME);
            try {
                Future<?> future = service1.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    ExecutorService service2 = Executors.newFixedThreadPool(maxThread);
                    Semaphore sm = new Semaphore(maxThread);
                    try {
                        List<Future<?>> statuses = new LinkedList<>();
                        try {
                            while (iterator.hasNext()) {
                                I i = iterator.next();
                                if (terminateTest != null && terminateTest.test(i)) {
                                    log.warn("E.>>> Loop terminated on condition");
                                    break;
                                }
                                if (beforeAction != null) beforeAction.run();
                                try {
                                    sm.acquire();
                                    Future<?> status = service2.submit(() -> {
                                        MDC.put(JOB_NAME, jobName);
                                        MDC.put(STEP_NAME, stepName);
                                        try {
                                            queue.put(transformer.apply(i));
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                        } finally {
                                            sm.release();
                                            MDC.clear();
                                        }
                                    });
                                    statuses.add(status);
                                    probeStatuses(statuses, true);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                            if (Thread.currentThread().isInterrupted()) {
                                log.warn("E.>>> Loop interrupted, shutdown taskExecutor ...");
                            } else {
                                log.debug("E.--- All task submitted, shutdown taskExecutor ...");
                            }
                        } catch (RuntimeException e) {
                            log.warn("E.>>> Internal error.");
                            throw e;
                        } finally {
                            shutdown(service2);
                        }
                        log.debug("E.--- pending futures {}", statuses.size());
                        probeStatuses(statuses, false);
                        log.debug("E.--- tasks terminated, flush & close ...");
                    } finally {
                        awaitEmptyQueue();
                        log.debug("E.--- interrupting the write listener");
                        MDC.clear();
                        main.interrupt();
                    }
                });
                try {
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        R r = queue.take();
                        action.accept(r);
                    }
                } catch (InterruptedException e) {
                    log.debug("E.--- write listener interrupted");
//                    Thread.currentThread().interrupt();
                }
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof BatchException) {
                        BatchException batchException = (BatchException) cause;
                        cause = batchException.getCause();
                        String place = placeOf(cause.getStackTrace());
                        log.warn("E.### Error forwarded in reader: {} [{}]", cause.getMessage(), place);
                        throw batchException;
                    }
                    String place = placeOf(cause.getStackTrace());
                    log.warn("E.### Error detected in reader: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                service1.shutdown();
            }
        }

        private void awaitEmptyQueue() {
            while (!queue.isEmpty()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    abstract class DoParallelSlim {
        private final int maxThread;
        private final Consumer<? super I> action;

        DoParallelSlim(int maxThread, Consumer<? super I> action) {
            this.maxThread = maxThread;
            this.action = action;
        }

        public void start() {
            try {
                openResources();
                log.info("Z.=== completed successfully.");
            } catch (BatchException e) {
                log.error("Z.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("Z.### abnormal program end.", e);
                throw new BatchException(e);
            }
        }

        protected abstract void openResources() throws Exception;

        protected void doWork(Iterator<I> iterator) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            String jobName = MDC.get(JOB_NAME);
            String stepName = MDC.get(STEP_NAME);
            try {
                Future<?> future = service1.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    ExecutorService service2 = Executors.newFixedThreadPool(maxThread);
                    Semaphore sm = new Semaphore(maxThread);
                    List<Future<?>> statuses = new LinkedList<>();
                    try {
                        while (iterator.hasNext()) {
                            I i = iterator.next();
                            if (terminateTest != null && terminateTest.test(i)) {
                                log.warn("Z.>>> Loop terminated on condition");
                                break;
                            }
                            if (beforeAction != null) beforeAction.run();
                            try {
                                sm.acquire();
                                Future<?> status = service2.submit(() -> {
                                    MDC.put(JOB_NAME, jobName);
                                    MDC.put(STEP_NAME, stepName);
                                    try {
                                        action.accept(i);
                                    } finally {
                                        sm.release();
                                        MDC.clear();
                                    }
                                });
                                statuses.add(status);
                                probeStatuses(statuses, true);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                        if (Thread.currentThread().isInterrupted()) {
                            log.warn("Z.>>> Loop interrupted, shutdown taskExecutor ...");
                        } else {
                            log.debug("Z.--- All task submitted, shutdown taskExecutor ...");
                        }
                    } finally {
                        shutdown(service2);
                    }
                    log.debug("Z.--- pending futures {}", statuses.size());
                    probeStatuses(statuses, false);
                    log.debug("Z.--- tasks terminated, flush & close ...");
                    MDC.clear();
                });
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof BatchException) {
                        BatchException batchException = (BatchException) cause;
                        cause = batchException.getCause();
                        String place = placeOf(cause.getStackTrace());
                        log.warn("Z.### Error forwarded in reader: {} [{}]", cause.getMessage(), place);
                        throw batchException;
                    }
                    String place = placeOf(cause.getStackTrace());
                    log.warn("Z.### Error detected in reader: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                service1.shutdown();
            }
        }
    }

    abstract class DoAsyncWrite {
        private final int maxThread;
        private final ExecutorService writerService;

        protected DoAsyncWrite(int maxThread) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.maxThread = maxThread;
            this.writerService = Executors.newCachedThreadPool();
        }

        public void start() {
            try {
                doOpen();
            } catch (BatchException e) {
                log.error("w.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("w.### abnormal program end.", e);
                throw new BatchException(e);
            } finally {
                writerService.shutdown();
            }
        }

        private void doOpen() throws Exception {
            try {
                openResources();
                log.info("w.=== completed successfully.");
            } catch (InterruptedException e) {
                log.debug("w.>>> thread interrupted");
            } finally {
                log.debug("w.--- resources closed.");
            }
        }

        protected <T extends AutoCloseable, O> PmQueueWriter<O> openQueue(SinkResource<T, O> sink, T t) {
            return PmQueueWriter.of(maxThread, writerService, sink, t);
        }

        protected void doWork(
                List<Future<?>> writerStatuses,
                Iterator<I> iterator, Function<I, Future<?>> task) {
            Thread main = Thread.currentThread();
            ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
            schedule.scheduleAtFixedRate(() -> monitor(main, writerStatuses), 5, 5, TimeUnit.SECONDS);
            try {
                final Semaphore sm = new Semaphore(maxThread);
                final List<Future<?>> statuses = new LinkedList<>();
                while (iterator.hasNext()) {
                    I i = iterator.next();
                    if (terminateTest != null && terminateTest.test(i)) {
                        log.warn("w.>>> Loop terminated on condition");
                        break;
                    }
                    if (beforeAction != null) beforeAction.run();
                    runTask(task, i, sm, statuses);
                }
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("w.>>> Loop interrupted, shutdown taskExecutor ...");
                } else {
                    log.debug("w.--- All task submitted, shutdown taskExecutor ...");
                }
                log.debug("w.--- pending futures {}", statuses.size());
                probeStatuses(statuses, sm, false);
                log.debug("w.--- tasks terminated, flush & close ...");
            } catch (RuntimeException e) {
                log.warn("w.>>> Internal error.");
                throw e;
            } finally {
                log.debug("w.--- the listener's monitor will be shut down ...");
                schedule.shutdown();
            }
        }

        private void runTask(Function<I, Future<?>> task, I i, Semaphore sm, List<Future<?>> statuses) {
            try {
                while (!sm.tryAcquire(5, TimeUnit.MILLISECONDS)) {
                    probeStatuses(statuses, sm, true);
                }
                statuses.add(task.apply(i));
                probeStatuses(statuses, sm, true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void probeStatuses(List<Future<?>> statuses, Semaphore sm, boolean hot) {
            int k = 0;
            do {
                int n = iterateStatus(statuses.iterator(), sm);
                if (hot || statuses.isEmpty()) {
                    return;
                }
                sendMessage(++k, n);
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

        private int iterateStatus(Iterator<Future<?>> it, Semaphore sm) {
            int k = 0;
            while (it.hasNext()) {
                Future<?> status = it.next();
                if (status.isDone() /*|| status.isCancelled()*/) {
                    try {
                        status.get();
                    } catch (InterruptedException e) {
                        log.debug("w.>>> task was interrupted. (dead branch?)");
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        Throwable cause = e.getCause();
                        String place = placeOf(cause.getStackTrace());
                        log.warn("w.### Error detected in task execution: {} [{}]", cause.getMessage(), place);
                        throw new BatchException(cause);
                    } finally {
                        sm.release();
                    }
                    it.remove();
                } else {
                    k++;
                }
            }
            return k;
        }

        protected abstract void openResources() throws Exception;

    }

}
