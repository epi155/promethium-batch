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

    /**
     * Program launcher in background with user returnCode
     *
     * @param p        job parameters
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @param <P>      class to provide job parameters
     * @return instance of {@link JobStatus}
     */
    <P> JobStatus forkExecPgm(P p, String stepName, ToIntFunction<P> pgm);

    /**
     * Program launcher in background with user returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StatsCount> JobStatus forkExecPgm(C c, ToIntFunction<C> pgm);

    /**
     * Program launcher in background with user returnCode
     *
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @return instance of {@link JobStatus}
     */
    JobStatus forkExecPgm(String stepName, IntSupplier pgm);

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

    /**
     * Program launcher in background with automatic returnCode
     *
     * @param p        job parameters
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @param <P>      class to provide job parameters
     * @return instance of {@link JobStatus}
     */
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

    /**
     * Program launcher in background with automatic returnCode
     *
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @return instance of {@link JobStatus}
     */
    JobStatus forkExecPgm(String stepName, Runnable pgm);
}
