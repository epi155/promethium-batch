package io.github.epi155.pm.batch.job;

import java.util.Optional;

/**
 * Procedure environment
 */
public interface ProcStatus extends ExecPgmProc, NextPgmProc, ElsePgmProc {
    /**
     * Petrieves the returnCode of the step with the indicated name
     * <p>
     * if a step with the indicated name does not exist or has not been executed, an Optional.empty() is returned
     *
     * @param stepName step name
     * @return optional step return code
     */
    Optional<Integer> returnCode(String stepName);

    /**
     * indicates whether the status completed successfully
     *
     * @return {@code true} if step completed OK thru WARN, else {@code false}
     */
    boolean isSuccess();

    /**
     * Pushes jobReturnCode onto the internal stack
     *
     * @return original jobStatus
     */
    ProcStatus push();

    /**
     * Pops jobReturnCode from the stack
     *
     * @return jobStatus with restored jobReturnCode
     */
    ProcStatus pop();

    /**
     * Retrieves, but does not remove, the head of the stack
     *
     * @return jobStatus with restored jobReturnCode
     */
    ProcStatus peek();

//    ProcStatus onSuccessOrFailure(Consumer<? super ProcStatus> successAction, Consumer<? super ProcStatus> failureAction);
}
