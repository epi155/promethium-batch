package io.github.epi155.pm.batch.fault;

import java.util.ServiceLoader;
import java.util.function.IntBinaryOperator;

public class Fixed {
    private Fixed() {}
    public static final int RC_OK;
    public static final int RC_WARN;
    public static final int RC_ERR_IO;
    public static final int RC_ERR_SQL;
    public static final int RC_ERR_STEP;
    public static final int RC_ERR_JOB;

    public static final String STEP_NAME;
    public static final String JOB_NAME;

    public static final IntBinaryOperator MAX_CC;

    static {
        ServiceLoader<ValueProvider> loader = ServiceLoader.load(ValueProvider.class);
        ValueFactory factory = loader.findFirst().orElseGet(HostValue::new).getInstance();

        RC_OK = factory.rcOk();
        RC_WARN = factory.rcWarning();
        RC_ERR_IO = factory.rcErrorIO();
        RC_ERR_SQL = factory.rcErrorSQL();
        RC_ERR_STEP = factory.rcErrorStep();
        RC_ERR_JOB = factory.rcErrorJob();

        STEP_NAME = factory.stepName();
        JOB_NAME = factory.jobName();

        MAX_CC = factory::rcMax;
    }

    private static class HostValue implements ValueProvider {
        @Override
        public ValueFactory getInstance() {
            return new ValueFactory() {
                @Override
                public int rcOk() {
                    return 0;
                }

                @Override
                public int rcWarning() {
                    return 4;
                }

                @Override
                public int rcErrorIO() {
                    return 8;
                }

                @Override
                public int rcErrorSQL() {
                    return 12;
                }

                @Override
                public int rcErrorStep() {
                    return 16;
                }

                @Override
                public int rcErrorJob() {
                    return 20;
                }
            };
        }
    }
}
