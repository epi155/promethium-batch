/**
 * Utility module for handling utilities for batch processing
 */
module promethium.batch.step {
    exports io.github.epi155.pm.batch.step;

    requires static lombok;
    requires org.slf4j;
    requires transitive java.sql;
    requires promethium.batch.job;
}