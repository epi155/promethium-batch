package io.github.epi155.pm.batch;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 3 outputs in parallel
 *
 * @param <I>  input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 * @param <O3> 3rd output type
 */
public interface ParallelLoop3<I, O1, O2, O3> {
    /**
     * performs repeated action from input to output in parallel
     * <pre>Loop.from(src).into(snk1,snk2,snk3).forEachParallel(n,(i,wr1,wr2,wr3) -> { ... });</pre>
     *
     * @param maxThread maximum number of parallel processing
     * @param worker    worker who takes the input value and writes the outputs using the consumers
     */
    void forEachParallel(int maxThread, Worker3<? super I,
            Consumer<? super O1>,
            Consumer<? super O2>,
            Consumer<? super O3>> worker);

    /**
     * performs repeated transformation from input to outputs in parallel
     * <p>first starts first writes
     * <pre>Loop.from(src).into(snk).forEachParallelFair(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into {@link Tuple3} outputs
     */
    void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple3<? extends O1, ? extends O2, ? extends O3>> transformer);

    /**
     * performs repeated transformation from input to outputs in parallel
     * <p>first ends first writes
     * <pre>Loop.from(src).into(snk).forEachParallel(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into {@link Tuple3} outputs
     */
    void forEachParallel(int maxThread, Function<? super I, ? extends Tuple3<? extends O1, ? extends O2, ? extends O3>> transformer);
}
