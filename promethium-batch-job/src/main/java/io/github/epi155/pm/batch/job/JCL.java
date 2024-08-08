package io.github.epi155.pm.batch.job;

import java.util.Collection;

/**
 * root interface to launch the job control language
 */
public interface JCL extends ValueFactory {
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

    /**
     * Initialize job environment
     *
     * @param name jobName
     * @param w    number of namespace nodes to use to select the stacktrace
     * @return instance of {@link JobStatus}
     */
    JobStatus job(String name, int w);

    /**
     * Initialize job environment
     *
     * @param name     jobName
     * @param prefixes namespace prefixes to use to select the stacktrace
     * @return instance of {@link JobStatus}
     */
    JobStatus job(String name, Collection<String> prefixes);
}
