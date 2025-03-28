/**
 * Utility module for handling utilities for batch processing
 */
module promethium.batch.fault {
    uses io.github.epi155.pm.batch.fault.ValueProvider;
    exports io.github.epi155.pm.batch.fault;

    requires static lombok;
    requires org.slf4j;
    requires transitive java.sql;
}
