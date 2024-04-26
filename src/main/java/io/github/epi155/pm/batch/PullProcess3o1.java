package io.github.epi155.pm.batch;

/**
 * interface to manage 3 input to 1 output processing
 *
 * @param <I1> 1st input type
 * @param <I2> 2nd input type
 * @param <I3> 3rd input type
 * @param <O>  output type
 */
public interface PullProcess3o1<I1, I2, I3, O> {
    /**
     * processes the data
     * <pre>
     * Batch.from(src1,src2,src3).into(dst).proceed((rd1,rd2,rd3,wr) -> {
     *     val i1 = rd1.get();
     *     val i2 = rd2.get();
     *     val i3 = rd3.get();
     *     ...
     *     wr.accept(o);
     * });
     * </pre>
     *
     * @param worker worker who read the input values and writes the output value
     */
    void proceed(PullWorker3o1<I1, I2, I3, O> worker);
}
