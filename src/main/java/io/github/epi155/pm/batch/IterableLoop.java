package io.github.epi155.pm.batch;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 1 output
 *
 * @param <I> input type
 * @param <O> output type
 */
public interface IterableLoop<I, O> extends ParallelLoop<I, O> {
    /**
     * performs repeated transformation from input to output sequentially
     *
     * @param transformer function that transforms input into output
     */
    void forEach(Function<? super I, ? extends O> transformer);

    /**
     * performs repeated action from input to output sequentially
     *
     * @param worker worker who takes the input value and writes the output using the consumer
     */
    void forEach(Worker<? super I, Consumer<? super O>> worker);

    /**
     * sets the shutdown timeout for parallel processing
     *
     * @param time time amount
     * @param unit time unit
     * @return instance of {@link ParallelLoop} for run parallel processing
     */
    ParallelLoop<I, O> shutdownTimeout(long time, TimeUnit unit);
}
