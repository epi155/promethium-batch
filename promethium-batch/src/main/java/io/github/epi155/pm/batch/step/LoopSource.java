package io.github.epi155.pm.batch.step;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * terminable interface to manage the elements of a single input resource.
 * <p>
 * it is possible to process the input immediately or to define one or more
 * output resources where the input processing can be sent
 *
 * @param <I> input type
 */
public interface LoopSource<I> extends LoopSourceLayer<I> {
    /**
     * Sets a condition to interrupt the processing cycle
     * <p>even if the value being processed is passed as an argument,
     * the condition may depend on an external parameter,
     * for example the reaching of a maximum execution time
     *
     * @param test test condition
     * @return {@code true} to stop the processing loop, {@code false} to continue as usual
     */
    LoopSourceLayer<I> terminate(Predicate<? super I> test);

    /**
     * transforms the item read from the source before offering it to the main processing loop.
     *
     * @param map transformation function
     * @param <J> new item type
     * @return new instance of {@link LoopSource}
     */
    <J> LoopSource<J> map(Function<? super I, ? extends J> map);
}
