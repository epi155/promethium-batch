package io.github.epi155.test;

import io.github.epi155.pm.batch.step.BatchException;
import io.github.epi155.pm.batch.step.Pgm;
import io.github.epi155.pm.batch.step.SinkResource;
import io.github.epi155.pm.batch.step.SourceResource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
public class Test {
    @org.junit.jupiter.api.Test
    void test01() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        SourceResource<?, Integer> src = SourceResource.fromIterator(list);
        val snk = SinkResource.of(System.out::println);

        Pgm.from(src).into(snk).forEach(
                it -> it * it);
        System.out.println("Done");

//        val sink = SinkResource.of(() -> Files.newBufferedWriter(Path.of("foo.txt")), (wr, s) -> wr::write);
    }

    void test10() {
        SourceResource<BufferedReader, String> src = SourceResource.fromSupplier(
                () -> {
                    try {
                        return Files.newBufferedReader(Path.of("foo-in.txt"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                br -> () -> {
                    try {
                        return br.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        val snk = SinkResource.of(
                () -> {
                    try {
                        return Files.newBufferedWriter(Path.of("foo-out.txt"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                (BufferedWriter bw, String s) -> {
                    try {
                        bw.write(s);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        Assertions.assertThrows(RuntimeException.class, () -> {
            Pgm.from(src).into(snk).forEach(it -> it);
        });

        System.out.println("Done");
    }

    @org.junit.jupiter.api.Test
    void test42() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
        SourceResource<?, Integer> src = SourceResource.fromIterator(list);
        Queue<Integer> out = new ArrayBlockingQueue<Integer>(5);
        val snk = SinkResource.of(out::add);
        /*
         * errore scrittura
         * 1. il monitor rileva l'errore e intettomple il thread principale
         * 2. lo stato di interupt viene rilevato dal semaforo, il metodo di start dei task resituisce un Future nullo
         * 3. il Future nullo interrompe il loop di sottomissione dei task
         * 4. alla chiusura del writer viene nuovamente rilevato lo stato di errore e lanciate la corretta eccezione nel
         *    thread principale
         */
        Assertions.assertThrows(BatchException.class, () -> {
            Pgm.from(src).into(snk).forEachParallel(2,
                    (i, w) -> w.accept(i * i));
        });
        System.out.println("Done");
    }

//    @org.junit.jupiter.api.Test
//    void test05() throws InterruptedException {
//        log.info("----- start");
//        for(int k=1; k<10_000;  k++) {
//            int j = k;
//            int n = 0;
//            while (j != 0) {
//                if ((j & 0x01) == 1) n++;
//                j >>= 1;
//            }
//            if (n==1 || (k & 0x1fff) == 0) log.info("**** waiting for writers idle {} ...", k);
//            TimeUnit.MILLISECONDS.sleep(5);
//        }
//        log.info("----- end");
//    }

    @org.junit.jupiter.api.Test
    void test43() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
        SourceResource<?, Integer> src = SourceResource.fromIterator(list);
        val snk = SinkResource.of(it -> {
        });
        Assertions.assertThrows(BatchException.class, () -> Pgm
                .from(src).into(snk).forEachParallel(4,
                        (i, w) -> {
                            if (i > 10)
                                throw new RuntimeException("Too big");
                            w.accept(i * i);
                        }));
        System.out.println("Done");
    }

    @org.junit.jupiter.api.Test
    void test51() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
        SourceResource<?, Integer> src = SourceResource.fromIterator(list);
        val snk = SinkResource.of(it -> {
        });
        Assertions.assertThrows(BatchException.class, () -> Pgm
                .from(src).into(snk).forEachParallelFair(4,
                        i -> {
                            if (i > 10) {
                                log.warn("trouble");
                                throw new RuntimeException("Too big");
                            }
                            return i * i;
                        }));
        System.out.println("Done");
    }

    @org.junit.jupiter.api.Test
    void test52() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
        SourceResource<?, Integer> src = SourceResource.fromIterator(list);
        val snk = SinkResource.of(it -> {
        });
        Assertions.assertDoesNotThrow(() -> Pgm
                .from(src).into(snk).forEachParallel(4,
                        i -> i * i));
        System.out.println("Done");
    }

    interface Dummy<I> extends AutoCloseable, Iterable<I> {
    }


}
