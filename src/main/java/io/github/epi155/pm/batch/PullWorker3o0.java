package io.github.epi155.pm.batch;

import java.util.function.Supplier;

/**
 * interface to provide 3 readers and no writer
 *
 * @param <I1> input type for 1st reader
 * @param <I2> input type for 2nd reader
 * @param <I3> input type for 3rd reader
 */
public interface PullWorker3o0<I1, I2, I3> {
    /**
     * provide 3 readers and no writer
     *
     * @param rd1 1st reader
     * @param rd2 2nd reader
     * @param rd3 3rd reader
     */
    void proceed(
            Supplier<? extends I1> rd1,
            Supplier<? extends I2> rd2,
            Supplier<? extends I3> rd3);
}
