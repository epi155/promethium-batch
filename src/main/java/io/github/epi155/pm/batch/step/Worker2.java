package io.github.epi155.pm.batch.step;

/**
 * interface to provide 1 input item and 2 writers
 *
 * @param <I>  input type
 * @param <W1> 1st output writer
 * @param <W2> 2nd output writer
 */
public interface Worker2<I, W1, W2> {
    /**
     * provide 1 input item and 2 writers
     *
     * @param i  input item
     * @param w1 1st writer
     * @param w2 2nd writer
     */
    void process(I i, W1 w1, W2 w2);
}
