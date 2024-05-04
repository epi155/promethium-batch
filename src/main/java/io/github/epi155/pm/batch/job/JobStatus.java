package io.github.epi155.pm.batch.job;

import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

/**
 * Step return status interface
 */
public interface JobStatus extends NextPgm, ExecPgm, ElsePgm, ForkPgm {
//    /**
//     * Complete Success Status
//     */
//    JobStatus OK = PmJobStatus.of(JCL.getInstance().rcOk());
//    /**
//     * Complete with Warning Status
//     */
//    JobStatus WARN = PmJobStatus.of(JCL.getInstance().rcWarning());

//    /**
//     * Static JobStatus constructor
//     *
//     * @param returnCode step return code
//     * @return instance of {@link JobStatus}
//     */
//    static JobStatus of(int returnCode) {
//        return PmJobStatus.of(returnCode);
//    }

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
     * Step return code
     *
     * @return return code
     */
    int returnCode();

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

    JobStatus push();

    JobStatus pop();

    JobStatus peek();
}
