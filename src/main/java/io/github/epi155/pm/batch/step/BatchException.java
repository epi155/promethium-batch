package io.github.epi155.pm.batch.step;

import io.github.epi155.pm.batch.job.JCL;
import lombok.Getter;
import lombok.val;
import org.slf4j.helpers.MessageFormatter;

/**
 * batch wrapper exception
 */
@Getter
public class BatchException extends RuntimeException {
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
        returnCode = JCL.getInstance().rcErrorStep();
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

    /**
     * Constructs the string with the location where the error occurred
     *
     * @param stackTrace full stack trace array
     * @return String with error position
     */
    public static String placeOf(StackTraceElement[] stackTrace) {
        for (val ste : stackTrace) {
            if (!ste.isNativeMethod() && !"java.base".equals(ste.getModuleName())) {
                String claz = ste.getClassName();
                String meth = ste.getMethodName();
                String file = ste.getFileName();
                int line = ste.getLineNumber();
                return String.format("%s->%s(%s:%d)", claz, meth, file, line);
            }
        }
        return "N/A";
    }
}
