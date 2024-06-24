package io.github.epi155.pm.batch.step;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * interface to handle a repeated transformation from 1 input, no output
 *
 * @param <I> input type
 */
public interface LoopSourceZro<I> extends ParallelLoopZro<I> {
    /**
     * Performs the specified action for each item taken from the source until all items have been processed or the
     * action throws an exception.
     *
     * @param action The action to be performed for each element
     */

    void forEach(Consumer<? super I> action);
    /**
     * sets the shutdown timeout for parallel processing
     *
     * @param time time amount
     * @param unit time unit
     * @return instance of {@link ParallelLoopZro} for run parallel processing
     */
    ParallelLoopZro<I> shutdownTimeout(long time, TimeUnit unit);
}
