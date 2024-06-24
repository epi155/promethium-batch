package io.github.epi155.pm.batch.job;

/**
 * interface to provide information about the execution of the step that ended in error
 */
public interface StepError {
    /**
     * provides the name of the step
     *
     * @return step name
     */
    String getName();

    /**
     * provides the return code of the step
     *
     * @return step return code
     */
    int getReturnCode();

    /**
     * returns the step error
     *
     * @return step error
     */
    Throwable getError();
}
