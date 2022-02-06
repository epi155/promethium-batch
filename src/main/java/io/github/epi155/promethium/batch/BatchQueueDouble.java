package io.github.epi155.promethium.batch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Slf4j
class BatchQueueDouble<T> extends BatchQueueSimple<T> {

    private final CountDownLatch countDown = new CountDownLatch(2); // sourceListener & targetListener
    private final BlockingQueue<BatchSource<T>> sourceQueue;
    private final ExecutorService execSource;

    BatchQueueDouble(
            int nmExec,
            int nmWait,
            BiConsumer<String,T> onSuccess,
            BiConsumer<String, Throwable> onError) {
        super(nmExec, onSuccess, onError);
        this.sourceQueue = new ArrayBlockingQueue<>(nmWait);
        this.execSource = Executors.newSingleThreadExecutor();

        execSource.execute(this::sourceListener);
    }
    BatchQueueDouble(
            int nmExec,
            int nmWait,
            ThreadFactory threadFactory,
            BiConsumer<String, T> onSuccess,
            BiConsumer<String, Throwable> onError) {
        super(nmExec, threadFactory, onSuccess, onError);
        this.sourceQueue = new ArrayBlockingQueue<>(nmWait);
        this.execSource = Executors.newSingleThreadExecutor(threadFactory);

        execSource.execute(this::sourceListener);
    }

    @Override
    protected void registerListener() {
        countDown.countDown();
    }

    private void sourceListener() {
        log.info("Source listener started ...");
        registerListener();
        try {
            for(int k=Integer.MIN_VALUE; k<Integer.MAX_VALUE; k++) {    // graceful ended by executor shutdown
                log.trace("W[---] job next cold {}/{} ...", sourceQueue.size(), targetQueue.size());
                BatchSource<T> source = sourceQueue.take();   // wait for data
                log.trace("W[#--] job {} took {}/{}", source.jobId, sourceQueue.size(), targetQueue.size());
                Future<T> promise = executorSubmit(source.jobId, source.action);
                log.trace("W[##-] job {} moving {}/{} ...", source.jobId, sourceQueue.size(), targetQueue.size());
                targetQueue.put(new BatchTarget<>(source.jobId, promise));  // wait for space available
                log.trace("W[###] job {} moved {}/{}", source.jobId, sourceQueue.size(), targetQueue.size());
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }


    @Override
    public void submit(String jobId, Supplier<T> action) throws InterruptedException {
        countDown.await();
        sourceQueue.add(new BatchSource<>(jobId, action));  // IllegalStateException
//        sourceQueue.offer(new BatchSource<>(jobId, action));  // false (when full)
//        sourceQueue.put(new BatchSource<>(jobId, action));  // wait (when full)
        registerTask();
        log.trace("Job {} queued (get) {}/{}", jobId, sourceQueue.size(), targetQueue.size());
    }

    @Override
    public void shutdown() {
        super.shutdown();
        if (! execSource.isShutdown()) {
            execSource.shutdown();
        }
    }

    private static class BatchSource<U> {
        private final String jobId;
        private final Supplier<U> action;

        public BatchSource(String jobId, Supplier<U> action) {
            this.jobId = jobId;
            this.action = action;
        }
    }

}
