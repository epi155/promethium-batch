package io.github.epi155.pm.batch.step;

import java.util.function.Supplier;

/**
 * interface to provide 2 readers and no writer
 *
 * @param <I1> input type for 1st reader
 * @param <I2> input type for 2nd reader
 */
public interface PullWorker2o0<I1, I2> {
    /**
     * provide 2 readers and 1 writer
     *
     * @param rd1 1st reader
     * @param rd2 2nd reader
     */
    void proceed(Supplier<? extends I1> rd1, Supplier<? extends I2> rd2);
}
