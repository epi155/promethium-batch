package io.github.epi155.pm.batch.step;

/**
 * interface to manage the elements of a single input resource.
 * <p>
 * it is possible to process the input immediately or to define one or more
 * output resources where the input processing can be sent
 *
 * @param <I> input type
 */
public interface LoopSourceLayer<I> extends LoopSourceStd<I> {
    /**
     * performs the indicated action after reading each element read before main processing
     *
     * @param action action to perform
     * @return new instance of {@link LoopSourceStd}
     */
    LoopSourceStd<I> before(Runnable action);
}
