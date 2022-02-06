package io.github.epi155.promethium.batch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Slf4j
class BatchQueueSimple<T> implements BatchQueue<T> {

    protected static class BatchTarget<T> {

        public final String jobId;
        public final Future<T> promise;

        public BatchTarget(String jobId, Future<T> promise) {
            this.jobId = jobId;
            this.promise = promise;
        }
    }

    private final Semaphore semaphore;
    private final Phaser phaser = new Phaser(1);
    private final CountDownLatch countDown = new CountDownLatch(1); // targetListener
    private final BiConsumer<String, T> onSuccess;
    private final BiConsumer<String, Throwable> onError;
    protected final BlockingQueue<BatchTarget<T>> targetQueue;
    private final ExecutorService executor;
    private final ExecutorService execTarget;

    BatchQueueSimple(
            int nmExec,
            BiConsumer<String,T> onSuccess,
            BiConsumer<String, Throwable> onError) {
        this.targetQueue = new ArrayBlockingQueue<>(nmExec);
        this.semaphore = new Semaphore(nmExec);
        this.onSuccess = onSuccess;
        this.onError = onError;
        this.execTarget = Executors.newSingleThreadExecutor();
        this.executor = Executors.newFixedThreadPool(nmExec);

        execTarget.execute(this::targetListener);
    }
    BatchQueueSimple(
            int nmExec,
            ThreadFactory threadFactory,
            BiConsumer<String, T> onSuccess,
            BiConsumer<String, Throwable> onError) {
        this.targetQueue = new ArrayBlockingQueue<>(nmExec);
        this.semaphore = new Semaphore(nmExec);
        this.onSuccess = onSuccess;
        this.onError = onError;
        this.execTarget = Executors.newSingleThreadExecutor(threadFactory);
        this.executor = Executors.newFixedThreadPool(nmExec, threadFactory);

        execTarget.execute(this::targetListener);
    }

    private void targetListener() {
        log.info("Target listener started ...");
        registerListener();
        BatchTarget<T> info;
        try {
            for(int k=Integer.MIN_VALUE; k<Integer.MAX_VALUE; k++) {	// graceful ended by executor shutdown
                log.trace("T[---] job next hot {} ...", targetQueue.size());
                info = targetQueue.take();	// wait for data on queue
                finalAction(info);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    protected void registerListener() {
        countDown.countDown();
    }

    private void finalAction(BatchTarget<T> info) throws InterruptedException {
        log.trace("T[#--] job {} took {}, wait termination ...", info.jobId, targetQueue.size());
        try {
            T result = info.promise.get();  // wait end of run
            log.trace("T[##-] job {} returning", info.jobId);
            onSuccess.accept(info.jobId, result);
            log.trace("T[###] job {} returned", info.jobId);
        } catch (ExecutionException e) {
            log.trace("T[##-] job {} crashing", info.jobId);
            Throwable cause = e.getCause();
            onError.accept(info.jobId, (cause==null) ? e : cause);
            log.warn("T[###] job {} crashed", info.jobId);
        } finally {
            phaser.arriveAndDeregister();
        }
    }

    @Override
    public void submit(String jobId, Supplier<T> action) throws InterruptedException {
        countDown.await();
        log.trace("S[--] job {} received ...", jobId);
        registerTask();
        Future<T> promise = executorSubmit(jobId, action);
        log.trace("S[#-] job {} submitting ...", jobId);
        targetQueue.put(new BatchTarget<>(jobId, promise));  // wait for space available
        log.trace("S[##] job {} submitted", jobId);
    }

    protected final void registerTask() {
        phaser.register();
    }
    protected final Future<T> executorSubmit(String jobId, Supplier<T> action) {
        return executor.submit(this.new BatchTask(jobId, action));
    }

    @Override
    public void shutdown() {
        log.info("H[-] waiting all job completion ...");
        phaser.arriveAndAwaitAdvance();
        log.info("H[#] all job completed.");

        if (! executor.isShutdown()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        if (! execTarget.isShutdown()) {
            execTarget.shutdown();
        }
    }

    private class BatchTask implements Callable<T> {
        private final String jobId;
        private final Supplier<T> action;

        public BatchTask(String jobId, Supplier<T> action) {
            this.jobId = jobId;
            this.action = action;
            log.trace("R[---] job {} ready.", jobId);
        }

        @Override
        public T call() {
            log.trace("R[#--] job {} beware ...", jobId);
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.trace("R[##-] job {} go ...", jobId);
            try {
                return action.get();
            } finally {
                semaphore.release();
                log.trace("R[###] job {} completed", jobId);
            }
        }
    }
}
