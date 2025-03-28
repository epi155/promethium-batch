package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.fault.BatchException;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

import static io.github.epi155.pm.batch.fault.Fixed.RC_ERR_JOB;

/**
 * batch wrapper IO exception
 */
@Getter
class BatchJobException extends BatchException {

    /**
     * batch exception constructor
     *
     * @param format  error message pattern
     * @param objects error message parameters
     */
    public BatchJobException(String format, Object... objects) {
        super(RC_ERR_JOB, MessageFormatter.arrayFormat(format, objects).getMessage());
    }
}
