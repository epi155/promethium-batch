package io.github.epi155.pm.batch;

/**
 * interface to provide 1 input item and 8 writers
 *
 * @param <I>  input type
 * @param <W1> 1st output writer
 * @param <W2> 2nd output writer
 * @param <W3> 3rd output writer
 * @param <W4> 4th output writer
 * @param <W5> 5th output writer
 * @param <W6> 6th output writer
 * @param <W7> 7th output writer
 * @param <W8> 8th output writer
 */
public interface Worker8<I, W1, W2, W3, W4, W5, W6, W7, W8> {
    /**
     * provide 1 input item and 8 writers
     *
     * @param i  input item
     * @param w1 1st writer
     * @param w2 2nd writer
     * @param w3 3rd writer
     * @param w4 4th writer
     * @param w5 5th writer
     * @param w6 6th writer
     * @param w7 7th writer
     * @param w8 8th writer
     */
    void process(I i, W1 w1, W2 w2, W3 w3, W4 w4, W5 w5, W6 w6, W7 w7, W8 w8);
}
