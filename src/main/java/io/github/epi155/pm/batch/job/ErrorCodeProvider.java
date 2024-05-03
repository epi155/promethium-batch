package io.github.epi155.pm.batch.job;

/**
 * Error Code SPI
 */
public interface ErrorCodeProvider {
    /**
     * Error code factory
     *
     * @return error code factory
     */
    ErrorCodeFactory getInstance();
}
