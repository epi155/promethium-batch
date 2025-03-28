/**
 * Utility module for handling utilities for batch processing
 */
module promethium.batch.core {
    exports io.github.epi155.pm.batch.core;

    requires static lombok;
    requires org.slf4j;
    requires transitive java.sql;
    requires transitive promethium.batch.fault;
}
