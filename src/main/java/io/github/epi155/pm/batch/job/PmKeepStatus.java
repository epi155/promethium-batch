package io.github.epi155.pm.batch.job;

import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

@AllArgsConstructor(staticName = "of")
class PmKeepStatus implements KeepStatus {
    private final JobStatus mainStatus;
    private JobStatus lastStatus;

    @Override
    public <P, C extends StepCount> JobStatus orFailure(P p, C c, BiFunction<P, C, Integer> pgm) {
        if (!mainStatus.isSuccess()) {
            lastStatus = JCL.getInstance().execPgm(p, c, pgm);
        }
        return mainStatus;
    }

    @Override
    public <C extends StepCount> JobStatus orFailure(C c, ToIntFunction<C> pgm) {
        if (!mainStatus.isSuccess()) {
            lastStatus = JCL.getInstance().execPgm(c, pgm);
        }
        return mainStatus;
    }

    @Override
    public <P, C extends StepCount> JobStatus orFailure(P p, C c, BiConsumer<P, C> pgm) {
        if (!mainStatus.isSuccess()) {
            lastStatus = JCL.getInstance().execPgm(p, c, pgm);
        }
        return mainStatus;
    }

    @Override
    public <C extends StepCount> JobStatus orFailure(C c, Consumer<C> pgm) {
        if (!mainStatus.isSuccess()) {
            lastStatus = JCL.getInstance().execPgm(c, pgm);
        }
        return mainStatus;
    }
}
