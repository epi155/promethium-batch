package io.github.epi155.test;

import io.github.epi155.pm.batch.job.JCL;
import io.github.epi155.pm.batch.job.Proc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.github.epi155.pm.batch.job.Cond.EQ;
import static io.github.epi155.pm.batch.job.Cond.NE;

@Slf4j
class TestJob2 {
    private static final Random rndm = new Random(1);
    @Test
    void job01() {
        int x = JCL.getInstance().job("job01")
                .execPgm("step01", this::step00)
                .cond(0,NE).execPgm("step02", this::step03)
                .cond(0,EQ,"step02").execPgm("step03", this::step02)
                .complete();
        log.info("Job returnCode: {}", x);
    }
    @Test
    void job02() {
        Proc<Void> proc01 = Proc.create(s -> s
                .forkPgm("step01", this::step21)
                .forkPgm("step02", this::step22)
                .join()
                .when(0,EQ).execPgm("step03", this::step23)
        );
        int x = JCL.getInstance().job("job02")
                .forkProc("proc01", proc01)
                .execPgm("step00", this::step00)
                .join()
                .when(0,EQ).execPgm("step04", this::step24)
                .complete();
        log.info("Job returnCode: {}", x);
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
