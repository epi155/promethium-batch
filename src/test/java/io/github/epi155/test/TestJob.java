package io.github.epi155.test;

import io.github.epi155.pm.batch.job.JCL;
import io.github.epi155.pm.batch.job.StepCount;
import io.github.epi155.pm.batch.step.Pgm;
import io.github.epi155.pm.batch.step.SinkResource;
import io.github.epi155.pm.batch.step.SourceResource;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.stream.IntStream;

class TestJob {

    @Test
    void job01() {
        MyCount count1 = new MyCount("Step01");
        MyCount count2 = new MyCount("Step02");
        MyCount count3 = new MyCount("Step03");
        MyCount count4 = new MyCount("Step04");
        MyCount count5 = new MyCount("Step05");
        int x = JCL.getInstance()
                .forkPgm(count1, this::step01)
                .forkPgm(count2, this::step01)
                .execPgm(count3, this::step01)
                .join()
                .push() // save rc
                .nextPgm(count4, this::step01) // execute if ok
                .elsePgm(count5, this::step01) // execute in ko
                .pop()
                .returnCode();
    }

    private void step01(MyCount c) {
        val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
        val snk1 = SinkResource.of(System.out::println, c::incEven);
        val snk2 = SinkResource.of(System.err::println, c::incOdd);

        Pgm.from(src).before(c::incRead).into(snk1, snk2).forEach(
                (it, wr1, wr2) -> {
                    if (it % 2 == 0) {
                        wr1.accept(it);
                    } else {
                        wr2.accept(it);
                    }
                });
    }

    private static class MyCount extends StepCount {
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
