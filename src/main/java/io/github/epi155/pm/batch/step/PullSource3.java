package io.github.epi155.pm.batch.step;

/**
 * interface to manage the elements of three input resources.
 * <p>
 * unlike the single source, where it is possible to push the data towards the processing process, in the case of
 * multiple sources it is the processing process that decides from which source, and when, to pull the data to be
 * processed
 *
 * @param <I1> element type of the 1st resource
 * @param <I2> element type of the 2nd resource
 * @param <I3> element type of the 3rd resource
 */
public interface PullSource3<I1, I2, I3> {
    /**
     * sets the sink resource to process the data
     *
     * @param sink the sink resource
     * @param <T>  sink resource type
     * @param <O>  sink element type
     * @return instance of {@link PullProcess3o1}
     */
    <T extends AutoCloseable, O> PullProcess3o1<I1, I2, I3, O> into(SinkResource<T, O> sink);

    /**
     * sets the sinks resource to process the data
     *
     * @param sink1 1st sink resource
     * @param sink2 2nd sink resource
     * @param <T1>  1st sink resource type
     * @param <O1>  1st sink element type
     * @param <T2>  2nd sink resource type
     * @param <O2>  2nd sink element type
     * @return instance of {@link PullProcess3o2}
     */
    <T1 extends AutoCloseable, O1, T2 extends AutoCloseable, O2>
    PullProcess3o2<I1, I2, I3, O1, O2> into(SinkResource<T1, O1> sink1, SinkResource<T2, O2> sink2);

    /**
     * sets the sinks resource to process the data
     *
     * @param sink1 1st sink resource
     * @param sink2 2nd sink resource
     * @param sink3 3rd sink resource
     * @param <T1>  1st sink resource type
     * @param <O1>  1st sink element type
     * @param <T2>  2nd sink resource type
     * @param <O2>  2nd sink element type
     * @param <T3>  3rd sink resource type
     * @param <O3>  3rd sink element type
     * @return instance of {@link PullProcess3o3}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3>
    PullProcess3o3<I1, I2, I3, O1, O2, O3> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3);

    /**
     * sets the sinks resource to process the data
     *
     * @param sink1 1st sink resource
     * @param sink2 2nd sink resource
     * @param sink3 3rd sink resource
     * @param sink4 4th sink resource
     * @param <T1>  1st sink resource type
     * @param <O1>  1st sink element type
     * @param <T2>  2nd sink resource type
     * @param <O2>  2nd sink element type
     * @param <T3>  3rd sink resource type
     * @param <O3>  3rd sink element type
     * @param <T4>  4th sink resource type
     * @param <O4>  4th sink element type
     * @return instance of {@link PullProcess3o4}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4>
    PullProcess3o4<I1, I2, I3, O1, O2, O3, O4> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4);

    /**
     * sets the sinks resource to process the data
     *
     * @param sink1 1st sink resource
     * @param sink2 2nd sink resource
     * @param sink3 3rd sink resource
     * @param sink4 4th sink resource
     * @param sink5 5th sink resource
     * @param <T1>  1st sink resource type
     * @param <O1>  1st sink element type
     * @param <T2>  2nd sink resource type
     * @param <O2>  2nd sink element type
     * @param <T3>  3rd sink resource type
     * @param <O3>  3rd sink element type
     * @param <T4>  4th sink resource type
     * @param <O4>  4th sink element type
     * @param <T5>  5th sink resource type
     * @param <O5>  5th sink element type
     * @return instance of {@link PullProcess3o5}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5>
    PullProcess3o5<I1, I2, I3, O1, O2, O3, O4, O5> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5);

    /**
     * sets the sinks resource to process the data
     *
     * @param sink1 1st sink resource
     * @param sink2 2nd sink resource
     * @param sink3 3rd sink resource
     * @param sink4 4th sink resource
     * @param sink5 5th sink resource
     * @param sink6 6th sink resource
     * @param <T1>  1st sink resource type
     * @param <O1>  1st sink element type
     * @param <T2>  2nd sink resource type
     * @param <O2>  2nd sink element type
     * @param <T3>  3rd sink resource type
     * @param <O3>  3rd sink element type
     * @param <T4>  4th sink resource type
     * @param <O4>  4th sink element type
     * @param <T5>  5th sink resource type
     * @param <O5>  5th sink element type
     * @param <T6>  6th sink resource type
     * @param <O6>  6th sink element type
     * @return instance of {@link PullProcess3o6}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6>
    PullProcess3o6<I1, I2, I3, O1, O2, O3, O4, O5, O6> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6);

    /**
     * sets the sinks resource to process the data
     *
     * @param sink1 1st sink resource
     * @param sink2 2nd sink resource
     * @param sink3 3rd sink resource
     * @param sink4 4th sink resource
     * @param sink5 5th sink resource
     * @param sink6 6th sink resource
     * @param sink7 7th sink resource
     * @param <T1>  1st sink resource type
     * @param <O1>  1st sink element type
     * @param <T2>  2nd sink resource type
     * @param <O2>  2nd sink element type
     * @param <T3>  3rd sink resource type
     * @param <O3>  3rd sink element type
     * @param <T4>  4th sink resource type
     * @param <O4>  4th sink element type
     * @param <T5>  5th sink resource type
     * @param <O5>  5th sink element type
     * @param <T6>  6th sink resource type
     * @param <O6>  6th sink element type
     * @param <T7>  7th sink resource type
     * @param <O7>  7th sink element type
     * @return instance of {@link PullProcess3o7}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6,
            T7 extends AutoCloseable, O7>
    PullProcess3o7<I1, I2, I3, O1, O2, O3, O4, O5, O6, O7> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7);

    /**
     * sets the sinks resource to process the data
     *
     * @param sink1 1st sink resource
     * @param sink2 2nd sink resource
     * @param sink3 3rd sink resource
     * @param sink4 4th sink resource
     * @param sink5 5th sink resource
     * @param sink6 6th sink resource
     * @param sink7 7th sink resource
     * @param sink8 8th sink resource
     * @param <T1>  1st sink resource type
     * @param <O1>  1st sink element type
     * @param <T2>  2nd sink resource type
     * @param <O2>  2nd sink element type
     * @param <T3>  3rd sink resource type
     * @param <O3>  3rd sink element type
     * @param <T4>  4th sink resource type
     * @param <O4>  4th sink element type
     * @param <T5>  5th sink resource type
     * @param <O5>  5th sink element type
     * @param <T6>  6th sink resource type
     * @param <O6>  6th sink element type
     * @param <T7>  7th sink resource type
     * @param <O7>  7th sink element type
     * @param <T8>  8th sink resource type
     * @param <O8>  8th sink element type
     * @return instance of {@link PullProcess3o8}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6,
            T7 extends AutoCloseable, O7,
            T8 extends AutoCloseable, O8>
    PullProcess3o8<I1, I2, I3, O1, O2, O3, O4, O5, O6, O7, O8> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7,
            SinkResource<T8, O8> sink8);

    /**
     * processes the data
     * <pre>
     * Batch.from(src1, src2, src3).proceed((rd1, rd2, rd3) -> {
     *     val i1 = rd1.get();
     *     val i2 = rd2.get();
     *     val i3 = rd3.get();
     *     ...
     * });
     * </pre>
     *
     * @param worker worker who read the input values
     */
    void proceed(PullWorker3o0<I1, I2, I3> worker);
}
