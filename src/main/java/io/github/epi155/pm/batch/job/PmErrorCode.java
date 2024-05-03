package io.github.epi155.pm.batch.job;

class PmErrorCode implements ErrorCodeProvider {
    @Override
    public ErrorCodeFactory getInstance() {
        return new ErrorCodeFactory() {
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
