package io.github.epi155.pm.batch.job;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * class to define a procedure (sequence of programs)
 *
 * @param <P> type to specify the parameter class of the procedure
 */
public abstract class Proc<P> {
    /**
     * static constructor of a procedure with a parameter class
     * <p>example
     * <pre>
     * Proc&lt;Parm01&gt; proc01 = Proc.create((p, s) -> s
     *     .execPgm(p, "Step01", this::step01)
     *     .cond(0,NE)
     *         .execPgm(p, "Step02", this::step02)
     * );
     * </pre>
     *
     * @param route sequence of programs to call (internal job)
     * @param <Q>   parameter type
     * @return instance of {@link Proc} (the procedure)
     */
    public static <Q> Proc<Q> create(BiFunction<Q, JobStatus, JobStatus> route) {
        return new PmProc<>(route);
    }

    /**
     * static constructor of a procedure without a parameter class
     * <p>example
     * <pre>
     * Proc&lt;Void&gt; proc01 = Proc.create(s -> s
     *     .execPgm("Step01", this::step01)
     *     .cond(0,NE)
     *         .execPgm("Step02", this::step02)
     * );
     * </pre>
     *
     * @param route sequence of programs to call (internal job)
     * @return instance of {@link Proc} (the procedure)
     */
    public static Proc<Void> create(UnaryOperator<JobStatus> route) {
        return new PmProc<>(route);
    }

    /**
     * calls the procedure
     *
     * @param p procedure parameter (or {@code null})
     * @param s job in which to launch the procedure
     * @return procedure max return code
     */
    protected abstract int call(P p, JobStatus s);
}
