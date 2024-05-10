package io.github.epi155.pm.batch.job;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public abstract class Proc<P> {
    public static <Q> Proc<Q> create(BiFunction<Q, JobStatus, JobStatus> route) {
        return new PmProc<>(route);
    }

    public static Proc<Void> create(UnaryOperator<JobStatus> route) {
        return new PmProc<>(route);
    }

    protected abstract int call(P p, JobStatus s);
}
