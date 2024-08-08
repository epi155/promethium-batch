package io.github.epi155.pm.batch.job;

import java.util.function.*;

/**
 * interface for running a program unconditionally
 */
public interface ForkPgm<S> extends Joinable {
    /**
     * Program launcher in background with user returnCode
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link S}
     */
    <P, C extends StatsCount> S forkPgm(P p, C c, BiFunction<P, C, Integer> pgm);

    /**
     * Program launcher in background with user returnCode
     *
     * @param p        job parameters
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @param <P>      class to provide job parameters
     * @return instance of {@link S}
     */
    <P> S forkPgm(P p, String stepName, ToIntFunction<P> pgm);

    /**
     * Program launcher in background with user returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link S}
     */
    <C extends StatsCount> S forkPgm(C c, ToIntFunction<C> pgm);

    /**
     * Program launcher in background with user returnCode
     *
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @return instance of {@link S}
     */
    S forkPgm(String stepName, IntSupplier pgm);

    /**
     * Program launcher in background with automatic returnCode
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link S}
     */
    <P, C extends StatsCount> S forkPgm(P p, C c, BiConsumer<P, C> pgm);

    /**
     * Program launcher in background with automatic returnCode
     *
     * @param p        job parameters
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @param <P>      class to provide job parameters
     * @return instance of {@link S}
     */
    <P> S forkPgm(P p, String stepName, Consumer<P> pgm);

    /**
     * Program launcher in background with automatic returnCode
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link S}
     */
    <C extends StatsCount> S forkPgm(C c, Consumer<C> pgm);

    /**
     * Program launcher in background with automatic returnCode
     *
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @return instance of {@link S}
     */
    S forkPgm(String stepName, Runnable pgm);
}
