package io.github.epi155.pm.batch.job;

import java.util.function.*;

/**
 * interface to run a program if the previous step complete successfully
 */
public interface NextPgmProc {
    /**
     * Program launcher with user returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link ProcStatus}
     */
    <P, C extends StatsCount> ProcStatus nextPgm(P p, C c, BiFunction<P, C, Integer> pgm);

    /**
     * Program launcher with user returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param p        job parameters
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @param <P>      class to provide job parameters
     * @return instance of {@link ProcStatus}
     */
    <P> ProcStatus nextPgm(P p, String stepName, ToIntFunction<P> pgm);

    /**
     * Program launcher with user returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link ProcStatus}
     */
    <C extends StatsCount> ProcStatus nextPgm(C c, ToIntFunction<C> pgm);

    /**
     * Program launcher with user returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @return instance of {@link ProcStatus}
     */
    ProcStatus nextPgm(String stepName, IntSupplier pgm);

    /**
     * Program launcher with automatic returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param p   job parameters
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <C> class to manage program statistics
     * @return instance of {@link ProcStatus}
     */
    <P, C extends StatsCount> ProcStatus nextPgm(P p, C c, BiConsumer<P, C> pgm);

    /**
     * Program launcher with automatic returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param p        job parameters
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @param <P>      class to provide job parameters
     * @return instance of {@link ProcStatus}
     */
    <P> ProcStatus nextPgm(P p, String stepName, Consumer<P> pgm);

    /**
     * Program launcher with automatic returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param c   program statistics
     * @param pgm program (step) to execute
     * @param <C> class to manage program statistics
     * @return instance of {@link ProcStatus}
     */
    <C extends StatsCount> ProcStatus nextPgm(C c, Consumer<C> pgm);

    /**
     * Program launcher with automatic returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program is not launched
     *
     * @param stepName step name
     * @param pgm      program (step) to execute
     * @return instance of {@link ProcStatus}
     */
    ProcStatus nextPgm(String stepName, Runnable pgm);
}
