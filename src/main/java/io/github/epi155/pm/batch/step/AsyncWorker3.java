package io.github.epi155.pm.batch.step;

import java.util.concurrent.Future;

/**
 * interface to provide 1 input item and 3 writer for asynchronous process
 *
 * @param <I>  input type
 * @param <W1> 1st output writer
 * @param <W2> 2nd output writer
 * @param <W3> 3rd output writer
 */
public interface AsyncWorker3<I, W1, W2, W3> {
    /**
     * provide 1 input item and 3 writer for asynchronous process
     *
     * @param i  input item
     * @param w1 1st writer
     * @param w2 2nd writer
     * @param w3 3rd writer
     * @return {@link Future} instance of asynchronous process
     */
    Future<?> apply(I i, W1 w1, W2 w2, W3 w3);
}
