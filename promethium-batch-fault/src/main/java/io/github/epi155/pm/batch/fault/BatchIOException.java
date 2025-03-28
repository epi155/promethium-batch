package io.github.epi155.pm.batch.fault;

import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;

import static io.github.epi155.pm.batch.fault.Fixed.RC_ERR_IO;

/**
 * batch wrapper IO exception
 */
@Getter
public class BatchIOException extends BatchException {
    private static final long serialVersionUID = -2175976057814817139L;

    /**
     * batch exception constructor
     *
     * @param e exception wrapped
     */
    public BatchIOException(IOException e) {
        super(RC_ERR_IO, e, "* IO Error> {}", e.getMessage());
    }

    /**
     * batch exception constructor
     *
     * @param format  error message pattern
     * @param objects error message parameters
     */
    public BatchIOException(String format, Object... objects) {
        super(RC_ERR_IO, MessageFormatter.arrayFormat(format, objects).getMessage());
    }
}
