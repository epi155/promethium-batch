package io.github.epi155.pm.batch.job;

import java.util.function.Function;

public interface LoopProc<S> {
    <P extends Iterable<Q>, Q> S forEachProc(P p, Function<Q, String> name, Proc<Q> proc);
}
