package io.github.epi155.pm.batch.job;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

/**
 * interface for running a program unconditionally in background
 */
public interface ForkPgm {
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
    <P, C extends StepCount> JobStatus forkPgm(P p, C c, BiFunction<P, C, Integer> pgm);

    /**
     * Program launcher in background with user returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StepCount> JobStatus forkPgm(C c, ToIntFunction<C> pgm);

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
    <P, C extends StepCount> JobStatus forkPgm(P p, C c, BiConsumer<P, C> pgm);

    /**
     * Program launcher in background with automatic returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <C extends StepCount> JobStatus forkPgm(C c, Consumer<C> pgm);
}
