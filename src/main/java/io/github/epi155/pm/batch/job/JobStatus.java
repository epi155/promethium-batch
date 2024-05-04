package io.github.epi155.pm.batch.job;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Step return status interface
 */
public interface JobStatus
        extends ExecPgm, NextPgm, ElsePgm,
        ForkExecPgm, ForkNextPgm, ForkElsePgm {
    /**
     * Compose JobStatus
     * <p>used for conditional step
     * <pre>
     * int xc = JCL.getInstance()
     *             .execPgm(count1, this::step01)
     *             .map(s -> s.isSuccess() ? s.execPgm(count2, this::step02)
     *                                     : s.execPgm(count3, this::step03))
     *             .returnCode();
     * </pre>
     *
     * @param map function to compose status
     * @return composed JobStatus
     */
    JobStatus map(@NotNull UnaryOperator<JobStatus> map);

    /**
     * Max step return code
     *
     * @return max step return code
     */
    int maximumConditionCode();

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
     * Complete job, and get job return code
     *
     * @return job return code
     */
    int complete();

    /**
     * indicates whether the status completed successfully
     *
     * @return {@code true} if step completed OK thru WARN, else {@code false}
     */
    boolean isSuccess();

    /**
     * waits for the completion of the programs launched in the background
     * and returns the state with the highest returnCode amongst the current state
     * and the states of the finished programs running in the background
     *
     * @return status with the highest returnCode
     */
    JobStatus join();

    /**
     * Pushes jobReturnCode onto the internal stack
     *
     * @return original jobStatus
     */
    JobStatus push();

    /**
     * Pops jobReturnCode from the stack
     *
     * @return jobStatus with restored jobReturnCode
     */
    JobStatus pop();

    /**
     * Retrieves, but does not remove, the head of the stack
     *
     * @return jobStatus with restored jobReturnCode
     */
    JobStatus peek();
}
