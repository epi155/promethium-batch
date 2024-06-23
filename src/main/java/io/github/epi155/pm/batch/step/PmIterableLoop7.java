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
class PmIterableLoop7<I, S extends AutoCloseable, T1 extends AutoCloseable, O1, T2 extends AutoCloseable, O2, T3 extends AutoCloseable, O3, T4 extends AutoCloseable, O4, O5, T5 extends AutoCloseable, O6, T6 extends AutoCloseable, T7 extends AutoCloseable, O7> implements IterableLoop7<I, O1, O2, O3, O4, O5, O6, O7> {
    private final PmPushSource<S, I> par;
    private final SinkResource<T1, O1> sink1;
    private final SinkResource<T2, O2> sink2;
    private final SinkResource<T3, O3> sink3;
    private final SinkResource<T4, O4> sink4;
    private final SinkResource<T5, O5> sink5;
    private final SinkResource<T6, O6> sink6;
    private final SinkResource<T7, O7> sink7;

    PmIterableLoop7(PmPushSource<S, I> parent, SinkResource<T1, O1> sink1, SinkResource<T2, O2> sink2, SinkResource<T3, O3> sink3, SinkResource<T4, O4> sink4, SinkResource<T5, O5> sink5, SinkResource<T6, O6> sink6, SinkResource<T7, O7> sink7) {
        this.par = parent;
        this.sink1 = sink1;
        this.sink2 = sink2;
        this.sink3 = sink3;
        this.sink4 = sink4;
        this.sink5 = sink5;
        this.sink6 = sink6;
        this.sink7 = sink7;
    }

    @Override
    public void forEach(
            Function<? super I,
                    ? extends Tuple7<
                            ? extends O1,
                            ? extends O2,
                            ? extends O3,
                            ? extends O4,
                            ? extends O5,
                            ? extends O6,
                            ? extends O7>> transformer) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             T4 t4 = sink4.get();
             T5 t5 = sink5.get();
             T6 t6 = sink6.get();
             T7 t7 = sink7.get();
             S s = par.source.get()) {
            Iterator<I> iterator = par.source.iterator(s);
            while (iterator.hasNext()) {
                I i = iterator.next();
                if (par.terminateTest != null && par.terminateTest.test(i)) {
                    log.warn("QT7.>>> Loop terminated on condition");
                    break;
                }
                if (par.beforeAction != null) par.beforeAction.run();
                val oo = transformer.apply(i);
                oo.onT1(v -> sink1.accept(t1, v));
                oo.onT2(v -> sink2.accept(t2, v));
                oo.onT3(v -> sink3.accept(t3, v));
                oo.onT4(v -> sink4.accept(t4, v));
                oo.onT5(v -> sink5.accept(t5, v));
                oo.onT6(v -> sink6.accept(t6, v));
                oo.onT7(v -> sink7.accept(t7, v));
            }
        } catch (BatchException e) {
            throw e;
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public void forEachParallelFair(
            int maxThread,
            Function<? super I,
                    ? extends Tuple7<
                            ? extends O1,
                            ? extends O2,
                            ? extends O3,
                            ? extends O4,
                            ? extends O5,
                            ? extends O6,
                            ? extends O7>> transformer) {
        par.new DoParallelFair<Tuple7<? extends O1, ? extends O2, ? extends O3, ? extends O4,
                ? extends O5, ? extends O6, ? extends O7>>(maxThread, transformer) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     T5 t5 = sink5.get();
                     T6 t6 = sink6.get();
                     T7 t7 = sink7.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> {
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
                        oo.onT6(v -> sink6.accept(t6, v));
                        oo.onT7(v -> sink7.accept(t7, v));
                    });
                }
            }
        }.start();
    }

    @Override
    public void forEachParallel(int maxThread, Function<? super I, ? extends Tuple7<? extends O1, ? extends O2, ? extends O3, ? extends O4, ? extends O5, ? extends O6, ? extends O7>> transformer) {
        par.new DoParallelRaw<Tuple7<? extends O1, ? extends O2, ? extends O3, ? extends O4,
                ? extends O5, ? extends O6, ? extends O7>>(maxThread, transformer) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     T5 t5 = sink5.get();
                     T6 t6 = sink6.get();
                     T7 t7 = sink7.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> {
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
                        oo.onT6(v -> sink6.accept(t6, v));
                        oo.onT7(v -> sink7.accept(t7, v));
                    });
                }
            }
        }.start();
    }

    @Override
    public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends Tuple7<? extends O1, ? extends O2, ? extends O3, ? extends O4, ? extends O5, ? extends O6, ? extends O7>>> asyncTransformer) {
        par.new DoAsyncParallelFair<Tuple7<? extends O1, ? extends O2, ? extends O3, ? extends O4,
                ? extends O5, ? extends O6, ? extends O7>>(maxThread, asyncTransformer) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     T5 t5 = sink5.get();
                     T6 t6 = sink6.get();
                     T7 t7 = sink7.get();
                     S s = par.source.get()) {
                    doWork(par.source.iterator(s), oo -> {
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
                        oo.onT6(v -> sink6.accept(t6, v));
                        oo.onT7(v -> sink7.accept(t7, v));
                    });
                }
            }
        }.start();
    }

    @Override
    public void forEachAsync(int maxThread, AsyncWorker7<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>, Consumer<? super O4>, Consumer<? super O5>, Consumer<? super O6>, Consumer<? super O7>> asyncWorker) {
        par.new DoAsyncWrite(maxThread) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                     T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                     T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                     T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                     T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                     T6 t6 = sink6.get(); PmQueueWriter<O6> w6 = openQueue(sink6, t6);
                     T7 t7 = sink7.get(); PmQueueWriter<O7> w7 = openQueue(sink7, t7);
                     S s = par.source.get()) {
                    doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                    w5.getFuture(), w6.getFuture(), w7.getFuture()),
                            par.source.iterator(s), i -> asyncWorker.apply(i,
                                    w1::write, w2::write, w3::write, w4::write,
                                    w5::write, w6::write, w7::write));
                }   // closing writer queues
            }
        }.start();
    }

    @Override
    public void forEach(Worker7<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>, Consumer<? super O4>, Consumer<? super O5>, Consumer<? super O6>, Consumer<? super O7>> worker) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             T4 t4 = sink4.get();
             T5 t5 = sink5.get();
             T6 t6 = sink6.get();
             T7 t7 = sink7.get();
             S s = par.source.get()) {
            Consumer<? super O1> w1 = consumerOf(sink1, t1);
            Consumer<? super O2> w2 = consumerOf(sink2, t2);
            Consumer<? super O3> w3 = consumerOf(sink3, t3);
            Consumer<? super O4> w4 = consumerOf(sink4, t4);
            Consumer<? super O5> w5 = consumerOf(sink5, t5);
            Consumer<? super O6> w6 = consumerOf(sink6, t6);
            Consumer<? super O7> w7 = consumerOf(sink7, t7);
            Iterator<I> iterator = par.source.iterator(s);
            while (iterator.hasNext()) {
                I i = iterator.next();
                if (par.terminateTest != null && par.terminateTest.test(i)) {
                    log.warn("QW7.>>> Loop terminated on condition");
                    break;
                }
                if (par.beforeAction != null) par.beforeAction.run();
                worker.process(i, w1, w2, w3, w4, w5, w6, w7);
            }
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public ParallelLoop7<I, O1, O2, O3, O4, O5, O6, O7> shutdownTimeout(long time, TimeUnit unit) {
        par.setShutdownTimeout(time, unit);
        return this;
    }

    @Override
    public void forEachParallel(
            int maxThread,
            Worker7<? super I,
                    Consumer<? super O1>,
                    Consumer<? super O2>,
                    Consumer<? super O3>,
                    Consumer<? super O4>,
                    Consumer<? super O5>,
                    Consumer<? super O6>,
                    Consumer<? super O7>> worker) {
        par.new DoParallelWrite(maxThread) {
            @Override
            protected void openResources() throws Exception {
                try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                     T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                     T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                     T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                     T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                     T6 t6 = sink6.get(); PmQueueWriter<O6> w6 = openQueue(sink6, t6);
                     T7 t7 = sink7.get(); PmQueueWriter<O7> w7 = openQueue(sink7, t7);
                     S s = par.source.get()) {
                    doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                    w5.getFuture(), w6.getFuture(), w7.getFuture()),
                            par.source.iterator(s),
                            i -> worker.process(i, w1::write, w2::write, w3::write, w4::write,
                                    w5::write, w6::write, w7::write));
                }   // closing writer queues
            }
        }.start();
    }
}