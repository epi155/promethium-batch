package io.github.epi155.pm.batch;

/**
 * interface to manage 2 input to 4 output processing
 *
 * @param <I1> 1st input type
 * @param <I2> 2nd input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 * @param <O3> 3rd output type
 * @param <O4> 4th output type
 */
public interface PullProcess2o4<I1, I2, O1, O2, O3, O4> {
    /**
     * processes the data
     * <pre>
     * Batch.from(src1, src2).into(dst).proceed((rd1, rd2, wr1, wr2, wr3, wr4) -> {
     *     val i1 = rd1.get();
     *     val i2 = rd2.get();
     *     ...
     *     wr1.accept(o1);
     *     wr2.accept(o2);
     *     wr3.accept(o3);
     *     wr4.accept(o4);
     * });
     * </pre>
     *
     * @param worker worker who read the input values and writes the output value
     */
    void proceed(PullWorker2o4<I1, I2, O1, O2, O3, O4> worker);
}
