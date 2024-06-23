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
class PmIterableLoop3<S extends AutoCloseable, I, O1, T1 extends AutoCloseable, O2, T2 extends AutoCloseable, O3, T3 extends AutoCloseable> implements IterableLoop3<I, O1, O2, O3> {
    private final PmPushSource<S, I> par;
    private final SinkResource<T1, O1> sink1;
    private final SinkResource<T2, O2> sink2;
    private final SinkResource<T3, O3> sink3;

    PmIterableLoop3(PmPushSource<S, I> parent, SinkResource<T1, O1> sink1, SinkResource<T2, O2> sink2, SinkResource<T3, O3> sink3) {
        this.par = parent;
        this.sink1 = sink1;
        this.sink2 = sink2;
        this.sink3 = sink3;
    }

    @Override
    public void forEach(Function<? super I, ? extends Tuple3<? extends O1, ? extends O2, ? extends O3>> transformer) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             S s = par.source.get()) {
            Iterator<I> iterator = par.source.iterator(s);
            while (iterator.hasNext()) {
                I i = iterator.next();
                if (par.terminateTest != null && par.terminateTest.test(i)) {
                    log.warn("QT3.>>> Loop terminated on condition");
                    break;
                }
                if (par.beforeAction != null) par.beforeAction.run();
                val oo = transformer.apply(i);
                oo.onT1(v -> sink1.accept(t1, v));
                oo.onT2(v -> sink2.accept(t2, v));
                oo.onT3(v -> sink3.accept(t3, v));
            }
        } catch (BatchException e) {
            throw e;
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple3<? extends O1, ? extends O2, ? extends O3>> transformer) {
        par.new DoParallelFair<Tuple3<? extends O1, ? extends O2, ? extends O3>>(maxThread, transformer) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> {
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                    });
                }
            }
        }.start();
    }

    @Override
    public void forEachParallel(int maxThread, Function<? super I, ? extends Tuple3<? extends O1, ? extends O2, ? extends O3>> transformer) {
        par.new DoParallelRaw<Tuple3<? extends O1, ? extends O2, ? extends O3>>(maxThread, transformer) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> {
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                    });
                }
            }
        }.start();
    }

    @Override
    public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends Tuple3<? extends O1, ? extends O2, ? extends O3>>> asyncTransformer) {
        par.new DoAsyncParallelFair<Tuple3<? extends O1, ? extends O2, ? extends O3>>(maxThread, asyncTransformer) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> {
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                    });
                }
            }
        }.start();
    }

    @Override
    public void forEachAsync(int maxThread, AsyncWorker3<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>> asyncWorker) {
        par.new DoAsyncWrite(maxThread) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                     T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                     T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                     S s = par.source.get()) {
                    doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture()),
                            par.source.iterator(s), i -> asyncWorker.apply(i, w1::write, w2::write, w3::write));
                }   // closing writer queues
            }
        }.start();
    }

    @Override
    public void forEach(Worker3<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>> worker) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             S s = par.source.get()) {
            Consumer<? super O1> w1 = consumerOf(sink1, t1);
            Consumer<? super O2> w2 = consumerOf(sink2, t2);
            Consumer<? super O3> w3 = consumerOf(sink3, t3);
            Iterator<I> iterator = par.source.iterator(s);
            while (iterator.hasNext()) {
                I i = iterator.next();
                if (par.terminateTest != null && par.terminateTest.test(i)) {
                    log.warn("QW3.>>> Loop terminated on condition");
                    break;
                }
                if (par.beforeAction != null) par.beforeAction.run();
                worker.process(i, w1, w2, w3);
            }
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public ParallelLoop3<I, O1, O2, O3> shutdownTimeout(long time, TimeUnit unit) {
        par.setShutdownTimeout(time, unit);
        return this;
    }

    public void forEachParallel(
            int maxThread,
            Worker3<? super I,
                    Consumer<? super O1>,
                    Consumer<? super O2>,
                    Consumer<? super O3>> worker) {
        par.new DoParallelWrite(maxThread) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                     T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                     T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                     S s = par.source.get()) {
                    doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture()),
                            par.source.iterator(s), i -> worker.process(i, w1::write, w2::write, w3::write));
                }   // closing writer queues
            }
        }.start();
    }

}
