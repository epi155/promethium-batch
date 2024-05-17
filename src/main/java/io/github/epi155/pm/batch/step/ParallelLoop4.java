package io.github.epi155.pm.batch.step;

import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 4 outputs in parallel
 *
 * @param <I>  input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 * @param <O3> 3rd output type
 * @param <O4> 4th output type
 */
public interface ParallelLoop4<I, O1, O2, O3, O4> {
    /**
     * performs repeated action from input to output in parallel
     * <pre>Pgm.from(src)
     *      .into(snk1,snk2,snk3,snk4)
     *      .forEachParallel(n,(i,wr1,wr2,wr3,wr4) -> { ... });</pre>
     *
     * @param maxThread maximum number of parallel processing
     * @param worker    worker who takes the input value and writes the outputs using the consumers
     */
    void forEachParallel(int maxThread, Worker4<? super I,
            Consumer<? super O1>,
            Consumer<? super O2>,
            Consumer<? super O3>,
            Consumer<? super O4>> worker);

    /**
     * performs repeated transformation from input to outputs in parallel
     * <p>first starts first writes
     * <pre>Pgm.from(src).into(snk).forEachParallelFair(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into {@link Tuple4} outputs
     */
    void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple4<
            ? extends O1,
            ? extends O2,
            ? extends O3,
            ? extends O4>> transformer);

    /**
     * performs repeated transformation from input to outputs in parallel
     * <p>first ends first writes
     * <pre>Pgm.from(src).into(snk).forEachParallelRaw(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into {@link Tuple4} outputs
     */
    void forEachParallel(int maxThread, Function<? super I, ? extends Tuple4<
            ? extends O1,
            ? extends O2,
            ? extends O3,
            ? extends O4>> transformer);

    /**
     * performs repeated transformation from input to output using asynchronous task
     *
     * @param maxThread        maximum number of parallel processing
     * @param asyncTransformer asynchronous function that transforms input into {@link Tuple4} outputs
     */
    void forEachAsync(int maxThread,
                      Function<? super I,
                              ? extends Future<? extends Tuple4<
                                      ? extends O1,
                                      ? extends O2,
                                      ? extends O3,
                                      ? extends O4>>> asyncTransformer);

    /**
     * performs repeated action from input to output using asynchronous worker
     *
     * @param maxThread   maximum number of parallel processing
     * @param asyncWorker asyncWorker who takes the input value and writes the output using the consumer
     */
    void forEachAsync(int maxThread, AsyncWorker4<? super I,
            Consumer<? super O1>,
            Consumer<? super O2>,
            Consumer<? super O3>,
            Consumer<? super O4>> asyncWorker);
}
