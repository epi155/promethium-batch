package io.github.epi155.promethium.batch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface BatchQueue<T> {

    /**
     * Creates a batch queue that can execute n tasks in parallel.
     * <p>
     * if a task is submitted when n tasks are already running, it waits for a task to finish executing
     * </p>
     * @param nmExec        max number of parallel task
     * @param onSuccess     action when task terminate successful
     * @param onError       action when the task ends in error
     * @param <U>           data type returned by the task
     * @return              instance of {@link BatchQueue}
     */
    static <U> BatchQueue<U> getInstance(
            int nmExec,
            BiConsumer<String,U> onSuccess,
            BiConsumer<String, Throwable> onError
    ) {
        return new BatchQueueSimple<>(nmExec, onSuccess, onError);
    }

    /**
     * Creates a batch queue that can execute n tasks in parallel.
     * <p>
     * if a task is submitted when n tasks are already running, it waits for a task to finish executing
     * </p>
     * @param nmExec        max number of parallel task
     * @param threadFactory custom thread factory
     * @param onSuccess     action when task terminate successful
     * @param onError       action when the task ends in error
     * @param <U>           data type returned by the task
     * @return              instance of {@link BatchQueue}
     */
    static <U> BatchQueue<U> getInstance(
            int nmExec,
            ThreadFactory threadFactory,
            BiConsumer<String,U> onSuccess,
            BiConsumer<String, Throwable> onError
    ) {
        return new BatchQueueSimple<>(nmExec, threadFactory, onSuccess, onError);
    }

    /**
     * Creates a batch queue that can execute n tasks in parallel and hold m tasks waiting.
     * <p>
     * if a task is submitted when n tasks are already running, and m waiting,
     *  {@link IllegalStateException} is throws
     * </p>
     * @param nmExec        max number of parallel task
     * @param nmWait        max number of tasks waiting
     * @param onSuccess     action when task terminate successful
     * @param onError       action when the task ends in error
     * @param <U>           data type returned by the task
     * @return              instance of {@link BatchQueue}
     */
    static <U> BatchQueue<U> getInstance(
            int nmExec,
            int nmWait,
            BiConsumer<String,U> onSuccess,
            BiConsumer<String, Throwable> onError
    ) {
        return new BatchQueueDouble<>(nmExec, nmWait, onSuccess, onError);
    }

    /**
     * Creates a batch queue that can execute n tasks in parallel and hold m tasks waiting.
     * <p>
     * if a task is submitted when n tasks are already running, and m waiting,
     *  {@link IllegalStateException} is throws
     * </p>
     * @param nmExec        max number of parallel task
     * @param nmWait        max number of tasks waiting
     * @param threadFactory custom thread factory
     * @param onSuccess     action when task terminate successful
     * @param onError       action when the task ends in error
     * @param <U>           data type returned by the task
     * @return              instance of {@link BatchQueue}
     */
    static <U> BatchQueue<U> getInstance(
            int nmExec,
            int nmWait,
            ThreadFactory threadFactory,
            BiConsumer<String,U> onSuccess,
            BiConsumer<String, Throwable> onError
    ) {
        return new BatchQueueDouble<>(nmExec, nmWait, threadFactory, onSuccess, onError);
    }

    /**
     * Submit a new task
     * @param jobId         task identifier
     * @param action        task action
     * @throws InterruptedException      if any threads are interrupted while waiting
     */
    void submit(String jobId, Supplier<T> action) throws InterruptedException;

    /**
     * Stops graceful the {@link ExecutorService} who manage the queue.
     * <p>
     *     No new tasks can be submitted
     * </p>
     * <p>
     *     Waits until all submitted tasks are completed
     * </p>
     */
    void shutdown();
}
