package io.github.epi155.pm.batch.job;

import java.util.function.*;

/**
 * interface for running a program unconditionally
 */
public interface ExecPgmJob extends ExecPgmProc {
    /**
     * Program launcher with user returnCode
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <P, C extends StatsCount> JobStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm);

    /**
     * Program launcher with user returnCode
     *
     * @param p        job parameters
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @param <P>      class to provide job parameters
     * @return instance of {@link JobStatus}
     */
    <P> JobStatus execPgm(P p, String stepName, ToIntFunction<P> pgm);

    /**
     * Program launcher with user returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StatsCount> JobStatus execPgm(C c, ToIntFunction<C> pgm);

    /**
     * Program launcher with user returnCode
     *
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @return instance of {@link JobStatus}
     */
    JobStatus execPgm(String stepName, IntSupplier pgm);

    /**
     * Program launcher with automatic returnCode
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <P, C extends StatsCount> JobStatus execPgm(P p, C c, BiConsumer<P, C> pgm);

    /**
     * Program launcher with automatic returnCode
     *
     * @param p        job parameters
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @param <P>      class to provide job parameters
     * @return instance of {@link JobStatus}
     */
    <P> JobStatus execPgm(P p, String stepName, Consumer<P> pgm);

    /**
     * Program launcher with automatic returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StatsCount> JobStatus execPgm(C c, Consumer<C> pgm);

    /**
     * Program launcher with automatic returnCode
     *
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @return instance of {@link JobStatus}
     */
    JobStatus execPgm(String stepName, Runnable pgm);
}
