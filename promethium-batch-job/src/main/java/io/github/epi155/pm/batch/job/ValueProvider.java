package io.github.epi155.pm.batch.job;

/**
 * Error Code SPI
 */
public interface ValueProvider {
    /**
     * Error code factory
     *
     * @return error code factory
     */
    ValueFactory getInstance();
}
