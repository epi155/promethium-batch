package io.github.epi155.pm.batch.step;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.epi155.pm.batch.step.PmPushCore.consumerOf;

@Slf4j
class PmIterableLoop2<S extends AutoCloseable, I, O1, T1 extends AutoCloseable, O2, T2 extends AutoCloseable> implements IterableLoop2<I, O1, O2> {
    private final PmPushSource<S, I> par;
    private final SinkResource<T1, O1> sink1;
    private final SinkResource<T2, O2> sink2;

    PmIterableLoop2(PmPushSource<S, I> parent, SinkResource<T1, O1> sink1, SinkResource<T2, O2> sink2) {
        this.par = parent;
        this.sink1 = sink1;
        this.sink2 = sink2;
    }

    @Override
    public void forEach(Function<? super I, ? extends Tuple2<? extends O1, ? extends O2>> transformer) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             S s = par.source.get()) {
            Iterator<I> iterator = par.source.iterator(s);
            while (iterator.hasNext()) {
                I i = iterator.next();
                if (par.terminateTest != null && par.terminateTest.test(i)) {
                    log.warn("QT2.>>> Loop terminated on condition");
                    break;
                }
                if (par.beforeAction != null) par.beforeAction.run();
                val oo = transformer.apply(i);
                oo.onT1(v -> sink1.accept(t1, v));
                oo.onT2(v -> sink2.accept(t2, v));
            }
        } catch (BatchException e) {
            throw e;
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple2<? extends O1, ? extends O2>> transformer) {
        par.new DoParallelFair<Tuple2<? extends O1, ? extends O2>>(maxThread, transformer) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> {
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                    });
                }
            }
        }.start();
    }

    @Override
    public void forEachParallel(int maxThread, Function<? super I, ? extends Tuple2<? extends O1, ? extends O2>> transformer) {
        par.new DoParallelRaw<Tuple2<? extends O1, ? extends O2>>(maxThread, transformer) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> {
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                    });
                }
            }
        }.start();
    }

    @Override
    public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends Tuple2<? extends O1, ? extends O2>>> asyncTransformer) {
        par.new DoAsyncParallelFair<Tuple2<? extends O1, ? extends O2>>(maxThread, asyncTransformer) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> {
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                    });
                }
            }
        }.start();
    }

    @Override
    public void forEachAsync(int maxThread, AsyncWorker2<? super I, Consumer<? super O1>, Consumer<? super O2>> asyncWorker) {
        par.new DoAsyncWrite(maxThread) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                     T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                     S s = par.source.get()) {
                    doWork(List.of(w1.getFuture(), w2.getFuture()),
                            par.source.iterator(s), i -> asyncWorker.apply(i, w1::write, w2::write));
                }   // closing writer queues
            }
        }.start();
    }

    @Override
    public void forEach(Worker2<? super I, Consumer<? super O1>, Consumer<? super O2>> worker) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             S s = par.source.get()) {
            Consumer<? super O1> w1 = consumerOf(sink1, t1);
            Consumer<? super O2> w2 = consumerOf(sink2, t2);
            Iterator<I> iterator = par.source.iterator(s);
            while (iterator.hasNext()) {
                I i = iterator.next();
                if (par.terminateTest != null && par.terminateTest.test(i)) {
                    log.warn("QW2.>>> Loop terminated on condition");
                    break;
                }
                if (par.beforeAction != null) par.beforeAction.run();
                worker.process(i, w1, w2);
            }
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public ParallelLoop2<I, O1, O2> shutdownTimeout(long time, TimeUnit unit) {
        par.setShutdownTimeout(time, unit);
        return this;
    }

    @Override
    public void forEachParallel(
            int maxThread,
            Worker2<? super I,
                    Consumer<? super O1>,
                    Consumer<? super O2>> worker) {
        par.new DoParallelWrite(maxThread) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                     T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                     S s = par.source.get()) {
                    doWork(List.of(w1.getFuture(), w2.getFuture()),
                            par.source.iterator(s), i -> worker.process(i, w1::write, w2::write));
                }   // closing writer queues
            }
        }.start();
    }
}
