package io.github.epi155.pm.batch;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 2 outputs in parallel
 *
 * @param <I>  input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 */
public interface ParallelLoop2<I, O1, O2> {
    /**
     * performs repeated action from input to output in parallel
     * <pre>Loop.from(src).into(snk1,snk2).forEachParallel(n,(i,wr1,wr2) -> { ... });</pre>
     *
     * @param maxThread maximum number of parallel processing
     * @param worker    worker who takes the input value and writes the outputs using the consumers
     */
    void forEachParallel(int maxThread, Worker2<? super I, Consumer<? super O1>, Consumer<? super O2>> worker);

    /**
     * performs repeated transformation from input to outputs in parallel
     * <p>first starts first writes
     * <pre>Loop.from(src).into(snk).forEachParallelFair(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into {@link Tuple2} outputs
     */
    void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple2<? extends O1, ? extends O2>> transformer);

    /**
     * performs repeated transformation from input to outputs in parallel
     * <p>first ends first writes
     * <pre>Loop.from(src).into(snk).forEachParallel(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into {@link Tuple2} outputs
     */
    void forEachParallel(int maxThread, Function<? super I, ? extends Tuple2<? extends O1, ? extends O2>> transformer);
}