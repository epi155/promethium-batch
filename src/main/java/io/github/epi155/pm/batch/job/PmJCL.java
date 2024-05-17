package io.github.epi155.pm.batch.job;

import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

@Slf4j
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
