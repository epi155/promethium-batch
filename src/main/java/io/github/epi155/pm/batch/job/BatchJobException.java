package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.step.BatchException;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

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
        super(PmJCL.getInstance().rcErrorJob(), MessageFormatter.arrayFormat(format, objects).getMessage());
    }
}
