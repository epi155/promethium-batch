package io.github.epi155.pm.batch.job;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Job environment
 */
public interface JobStatus
        extends JobAction<JobStatus>, CondAction<JobStatus> {
    /**
     * Action on JobStatus
     * <p>used for conditional step
     * <pre>
     * int xc = JCL.getInstance(),job("job")
     *             .execPgm(count1, this::step01)
     *             .exec(s -> s.isSuccess() ? s.execPgm(count2, this::step02)
     *                                     : s.execPgm(count3, this::step03))
     *             .complete();
     * </pre>
     *
     * @param action action ontatus
     * @return instance of {@link JobStatus}
     */
    JobStatus exec(Consumer<JobStatus> action);

    /**
     * Max step return code
     *
     * @return max step return code
     */
    int maxcc();

    /**
     * Last step return code
     *
     * @return last step return code
     */
    Integer lastcc();

    /**
     * Complete job, and get job return code
     *
     * @return job return code
     */
    int complete();

    /**
     * Completes the job and returns information about its execution
     *
     * @return information about the execution of the job
     */
    JobInfo done();

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

    /**
     * Retrieves the returnCode of the step with the indicated name
     * <p>
     * if a step with the indicated name does not exist or has not been executed, an Optional.empty() is returned
     *
     * @param stepName step name
     * @return optional step return code
     */
    Optional<Integer> returnCode(String stepName);

}
