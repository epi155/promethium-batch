package io.github.epi155.pm.batch.job;

/**
 * class to execute a procedure
 *
 * @param <S> job class to operate on
 */
public interface ExecProc<S> {
    /**
     * runs the procedure (with a parameter class)
     *
     * @param p procedure parameters
     * @param procName procedure name
     * @param proc procedure to be run
     * @return job class
     * @param <P> type of procedure parameters
     */
    <P> S execProc(P p, String procName, Proc<P> proc);

    /**
     * runs the procedure (without a parameter class)
     *
     * @param procName procedure name
     * @param proc procedure to be run
     * @return job class
     */
    S execProc(String procName, Proc<Void> proc);
}
