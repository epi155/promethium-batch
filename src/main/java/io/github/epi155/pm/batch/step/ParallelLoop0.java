package io.github.epi155.pm.batch.step;

import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input, no output in parallel
 *
 * @param <I> input type
 */
public interface ParallelLoop0<I> {
    /**
     * Performs the specified action for each item taken from the source until all items have been processed or the
     * action throws an exception. Up to {@code maxThread} elements are processed in parallel
     *
     * @param maxThread maximum number of parallel processing
     * @param action    The action to be performed for each element
     */
    void forEachParallel(int maxThread, Consumer<? super I> action);

    /**
     * performs repeated transformation from input using asynchronous task
     *
     * @param maxThread        maximum number of parallel processing
     * @param asyncTransformer asynchronous function that transforms input into output
     */
    void forEachAsync(int maxThread, Function<? super I, ? extends Future<Void>> asyncTransformer);
}
