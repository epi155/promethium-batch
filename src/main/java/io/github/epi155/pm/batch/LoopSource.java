package io.github.epi155.pm.batch;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * interface to manage the elements of a single input resource.
 * <p>
 * it is possible to process the input immediately or to define one or more
 * output resources where the input processing can be sent
 *
 * @param <I> input type
 */
public interface LoopSource<I> extends ParallelLoop0<I> {
    /**
     * Performs the specified action for each item taken from the source until all items have been processed or the
     * action throws an exception.
     *
     * @param action The action to be performed for each element
     */
    void forEach(Consumer<? super I> action);

    /**
     * sets the sink resource
     * <pre>
     * Batch.from(source)<b>.into(sink)</b>.forEach(src -&gt; ...)
     * </pre>
     *
     * @param sink the sink resource
     * @param <T>  sink resource type
     * @param <O>  sink element type
     * @return instance of {@link IterableLoop}
     */
    <T extends AutoCloseable, O> IterableLoop<I, O> into(SinkResource<T, O> sink);

    /**
     * set 2 output resources
     * <pre>
     * Batch.from(source)<b>.into(sink1,sink2)</b>.forEach(src -&gt; ...)
     * </pre>
     *
     * @param sink1 1st sink resource
     * @param sink2 2nd sink resource
     * @param <T1>  1st sink resource type
     * @param <O1>  1st sink element type
     * @param <T2>  2nd sink resource type
     * @param <O2>  2nd sink element type
     * @return instance of {@link IterableLoop2}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2>
    IterableLoop2<I, O1, O2> into(SinkResource<T1, O1> sink1, SinkResource<T2, O2> sink2);

    /**
     * set 3 output resources
     * <pre>
     * Batch.from(source)<b>.into(sink1,sink2,sink3)</b>.forEach(src -&gt; ...)
     * </pre>
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
     * @return instance of {@link IterableLoop3}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3>
    IterableLoop3<I, O1, O2, O3> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3);

    /**
     * set 4 output resources
     * <pre>
     * Batch.from(source)<b>.into(sink1,sink2,sink3,sink4)</b>.forEach(src -&gt; ...)
     * </pre>
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
     * @return instance of {@link IterableLoop4}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4>
    IterableLoop4<I, O1, O2, O3, O4> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4);

    /**
     * set 5 output resources
     * <pre>
     * Batch.from(source)<b>.into(sink1,sink2,sink3,sink4,sink5)</b>.forEach(src -&gt; ...)
     * </pre>
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
     * @return instance of {@link IterableLoop5}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5>
    IterableLoop5<I, O1, O2, O3, O4, O5> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5);

    /**
     * set 6 output resources
     * <pre>
     * Batch.from(source)<b>.into(sink1,sink2,sink3,sink4,sink5,sink6)</b>.forEach(src -&gt; ...)
     * </pre>
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
     * @return instance of {@link IterableLoop6}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6>
    IterableLoop6<I, O1, O2, O3, O4, O5, O6> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6);

    /**
     * set 7 output resources
     * <pre>
     * Batch.from(source)<b>.into(sink1,sink2,sink3,sink4,sink5,sink6,sink7)</b>.forEach(src -&gt; ...)
     * </pre>
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
     * @return instance of {@link IterableLoop7}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6,
            T7 extends AutoCloseable, O7>
    IterableLoop7<I, O1, O2, O3, O4, O5, O6, O7> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7);

    /**
     * set 8 output resources
     * <pre>
     * Batch.from(source)<b>.into(sink1,sink2,sink3,sink4,sink5,sink6,sink7,sink8)</b>.forEach(src -&gt; ...)
     * </pre>
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
     * @return instance of {@link IterableLoop8}
     */
    <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6,
            T7 extends AutoCloseable, O7,
            T8 extends AutoCloseable, O8>
    IterableLoop8<I, O1, O2, O3, O4, O5, O6, O7, O8> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7,
            SinkResource<T8, O8> sink8);

    /**
     * sets the shutdown timeout for parallel processing
     *
     * @param time time amount
     * @param unit time unit
     * @return instance of {@link ParallelLoop0} for run parallel processing
     */
    ParallelLoop0<I> shutdownTimeout(long time, TimeUnit unit);
}
