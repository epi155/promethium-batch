package io.github.epi155.pm.batch.fault;

import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

import static io.github.epi155.pm.batch.fault.Fixed.RC_ERR_STEP;

/**
 * batch wrapper exception
 */
@Getter
public class BatchException extends RuntimeException {
    private static final long serialVersionUID = -3072557291564118358L;
    /**
     * Error code associated with the exception
     */
    private final int returnCode;

    /**
     * batch exception constructor
     *
     * @param e exception wrapped
     */
    public BatchException(Throwable e) {
        super(e);
        returnCode = RC_ERR_STEP;
    }

    /**
     * batch exception constructor
     *
     * @param returnCode error code associated with the exception
     * @param format     error message pattern
     * @param objects    error message parameters
     */
    public BatchException(int returnCode, String format, Object... objects) {
        super(MessageFormatter.arrayFormat(format, objects).getMessage());
        this.returnCode = returnCode;
    }

    /**
     * batch exception constructor
     *
     * @param returnCode error code associated with the exception
     * @param t          exception wrapped
     * @param format     error message pattern
     * @param objects    error message parameters
     */
    public BatchException(int returnCode, Throwable t, String format, Object... objects) {
        super(MessageFormatter.arrayFormat(format, objects).getMessage(), t);
        this.returnCode = returnCode;
    }

}
