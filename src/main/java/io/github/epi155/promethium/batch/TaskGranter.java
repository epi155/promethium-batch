package io.github.epi155.promethium.batch;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public interface TaskGranter<T> {
    /**
     * Create a {@link TaskGranter} instance.
     *
     * <p>
     * The purpose is to limit the tasks that are running
     * to avoid a DOS attack and
     * maintain a minimum of functionality even under attack.
     * </p>
     * @param nmWaitTask    maximum number of pending tasks
     * @param nmRunTask     maximum number of tasks running
     * @param timeout       the maximum waiting time for the result {@link TaskGranter#compute(String, Supplier)}
     * @param unit          the time unit of the timeout argument
     * @return              instance of {@link TaskGranter}
     */
    static <T> TaskGranter<T> getInstance(
            int nmWaitTask,
            int nmRunTask,
            long timeout,
            TimeUnit unit
    ) {
        return new TaskGranterImpl<>(nmWaitTask, nmRunTask, timeout, unit);
    }


    /**
     * Performs the action that generates the result.
     *
     * <p>
     *     The calculation is performed on a parallel task.
     *     The number of tasks running simultaneously is limited.
     *     The method waits for execution to finish.
     * </p>
     *
     * @param jobId     task identifier
     * @param action    task action
     * @return          task result, i.e. <code>action.get()</code>
     * @throws InterruptedException     the task was interrupted
     * @throws TimeoutException         the task has exceeded the time limit for execution
     * @throws ExecutionException       error in the execution of the task
     *
     * @throws IllegalStateException    the execution queue is full or the task was canceled
     */
    T compute(String jobId, Supplier<T> action) throws InterruptedException, TimeoutException, ExecutionException;

    /**
     * Stops graceful the {@link ExecutorService} who manage the queue.
     * <p>
     *     No new tasks can be performed
     * </p>
     * <p>
     *     Waits until all submitted tasks are completed
     * </p>
     */
    void shutdown();

}
