package io.github.epi155.promethium.test;

import io.github.epi155.promethium.batch.TaskGranter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
class TestGranter {
    private static final Random random = new Random();

    @Test   // +1000%
    void testHugeLoad() throws InterruptedException {
        int nmOk = stressTest(1000, 91);
        Assertions.assertTrue(nmOk > 60);
    }
    @Test   // +100%
    void testLargeLoad() throws InterruptedException {
        int nmOk = stressTest(100, 500);
        Assertions.assertTrue(nmOk > 50);
    }
    @Test   // +20%
    void testHighLoad() throws InterruptedException {
        int nmOk = stressTest(100, 833);
        Assertions.assertTrue(nmOk > 90);
    }

    private int stressTest(int nmTask, long delay) throws InterruptedException {
        TaskGranter granter = TaskGranter.getInstance(20, 2, 20, TimeUnit.SECONDS);

        val nmComplete = new AtomicInteger();
        val nmTimeout = new AtomicInteger();
        val nmDiscard = new AtomicInteger();

        for(int k=0; k<nmTask; k++) {
            attack(granter, k, nmComplete, nmTimeout, nmDiscard);
            TimeUnit.MILLISECONDS.sleep(delay);
        }

        granter.shutdown();
        log.info("Completed ......: {}", nmComplete.get());
        log.info("Timeout ........: {}", nmTimeout.get());
        log.info("Discarded ..,...: {}", nmDiscard.get());

        return nmComplete.get();
    }


    private void attack(TaskGranter granter, int k, AtomicInteger nmComplete, AtomicInteger nmTimeout, AtomicInteger nmDiscard) {
        Runnable task = () -> {
            val tiStart = System.currentTimeMillis();
            try {
                log.debug("--- Queue {}", k);
                String value = granter.compute(String.valueOf(k), () -> greet(k));
                val tiStop = System.currentTimeMillis();
                log.info("200 Result {} [{}]: {}", k, tiStop-tiStart, value);
                nmComplete.incrementAndGet();
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                val tiStop = System.currentTimeMillis();
                log.warn("500 Oops {} [{}]: {}", k, tiStop-tiStart, e.toString());
                nmTimeout.incrementAndGet();
            } catch (IllegalStateException e) {
                val tiStop = System.currentTimeMillis();
                log.warn("503 Oops {} [{}]: {}", k, tiStop-tiStart, e.toString());
                nmDiscard.incrementAndGet();
            }
        };
        new Thread(task).start();
    }

    @SneakyThrows
    private String greet(int k) {
        TimeUnit.SECONDS.sleep(2);
        return String.format("Hello from %d", k);
    }
}
