package io.github.epi155.pm.batch.step;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.Objects;
import java.util.concurrent.*;

import static io.github.epi155.pm.batch.step.BatchException.placeOf;

@Slf4j
class PmQueueWriter<T> implements Closeable {
    private final BlockingQueue<Tuple1<T>> queue;
    @Getter
    private final Future<?> future;

    private PmQueueWriter(BlockingQueue<Tuple1<T>> queue, Future<?> promise) {
        this.queue = queue;
        this.future = promise;
    }

    static <T extends AutoCloseable, O>
    PmQueueWriter<O> of(
            int maxThread,
            ExecutorService pool,
            SinkResource<T, O> sink, T t) {
        BlockingQueue<Tuple1<O>> queue = new LinkedBlockingDeque<>(2 * maxThread + 1);
        Future<?> promise = pool.submit(() -> {
            try {
                Tuple1<O> o;
                do {
                    o = queue.take();
                } while (o.onT1(it -> sink.accept(t, it)));
                log.debug("W.--- writer listener exit.");
            } catch (InterruptedException e) {
                log.warn("W.>>> writer listener interrupted.");
                Thread.currentThread().interrupt();
            }
        });
        return new PmQueueWriter<>(queue, promise);
    }

    public void write(@NotNull T t) {
        put(Tuple1.of(Objects.requireNonNull(t)));
    }

    private void put(Tuple1<T> t1) {
        try {
            queue.put(t1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void close() {
        put(Tuple1.empty());   // send End Of Write
        try {
            log.debug("W.--- waiting for the writer listener to terminate ...");
            future.get();
            log.debug("W.--- writer listener successful terminated.");
        } catch (InterruptedException e) {
            log.info("W.>>> writer listener was interrupted. (dead branch?)");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            String place = placeOf(cause.getStackTrace());
            log.warn("W.### Error in writer listener: {} [{}]", cause.getMessage(), place);
            throw new BatchException((cause));
        }
    }
}