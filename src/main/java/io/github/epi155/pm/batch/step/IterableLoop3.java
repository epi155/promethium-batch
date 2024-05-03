package io.github.epi155.pm.batch.step;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 3 outputs
 *
 * @param <I>  input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 * @param <O3> 3rd output type
 */
public interface IterableLoop3<I, O1, O2, O3> extends ParallelLoop3<I, O1, O2, O3> {
    /**
     * performs repeated transformation from input to outputs sequentially
     *
     * @param transformer function that transforms input into a {@link Tuple3} outputs
     */
    void forEach(Function<? super I, ? extends Tuple3<? extends O1, ? extends O2, ? extends O3>> transformer);

    /**
     * performs repeated action from input to outputs sequentially
     *
     * @param worker worker who takes the input value and writes the outputs using the consumers
     */
    void forEach(Worker3<? super I,
            Consumer<? super O1>,
            Consumer<? super O2>,
            Consumer<? super O3>> worker);

    /**
     * sets the shutdown timeout for parallel processing
     *
     * @param time time amount
     * @param unit time unit
     * @return instance of {@link ParallelLoop3} for run parallel processing
     */
    ParallelLoop3<I, O1, O2, O3> shutdownTimeout(long time, TimeUnit unit);

}
