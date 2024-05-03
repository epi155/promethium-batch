package io.github.epi155.pm.batch.step;

import java.util.concurrent.Future;

/**
 * interface to provide 1 input item and 2 writer for asynchronous process
 *
 * @param <I>  input type
 * @param <W1> 1st output writer
 * @param <W2> 2nd output writer
 */
public interface AsyncWorker2<I, W1, W2> {
    /**
     * provide 1 input item and 2 writer for asynchronous process
     *
     * @param i  input item
     * @param w1 1st writer
     * @param w2 2nd writer
     * @return {@link Future} instance of asynchronous process
     */
    Future<?> apply(I i, W1 w1, W2 w2);
}
