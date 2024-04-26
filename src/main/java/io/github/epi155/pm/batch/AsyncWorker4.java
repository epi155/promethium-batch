package io.github.epi155.pm.batch;

import java.util.concurrent.Future;

/**
 * interface to provide 1 input item and 4 writer for asynchronous process
 *
 * @param <I>  input type
 * @param <W1> 1st output writer
 * @param <W2> 2nd output writer
 * @param <W3> 3rd output writer
 * @param <W4> 4th output writer
 */
public interface AsyncWorker4<I, W1, W2, W3, W4> {
    /**
     * provide 1 input item and 4 writer for asynchronous process
     *
     * @param i  input item
     * @param w1 1st writer
     * @param w2 2nd writer
     * @param w3 3rd writer
     * @param w4 4th writer
     * @return {@link Future} instance of asynchronous process
     */
    Future<?> apply(I i, W1 w1, W2 w2, W3 w3, W4 w4);
}
