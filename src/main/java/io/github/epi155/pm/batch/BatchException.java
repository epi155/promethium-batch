package io.github.epi155.pm.batch;

import lombok.val;

/**
 * quid wrapper exception
 */
public class BatchException extends RuntimeException {
    /**
     * quid exception constructor
     *
     * @param e exception wrapped
     */
    public BatchException(Throwable e) {
        super(e);
    }

    static String placeOf(StackTraceElement[] stackTrace) {
        for (val ste : stackTrace) {
            if (ste.isNativeMethod() || "java.base".equals(ste.getModuleName()))
                continue;
            String claz = ste.getClassName();
            String meth = ste.getMethodName();
            String file = ste.getFileName();
            int line = ste.getLineNumber();
            return String.format("%s->%s(%s:%d)", claz, meth, file, line);
        }
        return "N/A";
    }
}
