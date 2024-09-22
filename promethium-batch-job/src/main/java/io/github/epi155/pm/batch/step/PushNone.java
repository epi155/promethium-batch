package io.github.epi155.pm.batch.step;

import io.github.epi155.pm.batch.job.BatchException;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * class to manage elements of a single input resource without explicit output resources
 *
 * @param <S> source resource type
 * @param <I> input type
 */
@Slf4j
public abstract class PushNone<S extends AutoCloseable, I> extends PmPushCore<I> implements LoopSourceZro<I> {
    /**
     * source resource to read data from
     */
    protected final SourceResource<S, I> source;

    /**
     * constructor
     *
     * @param source source resource to read data from
     */
    protected PushNone(SourceResource<S, I> source) {
        this.source = source;
    }

    /**
     *
     * @return used source resource
     */
    public S getSource() {
        return source.get();
    }

    /**
     *
     * @param s source resource
     * @return element iterator
     */
    public Iterator<I> srcIterator(S s) {
        return source.iterator(s);
    }

    @Override
    public ParallelLoopZro<I> shutdownTimeout(long time, TimeUnit unit) {
        setShutdownTimeout(time, unit);
        return this;
    }

    @Override
    public void forEach(Consumer<? super I> action) {
        try (S s = source.get()) {
            Iterator<I> iterator = source.iterator(s);
            while (iterator.hasNext()) {
                I i = iterator.next();
                if (terminateTest != null && terminateTest.test(i)) {
                    log.warn("Q0->>> Loop terminated on condition");
                    break;
                }
                if (beforeAction != null) beforeAction.run();
                action.accept(i);
            }
        } catch (BatchException e) {
            throw e;
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public void forEachParallel(int maxThread, Consumer<? super I> action) {
        new DoParallelSlim(maxThread, action) {
            @Override
            protected void openResources() throws Exception {
                try (S s = source.get()) {
                    doWork(source.iterator(s));
                }
            }
        }.start();
    }

    @Override
    public void forEachAsync(int maxThread, Function<? super I, ? extends Future<Void>> asyncTransformer) {
        new DoAsyncParallelFair<Void>(maxThread, asyncTransformer) {
            @Override
            protected void openResources() throws Exception {
                try (S s = source.get()) {
                    doWork(source.iterator(s), oo -> {
                    });
                }
            }
        }.start();
    }
}
