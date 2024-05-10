package io.github.epi155.pm.batch.job;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Job environment
 */
public interface JobStatus
        extends ExecPgm<JobStatus>, LoopPgm<JobStatus>, ExecProc<JobStatus>, LoopProc<JobStatus>
{
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
    JobStatus exec(@NotNull Consumer<JobStatus> action);

    /**
     * Max step return code
     *
     * @return max step return code
     */
    int maximumConditionCode();

    /**
     * Complete job, and get job return code
     *
     * @return job return code
     */
    int complete();

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

    /**
     * condition for not performing the next operation
     * <p>
     * example
     * <pre>
     * JCL.getInstance().job("job01")
     *     .execPgm("step01", this::step01)
     *     <b>.cond(0,NE)</b>.execPgm("step02", this::step02)
     *     .complete();
     * </pre>
     * if the previous program (step01) ends with a return code other than zero (NE),
     * it does not execute the program that follows the condition (step02)
     *
     * @param cc value to test
     * @param cond condition to be tested
     * @return state to which to apply the operation
     */
    CondStatus<JobStatus> cond(int cc, Cond cond);

    /**
     * condition for performing the next operation
     * <p>
     * example
     * <pre>
     * JCL.getInstance().job("job01")
     *     .execPgm("step01", this::step01)
     *     <b>.when(0,EQ)</b>.execPgm("step02", this::step02)
     *     .complete();
     * </pre>
     * if the previous program (step01) ends with a return code equal (EQ) to zero,
     * executes the program that follows the condition (step02)
     *
     * @param cc value to test
     * @param cond condition to be tested
     * @return state to which to apply the operation
     */
    default CondStatus<JobStatus> when(int cc, Cond cond) {
        return cond(cc, cond.not());
    }

    /**
     * condition for not performing the next operation
     * <p>
     * if the given name does not exist or has not been executed, the next program is executed
     * <p>
     * example
     * <pre>
     * JCL.getInstance().job("job01")
     *     .execPgm("step01", this::step01)
     *     <b>.cond(0,NE,"step01")</b>
     *         .execPgm("step02", this::step02)
     *     .complete();
     * </pre>
     * if the indicated program (step01) ends with a non-zero return code (NE),
     * it does not execute the program that follows the condition (step02)
     *
     * @param cc value to test
     * @param cond condition to be tested
     * @param stepName name of the step to test
     * @return state to which to apply the operation
     */
    CondStatus<JobStatus> cond(int cc, Cond cond, String stepName);

    /**
     * condition for performing the next operation
     * <p>
     * if the given name does not exist or has not been executed, the next program is executed
     * <p>
     * example
     * <pre>
     * JCL.getInstance().job("job01")
     *     .execPgm("step01", this::step01)
     *     <b>.when(0,EQ,"step01")</b>
     *         .execPgm("step02", this::step02)
     *     .complete();
     * </pre>
     * if the indicated program (step01) ends with a return code equal (EQ) to zero,
     * executes the program that follows the condition (step02)
     *
     * @param cc value to test
     * @param cond condition to be tested
     * @param stepName name of the step to test
     * @return state to which to apply the operation
     */
    default CondStatus<JobStatus> when(int cc, Cond cond, String stepName) {
        return cond(cc, cond.not(), stepName);
    }
}
