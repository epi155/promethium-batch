package io.github.epi155.pm.batch.job;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

@Getter
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
class PmJobStatus implements JobStatus {
    @Accessors(fluent = true)
    private int returnCode;
    private final ErrorCodeFactory factory;

    public <P, C extends StepCount> JobStatus nextPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (isSuccess()) {
            return JCL.getInstance().execPgm(p, c, pgm);
        } else {
            return this;
        }
    }

    public <C extends StepCount> JobStatus nextPgm(C c, ToIntFunction<C> pgm) {
        if (isSuccess()) {
            return JCL.getInstance().execPgm(c, pgm);
        } else {
            return this;
        }
    }

    public <P, C extends StepCount> JobStatus nextPgm(P p, C c, BiConsumer<P, C> pgm) {
        if (isSuccess()) {
            return JCL.getInstance().execPgm(p, c, pgm);
        } else {
            return this;
        }
    }

    public <C extends StepCount> JobStatus nextPgm(C c, Consumer<C> pgm) {
        if (isSuccess()) {
            return JCL.getInstance().execPgm(c, pgm);
        } else {
            return this;
        }
    }

    public JobStatus map(@NotNull UnaryOperator<JobStatus> map) {
        return map.apply(this);
    }

    @Override
    public boolean isSuccess() {
        return factory.rcOk() <= returnCode && returnCode <= factory.rcWarning();
    }

    @Override
    public JobStatus join() {
        return PmJCL.getInstance().join(this);
    }

    @Override
    public JobStatus push() {
        PmJCL.getInstance().push(returnCode);
        return this;
    }

    @Override
    public JobStatus pop() {
        PmJCL.getInstance().pop().ifPresent(it -> returnCode = it);
        return this;
    }

    @Override
    public <P, C extends StepCount> JobStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        return JCL.getInstance().execPgm(p, c, pgm);
    }

    @Override
    public <C extends StepCount> JobStatus execPgm(C c, ToIntFunction<C> pgm) {
        return JCL.getInstance().execPgm(c, pgm);
    }

    @Override
    public <P, C extends StepCount> JobStatus execPgm(P p, C c, BiConsumer<P, C> pgm) {
        return JCL.getInstance().execPgm(p, c, pgm);
    }

    @Override
    public <C extends StepCount> JobStatus execPgm(C c, Consumer<C> pgm) {
        return JCL.getInstance().execPgm(c, pgm);
    }

    @Override
    public <P, C extends StepCount> JobStatus elsePgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (!isSuccess()) {
            return JCL.getInstance().execPgm(p, c, pgm);
        } else {
            return this;
        }
    }

    @Override
    public <C extends StepCount> JobStatus elsePgm(C c, ToIntFunction<C> pgm) {
        if (!isSuccess()) {
            return JCL.getInstance().execPgm(c, pgm);
        } else {
            return this;
        }
    }

    @Override
    public <P, C extends StepCount> JobStatus elsePgm(P p, C c, BiConsumer<P, C> pgm) {
        if (!isSuccess()) {
            return JCL.getInstance().execPgm(p, c, pgm);
        } else {
            return this;
        }
    }

    @Override
    public <C extends StepCount> JobStatus elsePgm(C c, Consumer<C> pgm) {
        if (!isSuccess()) {
            return JCL.getInstance().execPgm(c, pgm);
        } else {
            return this;
        }
    }

    @Override
    public <P, C extends StepCount> JobStatus forkPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        return JCL.getInstance().forkPgm(p, c, pgm);
    }

    @Override
    public <C extends StepCount> JobStatus forkPgm(C c, ToIntFunction<C> pgm) {
        return JCL.getInstance().forkPgm(c, pgm);
    }

    @Override
    public <P, C extends StepCount> JobStatus forkPgm(P p, C c, BiConsumer<P, C> pgm) {
        return JCL.getInstance().forkPgm(p, c, pgm);
    }

    @Override
    public <C extends StepCount> JobStatus forkPgm(C c, Consumer<C> pgm) {
        return JCL.getInstance().forkPgm(c, pgm);
    }

    @Override
    public <P, C extends StepCount> KeepStatus onSuccess(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (isSuccess()) {
            return PmKeepStatus.of(this, JCL.getInstance().execPgm(p, c, pgm));
        } else {
            return PmKeepStatus.of(this, null);
        }
    }

    @Override
    public <C extends StepCount> KeepStatus onSuccess(C c, ToIntFunction<C> pgm) {
        if (isSuccess()) {
            return PmKeepStatus.of(this, JCL.getInstance().execPgm(c, pgm));
        } else {
            return PmKeepStatus.of(this, null);
        }
    }

    @Override
    public <P, C extends StepCount> KeepStatus onSuccess(P p, C c, BiConsumer<P, C> pgm) {
        if (isSuccess()) {
            return PmKeepStatus.of(this, JCL.getInstance().execPgm(p, c, pgm));
        } else {
            return PmKeepStatus.of(this, null);
        }
    }

    @Override
    public <C extends StepCount> KeepStatus onSuccess(C c, Consumer<C> pgm) {
        if (isSuccess()) {
            return PmKeepStatus.of(this, JCL.getInstance().execPgm(c, pgm));
        } else {
            return PmKeepStatus.of(this, null);
        }
    }
}
