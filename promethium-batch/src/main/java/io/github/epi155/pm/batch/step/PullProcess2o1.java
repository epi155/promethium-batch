package io.github.epi155.pm.batch.step;

/**
 * interface to manage 2 input to 1 output processing
 *
 * @param <I1> 1st input type
 * @param <I2> 2nd input type
 * @param <O>  output type
 */
public interface PullProcess2o1<I1, I2, O> {
    /**
     * processes the data
     * <pre>
     * Pgm.from(src1, src2).into(dst).proceed((rd1, rd2, wr) -> {
     *     val i1 = rd1.get();
     *     val i2 = rd2.get();
     *     ...
     *     wr.accept(o);
     * });
     * </pre>
     *
     * @param worker worker who read the input values and writes the output value
     */
    void proceed(PullWorker2o1<I1, I2, O> worker);
}
