package io.github.epi155.pm.batch.step;

import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 8 outputs in parallel
 *
 * @param <I>  input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 * @param <O3> 3rd output type
 * @param <O4> 4th output type
 * @param <O5> 5th output type
 * @param <O6> 6th output type
 * @param <O7> 7th output type
 * @param <O8> 8th output type
 */
public interface ParallelLoop8<I, O1, O2, O3, O4, O5, O6, O7, O8> {
    /**
     * performs repeated action from input to output in parallel
     * <pre>Batch.from(src)
     *      .into(snk1,snk2,snk3,snk4,snk5,snk6,snk7,snk8)
     *      .forEachParallel(n,(i,wr1,wr2,wr3,wr4,wr5,wr6,wr7,wr8) -> { ... });</pre>
     *
     * @param maxThread maximum number of parallel processing
     * @param worker    worker who takes the input value and writes the outputs using the consumers
     */
    void forEachParallel(int maxThread, Worker8<? super I,
            Consumer<? super O1>,
            Consumer<? super O2>,
            Consumer<? super O3>,
            Consumer<? super O4>,
            Consumer<? super O5>,
            Consumer<? super O6>,
            Consumer<? super O7>,
            Consumer<? super O8>> worker);

    /**
     * performs repeated transformation from input to outputs in parallel
     * <p>first starts first writes
     * <pre>Batch.from(src).into(snk).forEachParallelFair(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into {@link Tuple8} outputs
     */
    void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple8<
            ? extends O1,
            ? extends O2,
            ? extends O3,
            ? extends O4,
            ? extends O5,
            ? extends O6,
            ? extends O7,
            ? extends O8>> transformer);

    /**
     * performs repeated transformation from input to outputs in parallel
     * <p>first ends first writes
     * <pre>Batch.from(src).into(snk).forEachParallel(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into {@link Tuple8} outputs
     */
    void forEachParallel(int maxThread, Function<? super I, ? extends Tuple8<
            ? extends O1,
            ? extends O2,
            ? extends O3,
            ? extends O4,
            ? extends O5,
            ? extends O6,
            ? extends O7,
            ? extends O8>> transformer);

    /**
     * performs repeated transformation from input to output using asynchronous task
     *
     * @param maxThread        maximum number of parallel processing
     * @param asyncTransformer asynchronous function that transforms input into {@link Tuple8} outputs
     */
    void forEachAsync(int maxThread,
                      Function<? super I,
                              ? extends Future<? extends Tuple8<
                                      ? extends O1,
                                      ? extends O2,
                                      ? extends O3,
                                      ? extends O4,
                                      ? extends O5,
                                      ? extends O6,
                                      ? extends O7,
                                      ? extends O8>>> asyncTransformer);

    /**
     * performs repeated action from input to output using asynchronous worker
     *
     * @param maxThread   maximum number of parallel processing
     * @param asyncWorker asyncWorker who takes the input value and writes the output using the consumer
     */
    void forEachAsync(int maxThread, AsyncWorker8<? super I,
            Consumer<? super O1>,
            Consumer<? super O2>,
            Consumer<? super O3>,
            Consumer<? super O4>,
            Consumer<? super O5>,
            Consumer<? super O6>,
            Consumer<? super O7>,
            Consumer<? super O8>> asyncWorker);
}
