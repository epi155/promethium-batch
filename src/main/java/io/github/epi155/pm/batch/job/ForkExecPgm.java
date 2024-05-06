package io.github.epi155.pm.batch.job;

import java.util.function.*;

/**
 * interface for running a program unconditionally in background
 */
public interface ForkExecPgm {
    /**
     * Program launcher in background with user returnCode
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <P, C extends StatsCount> JobStatus forkExecPgm(P p, C c, BiFunction<P, C, Integer> pgm);
    <P> JobStatus forkExecPgm(P p, String stepName, Function<P, Integer> pgm);

    /**
     * Program launcher in background with user returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StatsCount> JobStatus forkExecPgm(C c, ToIntFunction<C> pgm);
    JobStatus forkExecPgm(String stepName, Supplier<Integer> pgm);

    /**
     * Program launcher in background with automatic returnCode
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <P, C extends StatsCount> JobStatus forkExecPgm(P p, C c, BiConsumer<P, C> pgm);
    <P> JobStatus forkExecPgm(P p, String stepName, Consumer<P> pgm);

    /**
     * Program launcher in background with automatic returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StatsCount> JobStatus forkExecPgm(C c, Consumer<C> pgm);
    JobStatus forkExecPgm(String stepName, Runnable pgm);
}
