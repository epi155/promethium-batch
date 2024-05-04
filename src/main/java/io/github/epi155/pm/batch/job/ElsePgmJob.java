package io.github.epi155.pm.batch.job;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

/**
 * interface to run a program if the previous step did not complete successfully
 */
public interface ElsePgmJob extends ElsePgmProc {
    /**
     * Program launcher with user returnCode
     * <p>if the previous step is completed successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <P, C extends StatsCount> JobStatus elsePgm(P p, C c, BiFunction<P, C, Integer> pgm);

    /**
     * Program launcher with user returnCode
     * <p>if the previous step is completed successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StatsCount> JobStatus elsePgm(C c, ToIntFunction<C> pgm);

    /**
     * Program launcher with automatic returnCode
     * <p>if the previous step is completed successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <P, C extends StatsCount> JobStatus elsePgm(P p, C c, BiConsumer<P, C> pgm);

    /**
     * Program launcher with automatic returnCode
     * <p>if the previous step is completed successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StatsCount> JobStatus elsePgm(C c, Consumer<C> pgm);
}
