/**
 * Package with classes to facilitate the development of job steps
 *
 * <p>Step example
 * <pre>
 *     public void run() {
 *         val src = SourceResource.bufferedReader(inFile);
 *         val snk = SinkResource.bufferedWriter(outFile);
 *         Pgm.from(src).into(snk).forEach(it -> it);
 *     }
 * </pre>
 */
package io.github.epi155.pm.batch.step;