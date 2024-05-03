package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.step.BatchException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
}
