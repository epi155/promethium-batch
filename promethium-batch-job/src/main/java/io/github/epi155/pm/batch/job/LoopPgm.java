package io.github.epi155.pm.batch.job;

import java.util.function.*;

/**
 * interface to run a program multiple times if the previous step completes successfully
 */
public interface LoopPgm<S> {
    /**
     * Loop program launcher with user returnCode
     *
     * @param p   job parameters
     * @param c   program statistics (by q)
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <Q> class on which to repeat the program execution
     * @param <C> class to manage program statistics
     * @return instance of {@link S}
     */
    <P extends Iterable<Q>, Q, C extends StatsCount> S forEachPgm(P p, Function<Q, C> c, BiFunction<Q, C, Integer> pgm);

    /**
     * Loop program launcher with user returnCode
     *
     * @param p    job parameters
     * @param name step name (by q)
     * @param pgm  program (step) to execute
     * @param <P>  class to provide job parameters
     * @param <Q>  class on which to repeat the program execution
     * @return instance of {@link S}
     */
    <P extends Iterable<Q>, Q> S forEachPgm(P p, Function<Q, String> name, ToIntFunction<Q> pgm);

    /**
     * Loop program launcher with automatic returnCode
     *
     * @param p   job parameters
     * @param c   program statistics (by q)
     * @param pgm program (step) to execute
     * @param <P> class to provide job parameters
     * @param <Q> class on which to repeat the program execution
     * @param <C> class to manage program statistics
     * @return instance of {@link S}
     */
    <P extends Iterable<Q>, Q, C extends StatsCount> S forEachPgm(P p, Function<Q, C> c, BiConsumer<Q, C> pgm);

    /**
     * Loop program launcher with automatic returnCode
     *
     * @param p    job parameters
     * @param name step name (by q)
     * @param pgm  program (step) to execute
     * @param <P>  class to provide job parameters
     * @param <Q>  class on which to repeat the program execution
     * @return instance of {@link S}
     */
    <P extends Iterable<Q>, Q> S forEachPgm(P p, Function<Q, String> name, Consumer<Q> pgm);
}
