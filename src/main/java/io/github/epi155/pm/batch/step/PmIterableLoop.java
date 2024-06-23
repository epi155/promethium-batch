package io.github.epi155.pm.batch.step;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.epi155.pm.batch.step.PmPushCore.consumerOf;

@Slf4j
class PmIterableLoop<I, S extends AutoCloseable, O, T extends AutoCloseable> implements IterableLoop<I, O> {
    private final PmPushSource<S, I> par;
    private final SinkResource<T, O> sink;

    PmIterableLoop(PmPushSource<S, I> parent, SinkResource<T, O> sink) {
        this.par = parent;
        this.sink = sink;
    }


    @Override
    public void forEach(Function<? super I, ? extends O> transformer) {
        try (T t = sink.get();
             S s = par.source.get()) {
            Iterator<I> iterator = par.source.iterator(s);
            while (iterator.hasNext()) {
                I i = iterator.next();
                if (par.terminateTest != null && par.terminateTest.test(i)) {
                    log.warn("QT1.>>> Loop terminated on condition");
                    break;
                }
                if (par.beforeAction != null) par.beforeAction.run();
                sink.accept(t, transformer.apply(i));
            }
        } catch (BatchException e) {
            throw e;
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    public void forEachParallelFair(int maxThread, Function<? super I, ? extends O> transformer) {
        par.new DoParallelFair<O>(maxThread, transformer) {
            @Override
            protected void openResources() throws Exception {
                try (T t = sink.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> sink.accept(t, oo));
                }
            }
        }.start();
    }

    @Override
    public void forEachParallel(int maxThread, Function<? super I, ? extends O> transformer) {
        par.new DoParallelRaw<O>(maxThread, transformer) {
            @Override
            protected void openResources() throws Exception {
                try (T t = sink.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> sink.accept(t, oo));
                }
            }
        }.start();
    }

    @Override
    public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends O>> asyncTransformer) {
        par.new DoAsyncParallelFair<O>(maxThread, asyncTransformer) {
            @Override
            protected void openResources() throws Exception {
                try (T t = sink.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> sink.accept(t, oo));
                }
            }
        }.start();
    }

    @Override
    public void forEachAsync(int maxThread, AsyncWorker<? super I, Consumer<? super O>> asyncWorker) {
        par.new DoAsyncWrite(maxThread) {
            @Override
            protected void openResources() throws Exception {
                try (T t = sink.get(); PmQueueWriter<O> w1 = openQueue(sink, t);
                     S s = par.source.get()) {
                    doWork(List.of(w1.getFuture()),
                            par.source.iterator(s), i -> asyncWorker.apply(i, w1::write));
                }   // closing writer queues
            }
        }.start();
    }

    @Override
    public ParallelLoop1<I, O> shutdownTimeout(long time, TimeUnit unit) {
        par.setShutdownTimeout(time, unit);
        return this;
    }

    @Override
    public void forEach(Worker<? super I, Consumer<? super O>> worker) {
        try (T t = sink.get();
             S s = par.source.get()) {
            Consumer<? super O> wr = consumerOf(sink, t);
            Iterator<I> iterator = par.source.iterator(s);
            while (iterator.hasNext()) {
                I i = iterator.next();
                if (par.terminateTest != null && par.terminateTest.test(i)) {
                    log.warn("QW1.>>> Loop terminated on condition");
                    break;
                }
                if (par.beforeAction != null) par.beforeAction.run();
                worker.process(i, wr);
            }
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public void forEachParallel(
            int maxThread,
            Worker<? super I, Consumer<? super O>> worker) {
        par.new DoParallelWrite(maxThread) {
            @Override
            protected void openResources() throws Exception {
                try (T t = sink.get(); PmQueueWriter<O> w1 = openQueue(sink, t);
                     S s = par.source.get()) {
                    doWork(List.of(w1.getFuture()),
                            par.source.iterator(s), i -> worker.process(i, w1::write));
                }   // closing writer queues
            }
        }.start();
    }
}
