package io.github.epi155.test;

import io.github.epi155.pm.batch.job.BatchException;
import io.github.epi155.pm.batch.step.Pgm;
import io.github.epi155.pm.batch.step.SinkResource;
import io.github.epi155.pm.batch.step.SourceResource;
import io.github.epi155.pm.batch.step.Tuple2;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
class TestEven {
    @org.junit.jupiter.api.Test
    void test01() {
        val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
        val snk1 = SinkResource.of(System.out::println);
        val snk2 = SinkResource.of(System.err::println);

        Pgm.from(src).into(snk1, snk2).forEach(
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

        Pgm.from(src).into(snk1, snk2).forEach(
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
                Pgm.from(src1)
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

        Pgm.from(src1)
                .shutdownTimeout(25, TimeUnit.MILLISECONDS)
                .forEachParallel(10, it -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
    }

//    @org.junit.jupiter.api.Test
//    void test05() {
//        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
//        val snk1 = SinkResource.of(System.out::println);
//
//        Batch.from(src1).into(snk1)
//                .shutdownTimeout(1, TimeUnit.SECONDS)
//                .forEachParallel(10, it -> {
//                    if (it == 1) {
//                        try {
//                            TimeUnit.MINUTES.sleep(10);
//                        } catch (InterruptedException e) {
//                            log.warn("Someone try to killed me !!!");
//                            for (int k = 0; k < 3; k++) {
//                                try {
//                                    log.info("Hai Ho !!!");
//                                    TimeUnit.MINUTES.sleep(1);
//                                } catch (InterruptedException ex) {
//                                    log.warn("Someone try again killed me !!!");
//                                }
//                            }
//                            Thread.currentThread().interrupt();
//                        } finally {
//                            log.warn("killed !!!");
//                        }
//                    }
//                    //wr.accept(it);
//                    return it;
//                });
//        try {
//            TimeUnit.MINUTES.sleep(5);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    @org.junit.jupiter.api.Test
    void test06() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);
        Random rndm = new Random();

        ExecutorService exec = Executors.newCachedThreadPool();
        Pgm.from(src1).into(snk1)
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
        Pgm.from(src1)
                .forEachAsync(10, it -> exec.submit(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(rndm.nextInt(200));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(it);
                }, null));

    }

    @org.junit.jupiter.api.Test
    void test08() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);
        Random rndm = new Random();

        ExecutorService exec = Executors.newCachedThreadPool();
        Pgm.from(src1).into(snk1)
                .forEachAsync(10, (it, wr) -> exec.submit(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(rndm.nextInt(200));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    wr.accept(it);
                }));
    }

    @org.junit.jupiter.api.Test
    void test09() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);
        Random rndm = new Random();

        ExecutorService exec = Executors.newCachedThreadPool();
        Assertions.assertThrows(BatchException.class, () -> Pgm.from(src1).into(snk1)
                .forEachAsync(10, (it, wr) -> exec.submit(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(rndm.nextInt(200));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (it == 64)
                        throw new IllegalStateException("64 is forbidden");
                    wr.accept(it);
                })));
    }

    @org.junit.jupiter.api.Test
    void test10() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);

        Assertions.assertThrows(BatchException.class, () ->
                Pgm.from(src1)
                        .map(it -> {
                            if (it == 64) throw new IllegalArgumentException("64");
                            return it * 2;
                        })
                        .into(snk1)
                        .forEachParallel(10, it -> it));
    }

    @org.junit.jupiter.api.Test
    void test11() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);

        Assertions.assertThrows(BatchException.class, () ->
                Pgm.from(src1)
                        .map(it -> {
                            if (it == 64) throw new IllegalArgumentException("64");
                            return it * 2;
                        })
                        .into(snk1)
                        .forEachParallel(10, (it, wr) -> wr.accept(it)));
    }

    @org.junit.jupiter.api.Test
    void test12() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);

        Assertions.assertThrows(BatchException.class, () ->
                Pgm.from(src1)
                        .map(it -> {
                            if (it == 64) throw new IllegalArgumentException("64");
                            return it * 2;
                        })
                        .into(snk1)
                        .forEachParallelFair(10, it -> it));
    }

    @org.junit.jupiter.api.Test
    void test13() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);
        ExecutorService exec = Executors.newCachedThreadPool();

        Assertions.assertThrows(BatchException.class, () ->
                Pgm.from(src1)
                        .map(it -> {
                            if (it == 64) throw new IllegalArgumentException("64");
                            return it * 2;
                        })
                        .into(snk1)
                        .forEachAsync(10, it -> exec.submit(() -> it)));
    }

    @org.junit.jupiter.api.Test
    void test14() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);
        ExecutorService exec = Executors.newCachedThreadPool();

        Assertions.assertThrows(BatchException.class, () ->
                Pgm.from(src1)
                        .map(it -> {
                            if (it == 64) throw new IllegalArgumentException("64");
                            return it * 2;
                        })
                        .into(snk1)
                        .forEachAsync(10, (it, wr) -> exec.submit(() -> wr.accept(it))));
    }

    @org.junit.jupiter.api.Test
    void test15() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);

        Assertions.assertThrows(BatchException.class, () ->
                Pgm.from(src1)
                        .map(it -> {
                            if (it == 64) throw new IllegalArgumentException("64");
                            return it * 2;
                        })
                        .into(snk1)
                        .forEach(it -> it));
    }

    @org.junit.jupiter.api.Test
    void test16() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());
        val snk1 = SinkResource.of(System.out::println);

        Assertions.assertThrows(BatchException.class, () ->
                Pgm.from(src1)
                        .map(it -> {
                            if (it == 64) throw new IllegalArgumentException("64");
                            return it * 2;
                        })
                        .into(snk1)
                        .forEach((it, wr) -> wr.accept(it)));
    }

    @org.junit.jupiter.api.Test
    void test17() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());

        Assertions.assertThrows(BatchException.class, () ->
                Pgm.from(src1)
                        .map(it -> {
                            if (it == 64) throw new IllegalArgumentException("64");
                            return it * 2;
                        })
                        .forEach(System.out::println));
    }

    @org.junit.jupiter.api.Test
    void test18() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());

        Assertions.assertThrows(BatchException.class, () ->
                Pgm.from(src1)
                        .map(it -> {
                            if (it == 64) throw new IllegalArgumentException("64");
                            return it * 2;
                        })
                        .forEachParallel(10, System.out::println));
    }

    @org.junit.jupiter.api.Test
    void test20() {
        val src1 = SourceResource.fromStream(IntStream.range(1, 100).boxed());

        Assertions.assertDoesNotThrow(() ->
                Pgm.from(src1)
                        .terminate(it -> it == 64)
                        .forEachParallel(10, System.out::println));
    }
}
