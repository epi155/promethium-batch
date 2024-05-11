package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.step.BatchException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;

/**
 * batch wrapper IO exception
 */
@Getter
@Slf4j
public class BatchIOException extends BatchException {
    /**
     * batch exception constructor
     *
     * @param e exception wrapped
     */
    public BatchIOException(IOException e) {
        super(PmJCL.getInstance().rcErrorIO(), e, "* IO Error> {}", e.getMessage());
    }

    /**
     * batch exception constructor
     *
     * @param format     error message pattern
     * @param objects    error message parameters
     */
    public BatchIOException(String format, Object... objects) {
        super(PmJCL.getInstance().rcErrorIO(), MessageFormatter.arrayFormat(format, objects).getMessage());
    }
}
