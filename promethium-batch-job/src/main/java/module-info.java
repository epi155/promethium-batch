import io.github.epi155.pm.batch.job.ValueProvider;

/**
 * Utility module for handling utilities for batch processing
 */
module promethium.batch.job {
    exports io.github.epi155.pm.batch.job;
    uses ValueProvider;

    requires static lombok;
    requires org.slf4j;
    requires transitive java.sql;
}