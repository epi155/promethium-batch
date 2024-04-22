package io.github.epi155.pm.batch;

/**
 * interface to manage 2 input to 2 output processing
 *
 * @param <I1> 1st input type
 * @param <I2> 2nd input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 */
public interface PullProcess2o2<I1, I2, O1, O2> {
    /**
     * processes the data
     * <pre>
     * Loop.from(src1, src2).into(dst).proceed((rd1, rd2, wr1, wr2) -> {
     *     val i1 = rd1.get();
     *     val i2 = rd2.get();
     *     ...
     *     wr1.accept(o1);
     *     wr2.accept(o2);
     * });
     * </pre>
     *
     * @param worker worker who read the input values and writes the output value
     */
    void proceed(PullWorker2o2<I1, I2, O1, O2> worker);
}
