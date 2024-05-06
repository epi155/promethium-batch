package io.github.epi155.pm.batch.job;

class PmValue implements ValueProvider {
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

            @Override
            public String jobName() {
                return "jobName";
            }

            @Override
            public String stepName() {
                return "stepName";
            }
        };
    }
}
