package io.github.epi155.pm.batch.job;

import java.time.Instant;

/**
 * interface to notify the execution of programs to the job
 */
public interface JobTrace {
    /**
     * provides information on program execution
     *
     * @param name       step name
     * @param returnCode step return code
     * @param tiStart    step start timestamp
     * @param tiEnd      step end timestamp
     */
    void add(String name, int returnCode, Instant tiStart, Instant tiEnd);

    /**
     * provides information on command execution
     *
     * @param name       command name
     * @param returnCode job code after command execution
     */
    void add(String name, int returnCode);

    /**
     * provides information on skipped program
     *
     * @param name step name
     */
    void add(String name);
}
