package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.fault.MatchContext;

import java.util.Collection;

import static io.github.epi155.pm.batch.fault.Fixed.RC_OK;

/**
 * root interface to launch the job control language
 */
public interface JCL {
    /**
     * Initialize job environment
     *
     * @param name jobName
     * @return instance of {@link JobStatus}
     */
    static JobStatus job(String name) {
        Class<?> claz = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        MatchContext.matcher.set(new MatchContext.MatchByLib(claz));
        return PmJob.of(RC_OK, name);
    }

    /**
     * Initialize job environment
     *
     * @param name jobName
     * @param w    number of namespace nodes to use to select the stacktrace
     * @return instance of {@link JobStatus}
     */
    static JobStatus job(String name, int w) {
        Class<?> claz = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        MatchContext.matcher.set(new MatchContext.MatchByPackagePrefix(claz, w));
        return PmJob.of(RC_OK, name);
    }

    /**
     * Initialize job environment
     *
     * @param name     jobName
     * @param prefixes namespace prefixes to use to select the stacktrace
     * @return instance of {@link JobStatus}
     */
    static JobStatus job(String name, Collection<String> prefixes) {
        MatchContext.matcher.set(new MatchContext.MatchByPackagePrefixes(prefixes));
        return PmJob.of(RC_OK, name);
    }
}
