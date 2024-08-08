package io.github.epi155.test;

import io.github.epi155.pm.batch.step.Pgm;
import io.github.epi155.pm.batch.step.SinkResource;
import io.github.epi155.pm.batch.step.SourceResource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class TestBalance {
    @Test
    void testBa1() {
        List<String> income = Arrays.asList(
                "20030101,0000100",
                "20030101,0000150",
                "20030103,0000035",
                "20030105,0000086",
                "20030101,0000100");
        List<String> outcome = Arrays.asList(
                "20030101,0000100",
                "20030101,0000150",
                "20030103,0000040",
                "20030105,0000086",
                "20030101,0000100",
                "20030214,0000025");
        income.sort(String::compareTo);
        outcome.sort(String::compareTo);

        List<String> unbaInc = new ArrayList<>();
        List<String> unbaOutc = new ArrayList<>();

        val src1 = SourceResource.fromIterator(income);
        val src2 = SourceResource.fromIterator(outcome);
        SinkResource<?, String> snk1 = SinkResource.of(unbaInc::add);
        SinkResource<?, String> snk2 = SinkResource.of(unbaOutc::add);

        Pgm.from(src1, src2).into(snk1, snk2)
                .proceed((rd1, rd2, wr1, wr2) -> {
                    String da1 = rd1.get();
                    String da2 = rd2.get();
                    while (da1 != null || da2 != null) {
                        if (da1 == null) {
                            wr2.accept(da2);
                            da2 = rd2.get();
                        } else if (da2 == null) {
                            wr1.accept(da1);
                            da1 = rd1.get();
                        } else {
                            int cmp = da1.compareTo(da2);
                            if (cmp > 0) {
                                wr2.accept(da2);
                                da2 = rd2.get();
                            } else if (cmp < 0) {
                                wr1.accept(da1);
                                da1 = rd1.get();
                            } else {
                                da1 = rd1.get();
                                da2 = rd2.get();
                            }
                        }
                    }

                });
        log.info("ii: {}", unbaInc);
        log.info("oo: {}", unbaOutc);
    }

    @Test
    void testBa2() {
        List<Integer> unbaInc = new ArrayList<>();
        List<Integer> unbaOutc = new ArrayList<>();

        val src1 = SourceResource.fromStream(IntStream.range(1, 20).boxed());
        val src2 = SourceResource.fromStream(IntStream.range(0, 19).boxed());
        SinkResource<?, Integer> snk1 = SinkResource.of(unbaInc::add);
        SinkResource<?, Integer> snk2 = SinkResource.of(unbaOutc::add);

        Pgm.from(src1, src2).into(snk1, snk2)
                .proceed((rd1, rd2, wr1, wr2) -> {
                    Integer da1 = rd1.get();
                    Integer da2 = rd2.get();
                    while (da1 != null || da2 != null) {
                        if (da1 == null) {
                            wr2.accept(da2);
                            da2 = rd2.get();
                        } else if (da2 == null) {
                            wr1.accept(da1);
                            da1 = rd1.get();
                        } else {
                            int cmp = da1.compareTo(da2);
                            if (cmp > 0) {
                                wr2.accept(da2);
                                da2 = rd2.get();
                            } else if (cmp < 0) {
                                wr1.accept(da1);
                                da1 = rd1.get();
                            } else {
                                da1 = rd1.get();
                                da2 = rd2.get();
                            }
                        }
                    }

                });
        log.info("ii: {}", unbaInc);
        log.info("oo: {}", unbaOutc);
    }
}
