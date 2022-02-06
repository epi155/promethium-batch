package io.github.epi155.promethium.test;

import io.github.epi155.promethium.batch.BatchQueue;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
class TestSubmit {
    private static final Random random = new Random();

    @Test
    void testSubmit1() {
        Assertions.assertAll(this::submitExample1);
    }
    @Test
    void testSubmit2() {
        Assertions.assertAll(this::submitExample2);
    }

    private void submitExample1() throws InterruptedException {
        val queue = BatchQueue.getInstance(2, this::onSuccess, this::onError);
        queue.submit("qui", this::backgroundTask);
        queue.submit("quo", this::backgroundTask);
        queue.submit("qua", this::backgroundTask);
        queue.submit("que", this::backgroundTask);
        queue.submit("quu", this::backgroundTask);
        queue.submit("quy", this::backgroundTask);
        log.debug("submitted");
        queue.shutdown();
        log.debug("finish");
    }
    private void submitExample2() throws InterruptedException {
        val queue = BatchQueue.getInstance(10, 25, this::onSuccess, this::onError);
        queue.submit("qui1", this::backgroundTask);
        queue.submit("quo1", this::backgroundTask);
        queue.submit("qua1", this::backgroundTask);
        queue.submit("que1", this::backgroundTask);
        queue.submit("quu1", this::backgroundTask);
        queue.submit("quy1", this::backgroundTask);

        queue.submit("qui2", this::backgroundTask);
        queue.submit("quo2", this::backgroundTask);
        queue.submit("qua2", this::backgroundTask);
        queue.submit("que2", this::backgroundTask);
        queue.submit("quu2", this::backgroundTask);
        queue.submit("quy2", this::backgroundTask);

        queue.submit("qui3", this::backgroundTask);
        queue.submit("quo3", this::backgroundTask);
        queue.submit("qua3", this::backgroundTask);
        queue.submit("que3", this::backgroundTask);
        queue.submit("quu3", this::backgroundTask);
        queue.submit("quy3", this::backgroundTask);

        queue.submit("qui4", this::backgroundTask);
        queue.submit("quo4", this::backgroundTask);
        queue.submit("qua4", this::backgroundTask);
        queue.submit("que4", this::backgroundTask);
        queue.submit("quu4", this::backgroundTask);
        queue.submit("quy4", this::backgroundTask);
        log.debug("submitted");
        queue.shutdown();
        log.debug("finish");
    }

    @SneakyThrows
    private Integer backgroundTask() {
//        if (random.nextInt(5) < 1)
//            throw new NullPointerException("aa");
        TimeUnit.SECONDS.sleep(5);
        return random.nextInt(10);
    }

    private void onError(String name, Throwable throwable) {
//        log.error("Something wrong in {} ...", name, throwable);
    }

    private void onSuccess(String name, Integer outcome) {
//        log.info("Result of {} is {}", name, outcome);
    }
}
