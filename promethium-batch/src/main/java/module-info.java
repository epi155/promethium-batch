import io.github.epi155.pm.batch.job.ValueProvider;

/**
 * Utility module for handling utilities for batch processing
 */
module promethium.batch {
    uses ValueProvider;
    exports io.github.epi155.pm.batch.step;
    exports io.github.epi155.pm.batch.job;

    requires static lombok;
    requires org.slf4j;
    requires transitive java.sql;
}