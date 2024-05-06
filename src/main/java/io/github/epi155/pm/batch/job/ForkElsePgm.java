package io.github.epi155.pm.batch.job;

import java.util.function.*;

/**
 * interface to run a program in background if the previous step did not complete successfully
 */
public interface ForkElsePgm {
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
    <P, C extends StatsCount> JobStatus forkElsePgm(P p, C c, BiFunction<P, C, Integer> pgm);
    <P> JobStatus forkElsePgm(P p, String stepName, Function<P, Integer> pgm);

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
    <C extends StatsCount> JobStatus forkElsePgm(C c, ToIntFunction<C> pgm);
    JobStatus forkElsePgm(String stepName, Supplier<Integer> pgm);

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
    <P, C extends StatsCount> JobStatus forkElsePgm(P p, C c, BiConsumer<P, C> pgm);
    <P> JobStatus forkElsePgm(P p, String stepName, Consumer<P> pgm);

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
    <C extends StatsCount> JobStatus forkElsePgm(C c, Consumer<C> pgm);
    JobStatus forkElsePgm(String stepName, Runnable pgm);
}
