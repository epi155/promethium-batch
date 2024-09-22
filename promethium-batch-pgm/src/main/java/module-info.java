/**
 * Utility module for handling utilities for batch processing
 */
module promethium.batch.pgm {
    exports io.github.epi155.pm.batch.pgm;

    requires static lombok;
    requires org.slf4j;
    requires transitive java.sql;
    requires promethium.batch.job;
}