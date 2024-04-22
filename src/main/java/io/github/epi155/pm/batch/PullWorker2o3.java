package io.github.epi155.pm.batch;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * interface to provide 2 readers and 3 writers
 *
 * @param <I1> input type for 1st reader
 * @param <I2> input type for 2nd reader
 * @param <O1> output type for 1st writer
 * @param <O2> output type for 2nd writer
 * @param <O3> output type for 3rd writer
 */
public interface PullWorker2o3<I1, I2, O1, O2, O3> {
    /**
     * provide 2 readers and 3 writers
     *
     * @param rd1 1st reader
     * @param rd2 2nd reader
     * @param wr1 1st writer
     * @param wr2 2nd writer
     * @param wr3 3rd writer
     */
    void proceed(Supplier<? extends I1> rd1, Supplier<? extends I2> rd2,
                 Consumer<? super O1> wr1,
                 Consumer<? super O2> wr2,
                 Consumer<? super O3> wr3);
}
