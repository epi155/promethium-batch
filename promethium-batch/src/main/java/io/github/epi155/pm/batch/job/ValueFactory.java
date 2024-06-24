package io.github.epi155.pm.batch.job;

/**
 * ErrorCode Factory Interface
 */
public interface ValueFactory {
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

    /**
     * Max returnCode operator
     *
     * @param a 1st return code
     * @param b 2nd return code
     * @return maximum return code
     */
    int rcMax(int a, int b);

    /**
     * MDC key for jobName
     *
     * @return MDC key for jobName
     */
    String jobName();

    /**
     * MDC key for stepName
     *
     * @return MDC key for stepName
     */
    String stepName();
}
