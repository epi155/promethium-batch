package io.github.epi155.pm.batch.job;

import java.io.PrintWriter;

/**
 * Minimal implementation of statistics counter
 */
public class BareCount extends StatsCount {
    /**
     * class constructor
     *
     * @param name step custom name
     */
    protected BareCount(String name) {
        super(name);
    }

    @Override
    protected void recap(PrintWriter pw) {
        // noop
    }
}
