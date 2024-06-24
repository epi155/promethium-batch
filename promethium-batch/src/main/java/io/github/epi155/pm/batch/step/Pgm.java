package io.github.epi155.pm.batch.step;

/**
 * root interface for the batch process
 */
public interface Pgm {
    /**
     * Sets a source resource<br>
     * <pre>
     * Pgm<b>.from(source)</b>.into(sink).forEach(src -&gt; ...)
     * </pre>
     *
     * @param source the source resource
     * @param <S>    source resource type
     * @param <I>    source element type
     * @return instance of {@link LoopSource}
     */
    static <S extends AutoCloseable, I> LoopSource<I> from(SourceResource<S, I> source) {
        return new PmLoopSource<>(source);
    }

    /**
     * Sets a source resource pair<br>
     * <pre>
     * Pgm.from(source1, source2).into(sink).proceed((it1, it2, wr) -&gt; ...)
     * </pre>
     *
     * @param source1 the 1st source resource
     * @param source2 the 2nd source resource
     * @param <S1>    1st source resource type
     * @param <I1>    1st source element type
     * @param <S2>    2nd source resource type
     * @param <I2>    2nd source element type
     * @return instance of {@link PullSource2}
     */
    static <S1 extends AutoCloseable, I1,
            S2 extends AutoCloseable, I2>
    PullSource2<I1, I2>
    from(SourceResource<S1, I1> source1,
         SourceResource<S2, I2> source2) {
        return new PmPullSource2<>(source1, source2);
    }

    /**
     * Set three source resources<br>
     * <pre>
     * Pgm.from(src1, src2, src3).into(sink).proceed((it1, it2, it3, wr) -&gt; ...)
     * </pre>
     *
     * @param source1 the 1st source resource
     * @param source2 the 2nd source resource
     * @param source3 the 3rd source resource
     * @param <S1>    1st source resource type
     * @param <I1>    1st source element type
     * @param <S2>    2nd source resource type
     * @param <I2>    2nd source element type
     * @param <S3>    3rd source resource type
     * @param <I3>    3rd source element type
     * @return instance of {@link PullSource3}
     */
    static <S1 extends AutoCloseable, I1,
            S2 extends AutoCloseable, I2,
            S3 extends AutoCloseable, I3>
    PullSource3<I1, I2, I3>
    from(SourceResource<S1, I1> source1,
         SourceResource<S2, I2> source2,
         SourceResource<S3, I3> source3) {
        return new PmPullSource3<>(source1, source2, source3);
    }
}
