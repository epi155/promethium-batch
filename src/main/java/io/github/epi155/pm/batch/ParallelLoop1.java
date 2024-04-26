package io.github.epi155.pm.batch;

import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * interface to handle a repeated transformation from 1 input to 1 output in parallel
 *
 * @param <I> input type
 * @param <O> output type
 */
public interface ParallelLoop1<I, O> {
    /**
     * performs repeated action from input to output in parallel
     * <pre>Batch.from(src).into(snk).forEachParallel(n,(i,wr) -> { ... });</pre>
     *
     * @param maxThread maximum number of parallel processing
     * @param worker    worker who takes the input value and writes the output using the consumer
     */
    void forEachParallel(int maxThread, Worker<? super I, Consumer<? super O>> worker);

    /**
     * performs repeated transformation from input to output in parallel
     * <p>first starts first writes
     * <pre>Batch.from(src).into(snk).forEachParallelFair(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into output
     */
    void forEachParallelFair(int maxThread, Function<? super I, ? extends O> transformer);

    /**
     * performs repeated transformation from input to output in parallel
     * <p>first ends first writes
     * <pre>Batch.from(src).into(snk).forEachParallel(n,i -> { ... });</pre>
     *
     * @param maxThread   maximum number of parallel processing
     * @param transformer function that transforms input into output
     */
    void forEachParallel(int maxThread, Function<? super I, ? extends O> transformer);

    /**
     * performs repeated transformation from input to output using asynchronous task
     *
     * @param maxThread        maximum number of parallel processing
     * @param asyncTransformer asynchronous function that transforms input into output
     */
    void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends O>> asyncTransformer);

    /**
     * performs repeated action from input to output using asynchronous worker
     *
     * @param maxThread   maximum number of parallel processing
     * @param asyncWorker asyncWorker who takes the input value and writes the output using the consumer
     */
    void forEachAsync(int maxThread, AsyncWorker<? super I, Consumer<? super O>> asyncWorker);
}
