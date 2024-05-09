package io.github.epi155.pm.batch.job;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ParallelStatus {
    /**
     * Loop program launcher with user returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program loop is not launched
     *
     * @param p   job parameters
     * @param c   program statistics (by q)
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <Q> class on which to repeat the program execution
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P p, Function<Q, C> c, BiFunction<Q, C, Integer> pgm);

    /**
     * Loop program launcher with automatic returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program loop is not launched
     *
     * @param p   job parameters
     * @param c   program statistics (by q)
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <Q> class on which to repeat the program execution
     * @param <C> class to manage program statistics
     * @return instance of {@link JobStatus}
     */
    <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus forEachPgm(P p, Function<Q, C> c, BiConsumer<Q, C> pgm);

    /**
     * Loop procedure launcher
     *
     * @param p    job parameters
     * @param name function that maps the procedure name from the iterable element of the job parameters
     * @param proc procedure
     * @param <P>  class to provide job parameters
     * @param <Q>  class on which to repeat the procedure execution
     * @return instance of {@link JobStatus}
     */
    <P extends Iterable<Q>, Q> JobStatus forEachProc(P p, Function<Q, String> name, UnaryOperator<SubStatus<Q>> proc);
}
