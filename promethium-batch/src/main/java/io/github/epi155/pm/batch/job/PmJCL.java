package io.github.epi155.pm.batch.job;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;

class PmJCL implements JCL {
    private final ValueFactory factory;

    private PmJCL() {
        ValueFactory wrkFactory = null;
        ServiceLoader<ValueProvider> loader = ServiceLoader.load(ValueProvider.class);
        Iterator<ValueProvider> iProvider = loader.iterator();
        while (iProvider.hasNext()) {
            ValueProvider provider = iProvider.next();
            wrkFactory = provider.getInstance();
            break;
        }
        this.factory = wrkFactory == null ? new PmValue().getInstance() : wrkFactory;
//        this.factory = loader.findFirst().orElseGet(PmValue::new).getInstance();
    }

    public static PmJCL getInstance() {
        return PmJCL.Helper.INSTANCE;
    }

    private static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(PmJCL.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste.getClassName();
            }
        }
        return null;
    }

    public JobStatus job(String name) {
//        Class<?> claz = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
//        JobContext.matcher.set(new JobContext.MatchByLib(claz));
        String className = getCallerClassName();
        JobContext.matcher.set(new JobContext.MatchByLib(className));
        return PmJob.of(rcOk(), this, name);
    }

    @Override
    public JobStatus job(String name, int w) {
//        Class<?> claz = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
//        JobContext.matcher.set(new JobContext.MatchByPackagePrefix(claz, w));
        String className = getCallerClassName();
        JobContext.matcher.set(new JobContext.MatchByPackagePrefix(className, w));
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
