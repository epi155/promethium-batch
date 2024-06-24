package io.github.epi155.pm.batch.job;

/**
 * interface to manage programs (procedures) that are running in the background
 */
public interface Joinable {
    /**
     * waits for the completion of all the programs launched in the background.
     * {@code lastcc} will be the maximum returnCode returned by programs running in the background
     *
     * @return instance of {@link JobStatus}
     */
    JobStatus join();

    /**
     * waits for the completion of the program (procedure) launched in the background.
     *
     * @param name name of the step (proc) associated with the program (procedure)
     * @return instance of {@link JobStatus} with step(proc) return code
     */
    JobStatus join(String name);

    /**
     * sends a stop signal to the program (procedure) that is running in the background
     *
     * @param name name of the step (proc) associated with the program (procedure)
     * @return unchanged instance of {@link JobStatus}
     */
    JobStatus quit(String name);
}
