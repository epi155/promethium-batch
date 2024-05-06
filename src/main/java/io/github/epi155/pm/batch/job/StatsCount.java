package io.github.epi155.pm.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;

import java.io.PrintWriter;
import java.io.StringWriter;

import static io.github.epi155.pm.batch.step.BatchException.placeOf;

/**
 * super class to manage the execution statistics
 */
@Slf4j
public abstract class StatsCount {
    private static final Marker RECAP_MARKER = org.slf4j.MarkerFactory.getMarker("REPORT");
    private final String name;
    private Throwable error;

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
        if (error != null) {
            pw.printf("Error: %s%n- at %s%n", error.getMessage(), placeOf(error.getStackTrace()));
        }
        pw.printf("ReturnCode: %d", returnCode);
        log.info(RECAP_MARKER, sw.toString());
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

    /**
     * set error for step report
     *
     * @param error step error
     */
    public void error(Throwable error) {
        this.error = error;
    }
}
