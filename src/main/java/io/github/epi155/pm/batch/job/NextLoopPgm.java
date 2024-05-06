package io.github.epi155.pm.batch.job;

import java.util.Iterator;
import java.util.List;
import java.util.function.*;

public interface NextLoopPgm {
    /**
     * Loop program launcher with user returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program loop is not launched
     *
     * @param p job parameters
     * @param c program statistics (by q)
     * @param pgm program (step) to execute
     * @return instance of {@link JobStatus}
     * @param <P> class to provide job parameters
     * @param <Q> class on which to repeat the program execution
     * @param <C> class to manage program statistics
     */
    <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus nextLoopPgm(P p, Function<Q, C> c, BiFunction<P, C, Integer> pgm);

    /**
     * Loop program launcher with user returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program loop is not launched
     *
     * @param p job parameters
     * @param c program statistics (by q)
     * @param pgm program (step) to execute
     * @return instance of {@link JobStatus}
     * @param <P> class to provide job parameters
     * @param <Q> class on which to repeat the program execution
     * @param <C> class to manage program statistics
     */
    <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus nextLoopPgm(P p, Function<Q, C> c, ToIntFunction<C> pgm);

    /**
     * Loop program launcher with automatic returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program loop is not launched
     *
     * @param p job parameters
     * @param c program statistics (by q)
     * @param pgm program (step) to execute
     * @return instance of {@link JobStatus}
     * @param <P> class to provide job parameters
     * @param <Q> class on which to repeat the program execution
     * @param <C> class to manage program statistics
     */
    <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus nextLoopPgm(P p, Function<Q, C> c, BiConsumer<P, C> pgm);

    /**
     * Loop program launcher with automatic returnCode
     * <p>if the previous step did not complete successfully,
     * the status of its execution is returned,
     * and the indicated program loop is not launched
     *
     * @param p job parameters
     * @param c program statistics (by q)
     * @param pgm program (step) to execute
     * @return instance of {@link JobStatus}
     * @param <P> class to provide job parameters
     * @param <Q> class on which to repeat the program execution
     * @param <C> class to manage program statistics
     */
    <P extends Iterable<Q>, Q, C extends StatsCount> JobStatus nextLoopPgm(P p, Function<Q, C> c, Consumer<C> pgm);

}
