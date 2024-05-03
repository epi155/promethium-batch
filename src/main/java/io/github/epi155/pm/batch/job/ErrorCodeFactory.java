package io.github.epi155.pm.batch.job;

/**
 * ErrorCode Factory Interface
 */
public interface ErrorCodeFactory {
    /**
     * Success error code
     *
     * @return success error code
     */
    int rcOk();

    /**
     * Warning error code
     *
     * @return warning error code
     */
    int rcWarning();

    /**
     * Error code on IO operation
     *
     * @return IO error code
     */
    int rcErrorIO();

    /**
     * Error code on SQL operation
     *
     * @return SQL errror code
     */
    int rcErrorSQL();

    /**
     * Error code at Step level
     *
     * @return step error code
     */
    int rcErrorStep();

    /**
     * Error code at Job level
     *
     * @return job error code
     */
    int rcErrorJob();
}
