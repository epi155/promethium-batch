package io.github.epi155.pm.batch.fault;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.slf4j.helpers.MessageFormatter;

import static io.github.epi155.pm.batch.fault.Fixed.RC_ERR_STEP;

/**
 * batch step exception
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
        super(RC_ERR_STEP, e, "* Pgm Error> {}", e.getMessage());
    }

    /**
     * batch exception constructor
     *
     * @param format  error message pattern
     * @param objects error message parameters
     */
    public BatchStepException(String format, Object... objects) {
        super(RC_ERR_STEP, MessageFormatter.arrayFormat(format, objects).getMessage());
    }

    /**
     * Constructs the string with the location where the error occurred
     *
     * @param stackTrace full stack trace array
     * @return String with error position
     */
    @SneakyThrows
    public static String placeOf(StackTraceElement[] stackTrace) {
        val matcher = MatchContext.matcher.get();
        for (val ste : stackTrace) {
            if (!ste.isNativeMethod() && !"java.base".equals(ste.getModuleName())) {
                String claz = ste.getClassName();
                if (matcher == null || matcher.match(claz)) {
                    String meth = ste.getMethodName();
                    String file = ste.getFileName();
                    int line = ste.getLineNumber();
                    return String.format("%s->%s(%s:%d) [%s]", claz, meth, file, line, MatchContext.MatchByLib.libOf(claz));
                }
            }
        }
        return "N/A";
    }
}
