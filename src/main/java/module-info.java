import io.github.epi155.pm.batch.job.ErrorCodeProvider;

/**
 * Utility module for handling utilities for batch processing
 */
module promethium.batch {
    uses ErrorCodeProvider;
    exports io.github.epi155.pm.batch.step;
    exports io.github.epi155.pm.batch.job;

    requires org.jetbrains.annotations;
    requires static lombok;
    requires org.slf4j;
    requires java.sql;
}