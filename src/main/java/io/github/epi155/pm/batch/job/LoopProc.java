package io.github.epi155.pm.batch.job;

import java.util.function.Function;

/**
 * class to iterate a procedure
 *
 * @param <S> job class to operate on
 */
public interface LoopProc<S> {
    /**
     * iterate the execution of the procedure

     * @param p job parameter (or parent procedure)
     * @param name function that returns the procedure name from the procedure parameters
     * @param proc procedure to be performed
     * @return job class
     * @param <P> type of job parameter
     * @param <Q> type of procedure parameter
     */
    <P extends Iterable<Q>, Q> S forEachProc(P p, Function<Q, String> name, Proc<Q> proc);
}
