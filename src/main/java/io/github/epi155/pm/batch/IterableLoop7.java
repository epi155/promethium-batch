package io.github.epi155.pm.batch;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 7 outputs
 *
 * @param <I>  input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 * @param <O3> 3rd output type
 * @param <O4> 4th output type
 * @param <O5> 5th output type
 * @param <O6> 6th output type
 * @param <O7> 7th output type
 */
public interface IterableLoop7<I, O1, O2, O3, O4, O5, O6, O7> extends ParallelLoop7<I, O1, O2, O3, O4, O5, O6, O7> {
    /**
     * performs repeated transformation from input to outputs sequentially
     *
     * @param transformer function that transforms input into a {@link Tuple7} outputs
     */
    void forEach(Function<? super I, ? extends Tuple7<
            ? extends O1,
            ? extends O2,
            ? extends O3,
            ? extends O4,
            ? extends O5,
            ? extends O6,
            ? extends O7>> transformer);

    /**
     * performs repeated action from input to outputs sequentially
     *
     * @param worker worker who takes the input value and writes the outputs using the consumers
     */
    void forEach(Worker7<? super I,
            Consumer<? super O1>,
            Consumer<? super O2>,
            Consumer<? super O3>,
            Consumer<? super O4>,
            Consumer<? super O5>,
            Consumer<? super O6>,
            Consumer<? super O7>> worker);

    /**
     * sets the shutdown timeout for parallel processing
     *
     * @param time time amount
     * @param unit time unit
     * @return instance of {@link ParallelLoop7} for run parallel processing
     */
    ParallelLoop7<I, O1, O2, O3, O4, O5, O6, O7> shutdownTimeout(long time, TimeUnit unit);
}
