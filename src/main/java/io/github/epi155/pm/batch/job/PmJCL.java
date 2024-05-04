package io.github.epi155.pm.batch.job;

import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

@Slf4j
class PmJCL implements JCL {
    private final ErrorCodeFactory factory;

    private PmJCL() {
        ServiceLoader<ErrorCodeProvider> loader = ServiceLoader.load(ErrorCodeProvider.class);
        this.factory = loader.findFirst().orElseGet(PmErrorCode::new).getInstance();
    }

    public static PmJCL getInstance() {
        return PmJCL.Helper.INSTANCE;
    }

    public JobStatus job(String name) {
        return PmJobStatus.of(rcOk(), this, name);
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
