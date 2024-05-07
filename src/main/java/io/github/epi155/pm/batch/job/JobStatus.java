package io.github.epi155.pm.batch.job;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Job environment
 */
public interface JobStatus
        extends ProcStatus,
        ExecPgmJob, NextPgmJob, ElsePgmJob,
        ForkExecPgm, ForkNextPgm, ForkElsePgm, NextLoopPgm {
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
     * waits for the completion of the programs launched in the background
     * and returns the state with the highest returnCode amongst the current state
     * and the states of the finished programs running in the background
     *
     * @return status with the highest returnCode
     */
    JobStatus join();

    /**
     * Procedure launcher in background unconditionally
     *
     * <p>example
     * <pre>
     * int rc = JCL.getInstance().job("Job01")
     *     .forkExecProc("Proc01", it -> it
     *             .execPgm(count1, this::step01)
     *             .nextPgm(count2, this::step02)
     *     )
     *     .execPgm(count3, this::step03)
     *     .join()
     *     .complete();
     * </pre>
     *
     * @param procName procedure name
     * @param proc     procedure
     * @return instance of {@link JobStatus}
     */
    JobStatus forkExecProc(String procName, UnaryOperator<ProcStatus> proc);

    /**
     * Procedure launcher in background if the previous step complete successfully
     *
     * <p>example
     * <pre>
     * int rc = JCL.getInstance().job("Job01")
     *     .execPgm(count1, this::step01)
     *     .forkNextProc("Proc01", it -> it
     *             .execPgm(count2, this::step02)
     *             .nextPgm(count3, this::step03)
     *     )
     *     .elsePgm(count4, this::step04)
     *     .join()
     *     .complete();
     * </pre>
     *
     * @param procName procedure name
     * @param proc     procedure
     * @return instance of {@link JobStatus}
     */
    JobStatus forkNextProc(String procName, UnaryOperator<ProcStatus> proc);

    /**
     * Procedure launcher in background if the previous step did not complete successfully
     *
     * <p>example
     * <pre>
     * int rc = JCL.getInstance().job("Job01")
     *     .execPgm(count1, this::step01)
     *     .forkElseProc("Proc01", it -> it
     *             .execPgm(count2, this::step02)
     *             .nextPgm(count3, this::step03)
     *     )
     *     .nextPgm(count4, this::step04)
     *     .join()
     *     .complete();
     * </pre>
     *
     * @param procName procedure name
     * @param proc     procedure
     * @return instance of {@link JobStatus}
     */
    JobStatus forkElseProc(String procName, UnaryOperator<ProcStatus> proc);

    /**
     * Procedure launcher unconditionally
     *
     * <p>example
     * <pre>
     * int rc = JCL.getInstance().job("Job01")
     *     .execProc("Proc01", it -> it
     *             .execPgm(count1, this::step01)
     *             .nextPgm(count2, this::step02)
     *     )
     *     .nextPgm(count3, this::step03)
     *     .complete();
     * </pre>
     *
     * @param procName procedure name
     * @param proc     procedure
     * @return instance of {@link JobStatus}
     */
    JobStatus execProc(String procName, UnaryOperator<ProcStatus> proc);

    /**
     * Procedure launcher if the previous step complete successfully
     *
     * <p>example
     * <pre>
     * int rc = JCL.getInstance().job("Job01")
     *     .execPgm(count1, this::step01)
     *     .nextProc("Proc01", it -> it
     *             .execPgm(count2, this::step02)
     *             .nextPgm(count3, this::step03)
     *     )
     *     .complete();
     * </pre>
     *
     * @param procName procedure name
     * @param proc     procedure
     * @return instance of {@link JobStatus}
     */
    JobStatus nextProc(String procName, UnaryOperator<ProcStatus> proc);

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
    <P extends Iterable<Q>, Q> JobStatus nextLoopProc(P p, Function<Q, String> name, UnaryOperator<ProcStatus> proc);

    /**
     * Procedure launcher if the previous step did not complete successfully
     *
     * <p>example
     * <pre>
     * int rc = JCL.getInstance().job("Job01")
     *     .execPgm(count1, this::step01)
     *     .elseProc("Proc01", it -> it
     *             .execPgm(count2, this::step02)
     *             .nextPgm(count3, this::step03)
     *     )
     *     .complete();
     * </pre>
     *
     * @param procName procedure name
     * @param proc     procedure
     * @return instance of {@link JobStatus}
     */
    JobStatus elseProc(String procName, UnaryOperator<ProcStatus> proc);

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
