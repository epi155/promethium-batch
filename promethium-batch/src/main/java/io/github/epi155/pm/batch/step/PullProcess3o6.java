package io.github.epi155.pm.batch.step;

/**
 * interface to manage 3 input to 6 output processing
 *
 * @param <I1> 1st input type
 * @param <I2> 2nd input type
 * @param <I3> 3rd input type
 * @param <O1> 1st output type
 * @param <O2> 2nd output type
 * @param <O3> 3rd output type
 * @param <O4> 4th output type
 * @param <O5> 5th output type
 * @param <O6> 6th output type
 */
public interface PullProcess3o6<I1, I2, I3, O1, O2, O3, O4, O5, O6> {
    /**
     * processes the data
     * <pre>
     * Pgm.from(src1,src2,src3).into(dst).proceed((rd1,rd2,rd3,wr1,wr2,wr3,wr4,wr5,wr6) -> {
     *     val i1 = rd1.get();
     *     val i2 = rd2.get();
     *     val i3 = rd3.get();
     *     ...
     *     wr1.accept(o1);
     *     wr2.accept(o2);
     *     wr3.accept(o3);
     *     wr4.accept(o4);
     *     wr5.accept(o5);
     *     wr6.accept(o6);
     * });
     * </pre>
     *
     * @param worker worker who read the input values and writes the output value
     */
    void proceed(PullWorker3o6<I1, I2, I3, O1, O2, O3, O4, O5, O6> worker);
}
