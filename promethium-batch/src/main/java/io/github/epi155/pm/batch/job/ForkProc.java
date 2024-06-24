package io.github.epi155.pm.batch.job;

/**
 * class to execute a procedure
 *
 * @param <S> job class to operate on
 */
public interface ForkProc<S> {
    /**
     * runs the procedure in background (with a parameter class)
     *
     * @param p        procedure parameters
     * @param procName procedure name
     * @param proc     procedure to be run
     * @param <P>      type of procedure parameters
     * @return job class
     */
    <P> S forkProc(P p, String procName, Proc<P> proc);

    /**
     * runs the procedure in background (without a parameter class)
     *
     * @param procName procedure name
     * @param proc     procedure to be run
     * @return job class
     */
    S forkProc(String procName, Proc<Void> proc);
}
