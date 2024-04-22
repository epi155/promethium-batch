package io.github.epi155.pm.batch;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 2 outputs
 *
 * @param <I>  input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 */
public interface IterableLoop2<I, O1, O2> extends ParallelLoop2<I, O1, O2> {
    /**
     * performs repeated transformation from input to outputs sequentially
     *
     * @param transformer function that transforms input into a {@link Tuple2} outputs
     */
    void forEach(Function<? super I, ? extends Tuple2<? extends O1, ? extends O2>> transformer);

    /**
     * performs repeated action from input to outputs sequentially
     *
     * @param worker worker who takes the input value and writes the outputs using the consumers
     */
    void forEach(Worker2<? super I, Consumer<? super O1>, Consumer<? super O2>> worker);

    /**
     * sets the shutdown timeout for parallel processing
     *
     * @param time time amount
     * @param unit time unit
     * @return instance of {@link ParallelLoop2} for run parallel processing
     */
    ParallelLoop2<I, O1, O2> shutdownTimeout(long time, TimeUnit unit);
}
