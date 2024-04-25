package io.github.epi155.test;

import io.github.epi155.pm.batch.Loop;
import io.github.epi155.pm.batch.SinkResource;
import io.github.epi155.pm.batch.SourceResource;
import io.github.epi155.pm.batch.Tuple2;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
                Loop.from(src1)
                        .shutdownTimeout(1, TimeUnit.SECONDS)
                        .forEachParallel(10, it -> {
                            if (it == 64) {
                                throw new RuntimeException("BUG");
                            } else {
                                System.out.println(it);
                            }
                        }));
    }

    @org.junit.jupiter.api.Test
    void test04() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());

        Loop.from(src1)
                .shutdownTimeout(25, TimeUnit.MILLISECONDS)
                .forEachParallel(10, it -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
    }

    @org.junit.jupiter.api.Test
    void test05() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);

        Loop.from(src1).into(snk1)
                .shutdownTimeout(1, TimeUnit.SECONDS)
                .forEachParallel(10, it -> {
                    if (it == 1) {
                        try {
                            TimeUnit.MINUTES.sleep(10);
                        } catch (InterruptedException e) {
                            log.warn("Someone try to killed me !!!");
                            for (int k = 0; k < 3; k++) {
                                try {
                                    log.info("Hai Ho !!!");
                                    TimeUnit.MINUTES.sleep(1);
                                } catch (InterruptedException ex) {
                                    log.warn("Someone try again killed me !!!");
                                }
                            }
                            Thread.currentThread().interrupt();
                        } finally {
                            log.warn("killed !!!");
                        }
                    }
                    //wr.accept(it);
                    return it;
                });
        try {
            TimeUnit.MINUTES.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @org.junit.jupiter.api.Test
    void test06() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);
        Random rndm = new Random();

        ExecutorService exec = Executors.newCachedThreadPool();
        Loop.from(src1).into(snk1)
                .forEachAsync(10, it -> exec.submit(() -> {
                    TimeUnit.MILLISECONDS.sleep(rndm.nextInt(200));
                    return it;
                }));

    }

    @org.junit.jupiter.api.Test
    void test07() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        Random rndm = new Random();

        ExecutorService exec = Executors.newCachedThreadPool();
        Loop.from(src1)
                .forEachAsync(10, it -> exec.submit(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(rndm.nextInt(200));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(it);
                }, null));

    }
}
