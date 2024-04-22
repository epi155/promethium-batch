package io.github.epi155.pm.batch;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * interface to provide 2 readers and 1 writer
 *
 * @param <I1> input type for 1st reader
 * @param <I2> input type for 2nd reader
 * @param <O>  output type for writer
 */
public interface PullWorker2o1<I1, I2, O> {
    /**
     * provide 2 readers and 1 writer
     *
     * @param rd1 1st reader
     * @param rd2 2nd reader
     * @param wr  writer
     */
    void proceed(Supplier<? extends I1> rd1, Supplier<? extends I2> rd2, Consumer<? super O> wr);
}
