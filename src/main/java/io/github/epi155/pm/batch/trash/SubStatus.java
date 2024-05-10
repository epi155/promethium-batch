package io.github.epi155.pm.batch.trash;

import io.github.epi155.pm.batch.job.StatsCount;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public interface SubStatus<Q> {
    boolean isSuccess();

    Optional<Integer> returnCode(String stepName);

    SubStatus<Q> push();

    SubStatus<Q> pop();

    SubStatus<Q> peek();

    <C extends StatsCount> SubStatus<Q> execPgm(C c, BiFunction<Q, C, Integer> pgm);

    <C extends StatsCount> SubStatus<Q> execPgm(C c, BiConsumer<Q, C> pgm);

    SubStatus<Q> execPgm(String stepName, ToIntFunction<Q> pgm);

    SubStatus<Q> execPgm(String stepName, Consumer<Q> pgm);

    <C extends StatsCount> SubStatus<Q> nextPgm(C c, BiFunction<Q, C, Integer> pgm);

    <C extends StatsCount> SubStatus<Q> nextPgm(C c, BiConsumer<Q, C> pgm);

    SubStatus<Q> nextPgm(String stepName, ToIntFunction<Q> pgm);

    SubStatus<Q> nextPgm(String stepName, Consumer<Q> pgm);

    <C extends StatsCount> SubStatus<Q> elsePgm(C c, BiFunction<Q, C, Integer> pgm);

    <C extends StatsCount> SubStatus<Q> elsePgm(C c, BiConsumer<Q, C> pgm);

    SubStatus<Q> elsePgm(String stepName, ToIntFunction<Q> pgm);

    SubStatus<Q> elsePgm(String stepName, Consumer<Q> pgm);
}
