package io.github.epi155.promethium.batch;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.*;
import java.util.function.Supplier;

@Slf4j
public class TaskGranterImpl implements TaskGranter {
    private final BlockingQueue<LimitTask<?>> taskQueue;
    private final CountDownLatch countDown = new CountDownLatch(1); // listener
    private final Semaphore semaphore;
    private final Phaser phaser = new Phaser(1);
    private final ExecutorService listener;
    private final long timeout;
    private final TimeUnit unit;
    private final ExecutorService executor;
    private final int threshold;

    TaskGranterImpl(
            int nmWaitTask,
            int nmRunTask,
            long timeout,
            TimeUnit unit
    ) {
        this.threshold = nmWaitTask/2;
        this.timeout = timeout;
        this.unit = unit;
        this.semaphore = new Semaphore(nmRunTask);
        this.taskQueue = new ArrayBlockingQueue<>(nmWaitTask);
        this.listener = Executors.newSingleThreadExecutor();
        this.executor = Executors.newFixedThreadPool(nmRunTask);

        listener.execute(this::listener);
    }

    private void listener() {
        log.info("Listener started ...");
        countDown.countDown();
        try {
            while (! phaser.isTerminated()) {    // graceful ended by executor shutdown
                log.trace("job waiting {}- ...", taskQueue.size());
                val source = taskQueue.take();   // wait for data
                log.trace("taken {} ({}) ...", source.jobId, taskQueue.size());
                start(source);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private void start(LimitTask<?> task) {
        if (task.isCancelled()) {
            log.trace("Expired {}", task.jobId);
            return;
        }
        try {
            log.trace("{} ready ...", task.jobId);
            semaphore.acquire();    // wait for a slot to be available and acquire one
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        task.start();
    }

    @Override
    public <T> T compute(String jobId, Supplier<T> action) throws InterruptedException, TimeoutException, ExecutionException {
        countDown.await();      // wait for the listener to start
        phaser.register();
        val promise = this.new LimitTask<>(jobId, action);
        try {
            log.trace("queue {} ({}) ...", jobId, taskQueue.size());
            taskQueue.add(promise);   // IllegalStateException
            log.trace("{} awaiting ...", jobId);
            return promise.get(timeout, unit);
        } catch (TimeoutException e) {
            if (promise.cancel(true)) {
                log.trace("{} cancelling ...", jobId);
            } else {
                log.trace("{} already done", jobId);
            }
            if (taskQueue.size() > threshold) {
                // if there has been a timeout, most of the items in the queue will time out
                taskQueue.forEach(it -> it.cancel(false));
                taskQueue.clear();
            }
            throw e;
        } finally {
            phaser.arriveAndDeregister();
        }
    }

    public void shutdown() {
        log.info("waiting all job completion ...");
        phaser.arriveAndAwaitAdvance();
        log.info("all job completed.");

        if (!executor.isShutdown()) {
            executor.shutdown();
        }
        if (!listener.isShutdown()) {
            listener.shutdown();
        }
    }

    private class LimitTask<U> {
        private final Supplier<U> action;
        private final String jobId;
        private final FutureTask<U> delegate;

        public LimitTask(String jobId, Supplier<U> action) {
            this.jobId = jobId;
            this.action = action;
            this.delegate = new FutureTask<>(this::call);
        }

        public U call() {
            try {
                log.trace("{} go ...", jobId);
                val value = action.get();   // finally, calls the action
                log.trace("{} completed.", jobId);
                return value;
            } catch (Exception e) { // InterruptedException
                log.trace("{} exit {}.", jobId, e.toString());
                throw e;
            } finally {
                semaphore.release();    // release the execution slot
                log.trace("{} reset.", jobId);
            }
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            return delegate.cancel(mayInterruptIfRunning);
        }

        public boolean isCancelled() {
            return delegate.isCancelled();
        }

        public U get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return delegate.get(timeout, unit);
        }

        public void start() {
            log.trace("{} beware ...", jobId);
            executor.execute(delegate);
        }
    }
}
