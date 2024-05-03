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
class PmStepStatus implements StepStatus {
    @Accessors(fluent = true)
    private int returnCode;

    public <P, C extends StepCount> StepStatus nextPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (isSuccess()) {
            return JCL.getInstance().execPgm(p, c, pgm);
        } else {
            return this;
        }
    }

    public <C extends StepCount> StepStatus nextPgm(C c, ToIntFunction<C> pgm) {
        if (isSuccess()) {
            return JCL.getInstance().execPgm(c, pgm);
        } else {
            return this;
        }
    }

    public <P, C extends StepCount> StepStatus nextPgm(P p, C c, BiConsumer<P, C> pgm) {
        if (isSuccess()) {
            return JCL.getInstance().execPgm(p, c, pgm);
        } else {
            return this;
        }
    }

    public <C extends StepCount> StepStatus nextPgm(C c, Consumer<C> pgm) {
        if (isSuccess()) {
            return JCL.getInstance().execPgm(c, pgm);
        } else {
            return this;
        }
    }

    public StepStatus map(@NotNull UnaryOperator<StepStatus> map) {
        return map.apply(this);
    }

    @Override
    public boolean isSuccess() {
        if (OK.returnCode() <= WARN.returnCode()) {
            return OK.returnCode() <= returnCode && returnCode <= WARN.returnCode();
        } else {
            return OK.returnCode() >= returnCode && returnCode >= WARN.returnCode();
        }
    }

    @Override
    public StepStatus join() {
        return PmJCL.getInstance().join(this);
    }

    @Override
    public <P, C extends StepCount> StepStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        return JCL.getInstance().execPgm(p, c, pgm);
    }

    @Override
    public <C extends StepCount> StepStatus execPgm(C c, ToIntFunction<C> pgm) {
        return JCL.getInstance().execPgm(c, pgm);
    }

    @Override
    public <P, C extends StepCount> StepStatus execPgm(P p, C c, BiConsumer<P, C> pgm) {
        return JCL.getInstance().execPgm(p, c, pgm);
    }

    @Override
    public <C extends StepCount> StepStatus execPgm(C c, Consumer<C> pgm) {
        return JCL.getInstance().execPgm(c, pgm);
    }

    @Override
    public <P, C extends StepCount> StepStatus elsePgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (!isSuccess()) {
            return JCL.getInstance().execPgm(p, c, pgm);
        } else {
            return this;
        }
    }

    @Override
    public <C extends StepCount> StepStatus elsePgm(C c, ToIntFunction<C> pgm) {
        if (!isSuccess()) {
            return JCL.getInstance().execPgm(c, pgm);
        } else {
            return this;
        }
    }

    @Override
    public <P, C extends StepCount> StepStatus elsePgm(P p, C c, BiConsumer<P, C> pgm) {
        if (!isSuccess()) {
            return JCL.getInstance().execPgm(p, c, pgm);
        } else {
            return this;
        }
    }

    @Override
    public <C extends StepCount> StepStatus elsePgm(C c, Consumer<C> pgm) {
        if (!isSuccess()) {
            return JCL.getInstance().execPgm(c, pgm);
        } else {
            return this;
        }
    }

    @Override
    public <P, C extends StepCount> StepStatus forkPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        return JCL.getInstance().forkPgm(p, c, pgm);
    }

    @Override
    public <C extends StepCount> StepStatus forkPgm(C c, ToIntFunction<C> pgm) {
        return JCL.getInstance().forkPgm(c, pgm);
    }

    @Override
    public <P, C extends StepCount> StepStatus forkPgm(P p, C c, BiConsumer<P, C> pgm) {
        return JCL.getInstance().forkPgm(p, c, pgm);
    }

    @Override
    public <C extends StepCount> StepStatus forkPgm(C c, Consumer<C> pgm) {
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
