package io.github.epi155.test;

import io.github.epi155.pm.batch.Loop;
import io.github.epi155.pm.batch.SinkResource;
import io.github.epi155.pm.batch.SourceResource;
import io.github.epi155.pm.batch.Tuple2;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;

import java.util.stream.IntStream;

@Slf4j
public class TestEven {
    @org.junit.jupiter.api.Test
    void test01() {
        val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
        val snk1 = SinkResource.of(System.out::println);
        val snk2 = SinkResource.of(System.err::println);

        Loop.from(src).into(snk1, snk2).forEach(
                it -> {
                    if (it % 2 == 0) {
                        return Tuple2.of(it, null);
                    } else {
                        return Tuple2.of(null, it);
                    }
                });
    }

    @org.junit.jupiter.api.Test
    void test02() {
        val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
        val snk1 = SinkResource.of(System.out::println);
        val snk2 = SinkResource.of(System.err::println);

        Loop.from(src).into(snk1, snk2).forEach(
                (it, wr1, wr2) -> {
                    if (it % 2 == 0) {
                        wr1.accept(it);
                    } else {
                        wr2.accept(it);
                    }
                });
    }

    @org.junit.jupiter.api.Test
    void test03() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());

        Assertions.assertThrows(RuntimeException.class, () ->
                Loop.from(src1).forEachParallel(10, it -> {
            if (it==64) {
                throw new RuntimeException("BUG");
            } else {
                System.out.println(it);
            }
        }));
    }

}
