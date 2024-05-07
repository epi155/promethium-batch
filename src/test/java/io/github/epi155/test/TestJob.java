package io.github.epi155.test;

import io.github.epi155.pm.batch.job.BatchIOException;
import io.github.epi155.pm.batch.job.JCL;
import io.github.epi155.pm.batch.job.StatsCount;
import io.github.epi155.pm.batch.step.Pgm;
import io.github.epi155.pm.batch.step.SinkResource;
import io.github.epi155.pm.batch.step.SourceResource;
import io.github.epi155.pm.batch.step.Tuple2;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
class TestJob {

    @Test
    void job01() {
        MyCount count1 = new MyCount("Step01");
        MyCount count2 = new MyCount("Step02");
        MyCount count3 = new MyCount("Step03");
        MyCount count4 = new MyCount("Step04");
        MyCount count5 = new MyCount("Step05");
        int x = JCL.getInstance().job("Job01")
                .forkExecPgm(count1, this::step01)
                .forkExecPgm(count2, this::step01)
                .execPgm(count3, this::step01)
                .join()
                .push() // save rc
                .nextPgm(count4, this::step02) // execute if ok
                .peek()
                .elsePgm(count5, this::step01) // execute in ko
                .pop()
                .complete();
        log.info("Job returnCode: {}", x);
    }

    @Test
    void job02() {
        MyCount count1 = new MyCount("Step01");
        MyCount count2 = new MyCount("Step02");
        MyCount count3 = new MyCount("Step03");
        MyCount count4 = new MyCount("Step04");
        MyCount count5 = new MyCount("Step05");
        int rc = JCL.getInstance().job("Job01")
                .forkExecProc("Proc01", it -> it
                        .execPgm(count1, this::step01)
                        .nextPgm(count2, this::step01)
                )
                .execPgm(count3, this::step01)
                .join()
                .push() // save rc
                .nextPgm(count4, this::step02) // execute if ok
                .peek()
                .elsePgm(count5, this::step01) // execute in ko
                .pop()
                .complete();
        log.info("Job returnCode: {}", rc);
    }

    @Test
    void job03() {
        MyCount count1 = new MyCount("Step01");
        MyCount count2 = new MyCount("Step02");
        MyCount count3 = new MyCount("Step03");
        MyCount count4 = new MyCount("Step04");
        MyCount count5 = new MyCount("Step05");
        int rc = JCL.getInstance().job("Job01")
                .execProc("Proc01", it -> it
                        .execPgm(count1, this::step01)
                        .nextPgm(count2, this::step01)
                )
                .execPgm(count3, this::step01)
                .join()
                .push() // save rc
                .nextPgm(count4, this::step02) // execute if ok
                .peek()
                .elsePgm(count5, this::step01) // execute in ko
                .pop()
                .complete();
        log.info("Job returnCode: {}", rc);
    }
    @Test
    void job04() {
        int rc = JCL.getInstance().job("Job04")
                .execPgm(new MyCount("Step03e"), this::step03e)
                .nextPgm(new MyCount("Step03s"), this::step03s)
                .nextPgm(new MyCount("Step03w"), this::step03w)
                .complete();
        log.info("Job returnCode: {}", rc);
    }
    @Test
    void job05() {
        List<String> ls = new ArrayList<>();
        int rc = JCL.getInstance().job("JobPk")
                .execPgm("lock", this::step00)
                .nextPgm(ls, new MyCount("list"), this::step04)
//                .nextLoopProc(ls, )
                .nextLoopPgm(ls, MyCount::new, this::step05)
                .push()
                .exec(s -> s.returnCode("lock")
                        .filter(rcLock -> (rcLock == 0))
                        .ifPresent(rcLock -> s.execPgm(new MyCount("unlock"), this::step01)))
                .pop()
                .complete();
        log.info("Job returnCode: {}", rc);
    }
    @Test
    void job06() {
        MyCount count1 = new MyCount("Step01");
        MyCount count2 = new MyCount("Step02");
        MyCount count3 = new MyCount("Step03");
        int rc = JCL.getInstance().job("job06")
                .execPgm(count1, this::step01)
                .exec(s -> {
                    if (s.isSuccess())
                        s.execPgm(count2, this::step02);
                    else
                        s.execPgm(count3, this::step01);
                })
                .complete();
        log.info("Job returnCode: {}", rc);
    }

    private void step00() {
    }

    private void step04(List<String> cs, MyCount cnt) {
        cs.addAll(List.of("eins", "zwei", "drei"));
    }
    private void step05(MyCount cnt) {
    }

    private void step01(MyCount c) {
        val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
        val snk1 = SinkResource.of(System.out::println, c::incEven);
        val snk2 = SinkResource.of(System.err::println, c::incOdd);
        Random rndm = new Random();

        Pgm.from(src).before(c::incRead).into(snk1, snk2).forEach(
                (it, wr1, wr2) -> {
                    if (it % 2 == 0) {
                        wr1.accept(it);
                    } else {
                        wr2.accept(it);
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(rndm.nextInt(200));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
    }
    private void step03w(MyCount c) {
        val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
        val snk1 = SinkResource.of(it -> log.info("{} is even", it), c::incEven);
        val snk2 = SinkResource.of(it -> log.info("{} is odd", it), c::incOdd);
        Random rndm = new Random();

        Pgm.from(src).before(c::incRead).into(snk1, snk2).forEachParallel(5,
                (it, wr1, wr2) -> {
                    log.info("Hi {}", it);
                    if (it % 2 == 0) {
                        wr1.accept(it);
                    } else {
                        wr2.accept(it);
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(rndm.nextInt(200));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
    }
    private void step03s(MyCount c) {
        val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
        val snk1 = SinkResource.of(it -> log.info("{} is even", it), c::incEven);
        val snk2 = SinkResource.of(it -> log.info("{} is odd", it), c::incOdd);
        Random rndm = new Random();

        Pgm.from(src).before(c::incRead).into(snk1, snk2).forEachParallelFair(5,
                (it) -> {
                    log.info("Hi {}", it);
                    try {
                        TimeUnit.MILLISECONDS.sleep(rndm.nextInt(200));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    if (rndm.nextInt(5) == 0) throw new NullPointerException("Oops");
                    if (it % 2 == 0) {
                        return Tuple2.of(it,null);
                    } else {
                        return Tuple2.of(null,it);
                    }
                });
    }
    private void step03e(MyCount c) {
        val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
        val snk1 = SinkResource.of(it -> log.info("{} is even", it), c::incEven);
        val snk2 = SinkResource.of(it -> log.info("{} is odd", it), c::incOdd);
        Random rndm = new Random();

        Pgm.from(src).before(c::incRead).into(snk1, snk2).forEachParallel(5,
                (it) -> {
                    log.info("Hi {}", it);
                    try {
                        TimeUnit.MILLISECONDS.sleep(rndm.nextInt(200));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    if (it % 2 == 0) {
                        return Tuple2.of(it,null);
                    } else {
                        return Tuple2.of(null,it);
                    }
                });
    }

    private void step02(MyCount c) {
        try {
            Files.delete(Path.of("/dev/null"));
        } catch (IOException e) {
            throw new BatchIOException(e);
        }
    }

    private static class MyCount extends StatsCount {
        private int nEven = 0;
        private int nOdd = 0;
        private int nRead = 0;

        protected MyCount(String name) {
            super(name);
        }

        public void incEven() {
            nEven++;
        }

        public void incOdd() {
            nOdd++;
        }

        @Override
        protected void recap(PrintWriter pw) {
            pw.printf("- read .................:%,9d%n", nRead);
            pw.printf("- even .................:%,9d%n", nEven);
            pw.printf("- odd ..................:%,9d%n", nOdd);
        }

        public void incRead() {
            nRead++;
        }
    }
}
