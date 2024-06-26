package io.github.epi155.pm.batch.job;

import java.util.Collection;
import java.util.ServiceLoader;

class PmJCL implements JCL {
    private final ValueFactory factory;

    private PmJCL() {
        ServiceLoader<ValueProvider> loader = ServiceLoader.load(ValueProvider.class);
        this.factory = loader.findFirst().orElseGet(PmValue::new).getInstance();
    }

    public static PmJCL getInstance() {
        return PmJCL.Helper.INSTANCE;
    }

    public JobStatus job(String name) {
        Class<?> claz = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        JobContext.matcher.set(new JobContext.MatchByLib(claz));
        return PmJob.of(rcOk(), this, name);
    }

    @Override
    public JobStatus job(String name, int w) {
        Class<?> claz = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        JobContext.matcher.set(new JobContext.MatchByPackagePrefix(claz, w));
        return PmJob.of(rcOk(), this, name);
    }

    @Override
    public JobStatus job(String name, Collection<String> prefixes) {
        JobContext.matcher.set(new JobContext.MatchByPackagePrefixes(prefixes));
        return PmJob.of(rcOk(), this, name);
    }

    @Override
    public int rcOk() {
        return factory.rcOk();
    }

    @Override
    public int rcWarning() {
        return factory.rcWarning();
    }

    @Override
    public int rcErrorStep() {
        return factory.rcErrorStep();
    }

    @Override
    public int rcErrorJob() {
        return factory.rcErrorJob();
    }

    @Override
    public int rcMax(int a, int b) {
        return factory.rcMax(a, b);
    }

    @Override
    public String jobName() {
        return factory.jobName();
    }

    @Override
    public String stepName() {
        return factory.stepName();
    }

    @Override
    public int rcErrorIO() {
        return factory.rcErrorIO();
    }

    @Override
    public int rcErrorSQL() {
        return factory.rcErrorSQL();
    }

    private static class Helper {
        private static final PmJCL INSTANCE = new PmJCL();
    }
}
