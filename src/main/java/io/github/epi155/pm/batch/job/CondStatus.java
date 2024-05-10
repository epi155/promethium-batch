package io.github.epi155.pm.batch.job;

/**
 * interface that contains the operations that can be performed after a condition code
 *
 * @param <T> class to operate on
 */
public interface CondStatus<T> extends ExecPgm<T>, LoopPgm<T>, ExecProc<T> {
}
