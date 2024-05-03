package io.github.epi155.pm.batch.step;

import java.util.concurrent.Future;

/**
 * interface to provide 1 input item and 1 writer for asynchronous process
 *
 * @param <I> input type
 * @param <W> output writer
 */
public interface AsyncWorker<I, W> {
    /**
     * provide 1 input item and 1 writer for asynchronous process
     *
     * @param i input item
     * @param w writer
     * @return {@link Future} instance of asynchronous process
     */
    Future<?> apply(I i, W w);
}
