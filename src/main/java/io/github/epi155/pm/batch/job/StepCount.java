package io.github.epi155.pm.batch.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * super class to manage the execution statistics of a single step (program)
 */
@Slf4j
public abstract class StepCount {
    private final String stepName;
    /**
     * Step return code
     */
    @Setter
    protected int returnCode;

    /**
     * class constructor
     *
     * @param stepName step custom name
     */
    protected StepCount(String stepName) {
        this.stepName = stepName;
    }

    void recap() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.printf("Report %s:%n", stepName);
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
        return stepName;
    }
}
