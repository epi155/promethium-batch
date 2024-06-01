package io.github.epi155.pm.batch.job;

import java.util.List;

/**
 * interface to provide information about job execution
 */
public interface JobInfo {
    /**
     * provides the name of the job
     *
     * @return job name
     */
    String getName();
    /**
     * provides the exit code of the job
     *
     * @return job exit code
     */
    int getExitCode();

    /**
     * provides the list of information on the steps that ended in error
     *
     * @return list of information on the steps that ended in error
     */
    List<StepError> getErrors();
}
