package io.github.epi155.pm.batch;

/**
 * interface to provide 1 input item and 3 writers
 *
 * @param <I>  input type
 * @param <W1> 1st output writer
 * @param <W2> 2nd output writer
 * @param <W3> 3rd output writer
 */
public interface Worker3<I, W1, W2, W3> {
    /**
     * provide 1 input item and 3 writers
     *
     * @param i  input item
     * @param w1 1st writer
     * @param w2 2nd writer
     * @param w3 3rd writer
     */
    void process(I i, W1 w1, W2 w2, W3 w3);
}
