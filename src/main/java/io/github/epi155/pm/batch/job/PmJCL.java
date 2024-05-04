package io.github.epi155.pm.batch.job;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.*;

@Slf4j
class PmJCL implements JCL {
    private final ErrorCodeFactory factory;

    private PmJCL() {
        ServiceLoader<ErrorCodeProvider> loader = ServiceLoader.load(ErrorCodeProvider.class);
        this.factory = loader.findFirst().orElseGet(PmErrorCode::new).getInstance();
    }

    public static PmJCL getInstance() {
        return PmJCL.Helper.INSTANCE;
    }

    @Override
    public int rcOk() {
        return factory.rcOk();
    }

    @Override
    public int rcWarning() {
        return factory.rcWarning();
    }

    @Override
    public int rcErrorStep() {
        return factory.rcErrorStep();
    }

    @Override
    public int rcErrorJob() {
        return factory.rcErrorJob();
    }

    @Override
    public int rcErrorIO() {
        return factory.rcErrorIO();
    }

    @Override
    public int rcErrorSQL() {
        return factory.rcErrorSQL();
    }

    private JobStatus iefbr14() {
        return PmJobStatus.of(rcOk(), this);
    }

    public <P, C extends StatsCount> JobStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        return iefbr14().execPgm(p,c,pgm);
    }

    public <C extends StatsCount> JobStatus execPgm(C c, ToIntFunction<C> pgm) {
        return iefbr14().execPgm(c,pgm);
    }

    public <P, C extends StatsCount> JobStatus execPgm(P p, C c, BiConsumer<P, C> pgm) {
        return iefbr14().execPgm(p,c,pgm);
    }

    public <C extends StatsCount> JobStatus execPgm(C c, Consumer<C> pgm) {
        return iefbr14().execPgm(c,pgm);
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        return iefbr14().forkPgm(p,c,pgm);
    }

    @Override
    public <C extends StatsCount> JobStatus forkPgm(C c, ToIntFunction<C> pgm) {
        return iefbr14().forkPgm(c,pgm);
    }

    @Override
    public <P, C extends StatsCount> JobStatus forkPgm(P p, C c, BiConsumer<P, C> pgm) {
        return iefbr14().forkPgm(p,c,pgm);
    }

    @Override
    public <C extends StatsCount> JobStatus forkPgm(C c, Consumer<C> pgm) {
        return iefbr14().forkPgm(c,pgm);
    }

    private static class Helper {
        private static final PmJCL INSTANCE = new PmJCL();
    }
}
