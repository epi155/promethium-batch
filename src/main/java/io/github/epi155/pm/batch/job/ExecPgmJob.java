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
    <P> JobStatus execPgm(P p, String stepName, Function<P, Integer> pgm);

    /**
     * Program launcher with user returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StatsCount> JobStatus execPgm(C c, ToIntFunction<C> pgm);
    JobStatus execPgm(String stepName, Supplier<Integer> pgm);

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
    JobStatus execPgm(String stepName, Runnable pgm);
}
