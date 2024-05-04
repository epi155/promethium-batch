package io.github.epi155.pm.batch.job;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * super class to manage the execution statistics
 */
@Slf4j
public abstract class StatsCount {
    private final String name;

    /**
     * class constructor
     *
     * @param name step custom name
     */
    protected StatsCount(String name) {
        this.name = name;
    }

    void recap(int returnCode) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.printf("Report %s:%n", name);
        recap(pw);
        pw.printf("ReturnCode: %d", returnCode);
        log.info(sw.toString());
    }

    /**
     * customized statistical report
     *
     * @param pw writer
     */
    protected abstract void recap(PrintWriter pw);

    String name() {
        return name;
    }
}
