package io.github.epi155.pm.batch.job;

public interface ExecProc<S> {
    <P> S execProc(P p, String procName, Proc<P> proc);

    S execProc(String procName, Proc<Void> proc);
}
