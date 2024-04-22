package io.github.epi155.pm.batch;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * interface to provide 2 readers and 8 writers
 *
 * @param <I1> input type for 1st reader
 * @param <I2> input type for 2nd reader
 * @param <O1> output type for 1st writer
 * @param <O2> output type for 2nd writer
 * @param <O3> output type for 3rd writer
 * @param <O4> output type for 4th writer
 * @param <O5> output type for 5th writer
 * @param <O6> output type for 6th writer
 * @param <O7> output type for 7th writer
 * @param <O8> output type for 8th writer
 */
public interface PullWorker2o8<I1, I2, O1, O2, O3, O4, O5, O6, O7, O8> {
    /**
     * provide 2 readers and 8 writers
     *
     * @param rd1 1st reader
     * @param rd2 2nd reader
     * @param wr1 1st writer
     * @param wr2 2nd writer
     * @param wr3 3rd writer
     * @param wr4 4th writer
     * @param wr5 5th writer
     * @param wr6 6th writer
     * @param wr7 7th writer
     * @param wr8 8th writer
     */
    void proceed(Supplier<? extends I1> rd1, Supplier<? extends I2> rd2,
                 Consumer<? super O1> wr1,
                 Consumer<? super O2> wr2,
                 Consumer<? super O3> wr3,
                 Consumer<? super O4> wr4,
                 Consumer<? super O5> wr5,
                 Consumer<? super O6> wr6,
                 Consumer<? super O7> wr7,
                 Consumer<? super O8> wr8);
}
