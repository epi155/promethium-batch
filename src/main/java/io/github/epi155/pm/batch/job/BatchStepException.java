package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.step.BatchException;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

/**
 * batch wrapper IO exception
 */
@Getter
public class BatchStepException extends BatchException {
    private static final long serialVersionUID = -2689268030962162405L;

	/**
     * batch exception constructor
     *
     * @param e exception wrapped
     */
    public BatchStepException(Exception e) {
        super(PmJCL.getInstance().rcErrorStep(), e, "* Pgm Error> {}", e.getMessage());
    }

    /**
     * batch exception constructor
     *
     * @param format  error message pattern
     * @param objects error message parameters
     */
    public BatchStepException(String format, Object... objects) {
        super(PmJCL.getInstance().rcErrorStep(), MessageFormatter.arrayFormat(format, objects).getMessage());
    }
}
