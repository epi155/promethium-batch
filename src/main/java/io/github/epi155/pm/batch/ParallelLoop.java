package io.github.epi155.pm.batch;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 1 output in parallel
 *
 * @param <I> input type
 * @param <O> output type
 */
public interface ParallelLoop<I, O> {
    /**
     * performs repeated action from input to output in parallel
     * <pre>Loop.from(src).into(snk).forEachParallel(n,(i,wr) -> { ... });</pre>
     *
     * @param maxThread maximum number of parallel processing
     * @param worker    worker who takes the input value and writes the output using the consumer
     */
    void forEachParallel(int maxThread, Worker<? super I, Consumer<? super O>> worker);

    /**
     * performs repeated transformation from input to output in parallel
     * <p>first starts first writes
     * <pre>Loop.from(src).into(snk).forEachParallelFair(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into output
     */
    void forEachParallelFair(int maxThread, Function<? super I, ? extends O> transformer);

    /**
     * performs repeated transformation from input to output in parallel
     * <p>first ends first writes
     * <pre>Loop.from(src).into(snk).forEachParallel(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into output
     */
    void forEachParallel(int maxThread, Function<? super I, ? extends O> transformer);
}
