package io.github.epi155.pm.batch;

/**
 * interface to provide 1 input item and 1 writer
 *
 * @param <I> input type
 * @param <W> output writer
 */
public interface Worker<I, W> {
    /**
     * provide 1 input item and 1 writer
     *
     * @param i input item
     * @param w writer
     */
    void process(I i, W w);
}
