/**
 * Package with classes to create jobs and coordinate job steps
 *
 * <p>Job example
 * <pre>
 * public Integer call() {
 *     return Job.create("job01")
 *         .execPgm("step01", step01::run)
 *         .complete();
 * }
 * </pre>
 */
package io.github.epi155.pm.batch.job;