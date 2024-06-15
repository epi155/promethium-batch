package io.github.epi155.test;

import io.github.epi155.pm.batch.job.JCL;
import io.github.epi155.pm.batch.job.Proc;
import io.github.epi155.pm.batch.step.Pgm;
import io.github.epi155.pm.batch.step.SinkBufResource;
import io.github.epi155.pm.batch.step.SinkResource;
import io.github.epi155.pm.batch.step.SourceResource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Random;

import static io.github.epi155.pm.batch.job.Cond.EQ;
import static io.github.epi155.pm.batch.job.Cond.NE;

@Slf4j
class TestJob2 {
    private static final Random rndm = new Random(1);
    File inFile = null;
    File outFile = null;
    Runnable step01 = new Runnable() {
        @Override
        public void run() {
            val src = SourceResource.bufferedReader(inFile);
            val snk = SinkBufResource.bufferedWriter(outFile);
            Pgm.from(src).into(snk).forEach(it -> it);
        }
    };

    @Test
    void job01() {
        int x = JCL.getInstance().job("job01")
                .execPgm("step01", this::step00)
                .cond(0, NE).execPgm("step02", this::step03)
                .cond(0, EQ, "step02").execPgm("step03", this::step02)
                .complete();
        log.info("Job returnCode: {}", x);
    }

    @Test
    void job02() {
        Proc<Void> proc01 = Proc.create(s -> s
                .forkPgm("step21", this::step21)
                .forkPgm("step22", this::step22)
                .join()
                .when(0, EQ).execPgm("step23", this::step23)
        );
        int x = JCL.getInstance().job("job02")
                .forkProc("proc01", proc01)
                .execPgm("step00", this::step00)
                .join()
                .when(0, EQ).execPgm("step04", this::step24)
                .complete();
        log.info("Job returnCode: {}", x);
    }

    @Test
    void job03() {
        int x = JCL.getInstance().job("job03")
                .forkPgm("sort1", this::sort1)
                .execPgm("sort2", this::sort2)
                .cond(0,EQ).quit("sort1")
                .cond(0,NE).join("sort1")
                .cond(0, NE).execPgm("balance", this::balance)
                .complete();
        log.info("Job returnCode: {}", x);
    }
    @Test
    void job13() {
        int x = JCL.getInstance().job("job13")
                .forkPgm("sort1", this::sort1)
                .forkPgm("sort1", this::sort2)
                .join()
                .cond(0, NE).execPgm("balance", this::balance)
                .complete();
        log.info("Job returnCode: {}", x);
    }

//    @Test
//    void job04() {
//        call();
//    }

    public Integer call() {
        return JCL.getInstance().job("job01")
                .execPgm("step01", step01::run)
                .complete();
    }

    private int sort1() {
        try {
            Thread.sleep(rndm.nextInt(15000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return 0;
    }

    private void sort2() {
        try {
            Thread.sleep(rndm.nextInt(5000));
//            throw new RuntimeException();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void balance() {
        try {
            Thread.sleep(rndm.nextInt(8000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int step25() {
        return rndm.nextInt(4);
    }

    private int step24() {
        return rndm.nextInt(4);
    }

    private int step23() {
        return rndm.nextInt(4);
    }

    private int step22() {
        return rndm.nextInt(4);
    }

    private int step21() {
        return rndm.nextInt(4);
    }

    private int step03() {
        return 4;
    }

    private void step02() {
    }

    private void step00() {
    }
}
