package io.github.epi155.pm.batch.job;

/**
 * root interface to launch the job control language
 */
public interface JCL extends ErrorCodeFactory {
    /**
     * JCL singleton
     *
     * @return instance of {@link JCL}
     */
    static JCL getInstance() {
        return PmJCL.getInstance();
    }

    /**
     * Initialize job environment
     *
     * @param name jobName
     * @return instance of {@link JobStatus}
     */
    JobStatus job(String name);
}
