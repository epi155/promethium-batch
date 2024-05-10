package io.github.epi155.pm.batch.job;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

class PmProc<P> extends Proc<P> {

    private final BiFunction<P, JobStatus, JobStatus> route;

    public PmProc(BiFunction<P, JobStatus, JobStatus> route) {
        this.route = route;
    }

    public PmProc(UnaryOperator<JobStatus> route) {
        this.route = (p, s) -> route.apply(s);
    }

    @Override
    protected int call(P p, JobStatus s) {
        return route.apply(p, s).complete();
    }
}
