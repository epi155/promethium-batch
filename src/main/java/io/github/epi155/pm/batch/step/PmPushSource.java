package io.github.epi155.pm.batch.step;

import io.github.epi155.pm.batch.job.JCL;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.MDC;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.epi155.pm.batch.step.BatchException.placeOf;

@Slf4j
abstract class PmPushSource<S extends AutoCloseable, I> implements LoopSource<I> {
    private static final String JOB_NAME;
    private static final String STEP_NAME;

    static {
        JOB_NAME = JCL.getInstance().jobName();
        STEP_NAME = JCL.getInstance().stepName();
    }

    protected final SourceResource<S, I> source;
    private Runnable beforeAction;
    private long time = 30;
    private TimeUnit unit = TimeUnit.SECONDS;
    private Predicate<? super I> terminateTest = null;

    PmPushSource(SourceResource<S, I> source) {
        this.source = source;
    }

    @Override
    public LoopSourceLayer<I> terminate(Predicate<? super I> test) {
        this.terminateTest = test;
        return this;
    }

    @Override
    public LoopSourceStd<I> before(Runnable action) {
        this.beforeAction = action;
        return this;
    }

    @Override
    public <T extends AutoCloseable, O> IterableLoop<I, O> into(SinkResource<T, O> sink) {
        return new IterableLoop<>() {

            @Override
            public void forEach(Function<? super I, ? extends O> transformer) {
                try (T t = sink.get();
                     S s = source.get()) {
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QT1.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        sink.accept(t, transformer.apply(i));
                    }
                } catch (BatchException e) {
                    throw e;
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            public void forEachParallelFair(int maxThread, Function<? super I, ? extends O> transformer) {
                new DoParallelFair<O>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T t = sink.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> sink.accept(t, oo));
                        }
                    }
                }.start();
            }

            @Override
            public void forEachParallel(int maxThread, Function<? super I, ? extends O> transformer) {
                new DoParallelRaw<O>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T t = sink.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> sink.accept(t, oo));
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends O>> asyncTransformer) {
                new DoAsyncParallelFair<>(maxThread, asyncTransformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T t = sink.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> sink.accept(t, oo));
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(int maxThread, AsyncWorker<? super I, Consumer<? super O>> asyncWorker) {
                new DoAsyncWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T t = sink.get(); PmQueueWriter<O> w1 = openQueue(sink, t);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture()),
                                    source.iterator(s), i -> asyncWorker.apply(i, w1::write));
                        }   // closing writer queues
                    }
                }.start();
            }

            @Override
            public ParallelLoop1<I, O> shutdownTimeout(long time, TimeUnit unit) {
                setShutdownTimeout(time, unit);
                return this;
            }

            @Override
            public void forEach(Worker<? super I, Consumer<? super O>> worker) {
                try (T t = sink.get();
                     S s = source.get()) {
                    Consumer<? super O> wr = sink.consumer(t);
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QW1.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
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
                new DoParallelWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T t = sink.get(); PmQueueWriter<O> w1 = openQueue(sink, t);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture()),
                                    source.iterator(s), i -> worker.process(i, w1::write));
                        }   // closing writer queues
                    }
                }.start();
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2>
    IterableLoop2<I, O1, O2> into(SinkResource<T1, O1> sink1, SinkResource<T2, O2> sink2) {
        return new IterableLoop2<>() {

            @Override
            public void forEach(Function<? super I, ? extends Tuple2<? extends O1, ? extends O2>> transformer) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     S s = source.get()) {
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QT2.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
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
                new DoParallelFair<Tuple2<? extends O1, ? extends O2>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachParallel(int maxThread, Function<? super I, ? extends Tuple2<? extends O1, ? extends O2>> transformer) {
                new DoParallelRaw<Tuple2<? extends O1, ? extends O2>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends Tuple2<? extends O1, ? extends O2>>> asyncTransformer) {
                new DoAsyncParallelFair<>(maxThread, asyncTransformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(int maxThread, AsyncWorker2<? super I, Consumer<? super O1>, Consumer<? super O2>> asyncWorker) {
                new DoAsyncWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture()),
                                    source.iterator(s), i -> asyncWorker.apply(i, w1::write, w2::write));
                        }   // closing writer queues
                    }
                }.start();
            }

            @Override
            public void forEach(Worker2<? super I, Consumer<? super O1>, Consumer<? super O2>> worker) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     S s = source.get()) {
                    Consumer<? super O1> w1 = sink1.consumer(t1);
                    Consumer<? super O2> w2 = sink2.consumer(t2);
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QW2.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        worker.process(i, w1, w2);
                    }
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public ParallelLoop2<I, O1, O2> shutdownTimeout(long time, TimeUnit unit) {
                setShutdownTimeout(time, unit);
                return this;
            }

            @Override
            public void forEachParallel(
                    int maxThread,
                    Worker2<? super I,
                            Consumer<? super O1>,
                            Consumer<? super O2>> worker) {
                new DoParallelWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture()),
                                    source.iterator(s), i -> worker.process(i, w1::write, w2::write));
                        }   // closing writer queues
                    }
                }.start();
            }
        };
    }

    private void setShutdownTimeout(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3>
    IterableLoop3<I, O1, O2, O3> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3) {
        return new IterableLoop3<>() {

            @Override
            public void forEach(Function<? super I, ? extends Tuple3<? extends O1, ? extends O2, ? extends O3>> transformer) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     S s = source.get()) {
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QT3.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
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
                new DoParallelFair<Tuple3<? extends O1, ? extends O2, ? extends O3>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
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
                new DoParallelRaw<Tuple3<? extends O1, ? extends O2, ? extends O3>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
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
                new DoAsyncParallelFair<>(maxThread, asyncTransformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
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
                new DoAsyncWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture()),
                                    source.iterator(s), i -> asyncWorker.apply(i, w1::write, w2::write, w3::write));
                        }   // closing writer queues
                    }
                }.start();
            }

            @Override
            public void forEach(Worker3<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>> worker) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     S s = source.get()) {
                    Consumer<? super O1> w1 = sink1.consumer(t1);
                    Consumer<? super O2> w2 = sink2.consumer(t2);
                    Consumer<? super O3> w3 = sink3.consumer(t3);
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QW3.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        worker.process(i, w1, w2, w3);
                    }
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public ParallelLoop3<I, O1, O2, O3> shutdownTimeout(long time, TimeUnit unit) {
                setShutdownTimeout(time, unit);
                return this;
            }

            public void forEachParallel(
                    int maxThread,
                    Worker3<? super I,
                            Consumer<? super O1>,
                            Consumer<? super O2>,
                            Consumer<? super O3>> worker) {
                new DoParallelWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture()),
                                    source.iterator(s), i -> worker.process(i, w1::write, w2::write, w3::write));
                        }   // closing writer queues
                    }
                }.start();
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4>
    IterableLoop4<I, O1, O2, O3, O4> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4) {
        return new IterableLoop4<>() {
            @Override
            public void forEach(Function<? super I, ? extends Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4>> transformer) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     S s = source.get()) {
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QT4.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        val oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                    }
                } catch (BatchException e) {
                    throw e;
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4>> transformer) {
                new DoParallelFair<Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachParallel(int maxThread, Function<? super I, ? extends Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4>> transformer) {
                new DoParallelRaw<Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4>>> asyncTransformer) {
                new DoAsyncParallelFair<>(maxThread, asyncTransformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(int maxThread, AsyncWorker4<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>, Consumer<? super O4>> asyncWorker) {
                new DoAsyncWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture()),
                                    source.iterator(s), i -> asyncWorker.apply(i, w1::write, w2::write, w3::write, w4::write));
                        }   // closing writer queues
                    }
                }.start();
            }

            @Override
            public void forEach(Worker4<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>, Consumer<? super O4>> worker) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     S s = source.get()) {
                    Consumer<? super O1> w1 = sink1.consumer(t1);
                    Consumer<? super O2> w2 = sink2.consumer(t2);
                    Consumer<? super O3> w3 = sink3.consumer(t3);
                    Consumer<? super O4> w4 = sink4.consumer(t4);
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QW4.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        worker.process(i, w1, w2, w3, w4);
                    }
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public ParallelLoop4<I, O1, O2, O3, O4> shutdownTimeout(long time, TimeUnit unit) {
                setShutdownTimeout(time, unit);
                return this;
            }

            @Override
            public void forEachParallel(
                    int maxThread,
                    Worker4<? super I,
                            Consumer<? super O1>,
                            Consumer<? super O2>,
                            Consumer<? super O3>,
                            Consumer<? super O4>> worker) {
                new DoParallelWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture()),
                                    source.iterator(s),
                                    i -> worker.process(i, w1::write, w2::write, w3::write, w4::write));
                        }   // closing writer queues
                    }
                }.start();
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5>
    IterableLoop5<I, O1, O2, O3, O4, O5> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5) {
        return new IterableLoop5<>() {
            @Override
            public void forEach(Function<? super I,
                    ? extends Tuple5<? extends O1,
                            ? extends O2,
                            ? extends O3,
                            ? extends O4,
                            ? extends O5>> transformer) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     T5 t5 = sink5.get();
                     S s = source.get()) {
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QT5.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        val oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
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
                            ? extends Tuple5<
                                    ? extends O1,
                                    ? extends O2,
                                    ? extends O3,
                                    ? extends O4,
                                    ? extends O5>> transformer) {
                new DoParallelFair<Tuple5<? extends O1, ? extends O2, ? extends O3, ? extends O4,
                        ? extends O5>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                                oo.onT5(v -> sink5.accept(t5, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachParallel(int maxThread, Function<? super I, ? extends Tuple5<? extends O1, ? extends O2, ? extends O3, ? extends O4, ? extends O5>> transformer) {
                new DoParallelRaw<Tuple5<? extends O1, ? extends O2, ? extends O3, ? extends O4,
                        ? extends O5>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                                oo.onT5(v -> sink5.accept(t5, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends Tuple5<? extends O1, ? extends O2, ? extends O3, ? extends O4, ? extends O5>>> asyncTransformer) {
                new DoAsyncParallelFair<>(maxThread, asyncTransformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                                oo.onT5(v -> sink5.accept(t5, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(
                    int maxThread,
                    AsyncWorker5<? super I,
                            Consumer<? super O1>,
                            Consumer<? super O2>,
                            Consumer<? super O3>,
                            Consumer<? super O4>,
                            Consumer<? super O5>> asyncWorker) {
                new DoAsyncWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                            w5.getFuture()),
                                    source.iterator(s), i -> asyncWorker.apply(i,
                                            w1::write, w2::write, w3::write, w4::write,
                                            w5::write));
                        }   // closing writer queues
                    }
                }.start();
            }

            @Override
            public void forEach(Worker5<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>, Consumer<? super O4>, Consumer<? super O5>> worker) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     T5 t5 = sink5.get();
                     S s = source.get()) {
                    Consumer<? super O1> w1 = sink1.consumer(t1);
                    Consumer<? super O2> w2 = sink2.consumer(t2);
                    Consumer<? super O3> w3 = sink3.consumer(t3);
                    Consumer<? super O4> w4 = sink4.consumer(t4);
                    Consumer<? super O5> w5 = sink5.consumer(t5);
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QW5.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        worker.process(i, w1, w2, w3, w4, w5);
                    }
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public ParallelLoop5<I, O1, O2, O3, O4, O5> shutdownTimeout(long time, TimeUnit unit) {
                setShutdownTimeout(time, unit);
                return this;
            }

            @Override
            public void forEachParallel(
                    int maxThread,
                    Worker5<? super I,
                            Consumer<? super O1>,
                            Consumer<? super O2>,
                            Consumer<? super O3>,
                            Consumer<? super O4>,
                            Consumer<? super O5>> worker) {
                new DoParallelWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                            w5.getFuture()),
                                    source.iterator(s),
                                    i -> worker.process(i, w1::write, w2::write, w3::write, w4::write,
                                            w5::write));
                        }   // closing writer queues
                    }
                }.start();
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6>
    IterableLoop6<I, O1, O2, O3, O4, O5, O6> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6) {
        return new IterableLoop6<>() {
            @Override
            public void forEach(
                    Function<? super I,
                            ? extends Tuple6<
                                    ? extends O1,
                                    ? extends O2,
                                    ? extends O3,
                                    ? extends O4,
                                    ? extends O5,
                                    ? extends O6>> transformer) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     T5 t5 = sink5.get();
                     T6 t6 = sink6.get();
                     S s = source.get()) {
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QT6.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        val oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
                        oo.onT6(v -> sink6.accept(t6, v));
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
                            ? extends Tuple6<
                                    ? extends O1,
                                    ? extends O2,
                                    ? extends O3,
                                    ? extends O4,
                                    ? extends O5,
                                    ? extends O6>> transformer) {
                new DoParallelFair<Tuple6<? extends O1, ? extends O2, ? extends O3, ? extends O4,
                        ? extends O5, ? extends O6>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             T6 t6 = sink6.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                                oo.onT5(v -> sink5.accept(t5, v));
                                oo.onT6(v -> sink6.accept(t6, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachParallel(int maxThread, Function<? super I, ? extends Tuple6<? extends O1, ? extends O2, ? extends O3, ? extends O4, ? extends O5, ? extends O6>> transformer) {
                new DoParallelRaw<Tuple6<? extends O1, ? extends O2, ? extends O3, ? extends O4,
                        ? extends O5, ? extends O6>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             T6 t6 = sink6.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                                oo.onT5(v -> sink5.accept(t5, v));
                                oo.onT6(v -> sink6.accept(t6, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends Tuple6<? extends O1, ? extends O2, ? extends O3, ? extends O4, ? extends O5, ? extends O6>>> asyncTransformer) {
                new DoAsyncParallelFair<>(maxThread, asyncTransformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             T6 t6 = sink6.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                                oo.onT5(v -> sink5.accept(t5, v));
                                oo.onT6(v -> sink6.accept(t6, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(
                    int maxThread,
                    AsyncWorker6<? super I,
                            Consumer<? super O1>,
                            Consumer<? super O2>,
                            Consumer<? super O3>,
                            Consumer<? super O4>,
                            Consumer<? super O5>,
                            Consumer<? super O6>> asyncWorker) {
                new DoAsyncWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                             T6 t6 = sink6.get(); PmQueueWriter<O6> w6 = openQueue(sink6, t6);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                            w5.getFuture(), w6.getFuture()),
                                    source.iterator(s), i -> asyncWorker.apply(i,
                                            w1::write, w2::write, w3::write, w4::write,
                                            w5::write, w6::write));
                        }   // closing writer queues
                    }
                }.start();
            }

            @Override
            public void forEach(Worker6<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>, Consumer<? super O4>, Consumer<? super O5>, Consumer<? super O6>> worker) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     T5 t5 = sink5.get();
                     T6 t6 = sink6.get();
                     S s = source.get()) {
                    Consumer<? super O1> w1 = sink1.consumer(t1);
                    Consumer<? super O2> w2 = sink2.consumer(t2);
                    Consumer<? super O3> w3 = sink3.consumer(t3);
                    Consumer<? super O4> w4 = sink4.consumer(t4);
                    Consumer<? super O5> w5 = sink5.consumer(t5);
                    Consumer<? super O6> w6 = sink6.consumer(t6);
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QW6.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        worker.process(i, w1, w2, w3, w4, w5, w6);
                    }
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public ParallelLoop6<I, O1, O2, O3, O4, O5, O6> shutdownTimeout(long time, TimeUnit unit) {
                setShutdownTimeout(time, unit);
                return this;
            }

            @Override
            public void forEachParallel(
                    int maxThread,
                    Worker6<? super I,
                            Consumer<? super O1>,
                            Consumer<? super O2>,
                            Consumer<? super O3>,
                            Consumer<? super O4>,
                            Consumer<? super O5>,
                            Consumer<? super O6>> worker) {
                new DoParallelWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                             T6 t6 = sink6.get(); PmQueueWriter<O6> w6 = openQueue(sink6, t6);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                            w5.getFuture(), w6.getFuture()),
                                    source.iterator(s),
                                    i -> worker.process(i, w1::write, w2::write, w3::write, w4::write,
                                            w5::write, w6::write));
                        }   // closing writer queues
                    }
                }.start();
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6,
            T7 extends AutoCloseable, O7>
    IterableLoop7<I, O1, O2, O3, O4, O5, O6, O7> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7) {
        return new IterableLoop7<>() {
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
                     S s = source.get()) {
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QT7.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
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
                new DoParallelFair<Tuple7<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
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
                new DoParallelRaw<Tuple7<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
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
                new DoAsyncParallelFair<>(maxThread, asyncTransformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             T6 t6 = sink6.get();
                             T7 t7 = sink7.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
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
                new DoAsyncWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                             T6 t6 = sink6.get(); PmQueueWriter<O6> w6 = openQueue(sink6, t6);
                             T7 t7 = sink7.get(); PmQueueWriter<O7> w7 = openQueue(sink7, t7);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                            w5.getFuture(), w6.getFuture(), w7.getFuture()),
                                    source.iterator(s), i -> asyncWorker.apply(i,
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
                     S s = source.get()) {
                    Consumer<? super O1> w1 = sink1.consumer(t1);
                    Consumer<? super O2> w2 = sink2.consumer(t2);
                    Consumer<? super O3> w3 = sink3.consumer(t3);
                    Consumer<? super O4> w4 = sink4.consumer(t4);
                    Consumer<? super O5> w5 = sink5.consumer(t5);
                    Consumer<? super O6> w6 = sink6.consumer(t6);
                    Consumer<? super O7> w7 = sink7.consumer(t7);
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QW7.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        worker.process(i, w1, w2, w3, w4, w5, w6, w7);
                    }
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public ParallelLoop7<I, O1, O2, O3, O4, O5, O6, O7> shutdownTimeout(long time, TimeUnit unit) {
                setShutdownTimeout(time, unit);
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
                new DoParallelWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                             T6 t6 = sink6.get(); PmQueueWriter<O6> w6 = openQueue(sink6, t6);
                             T7 t7 = sink7.get(); PmQueueWriter<O7> w7 = openQueue(sink7, t7);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                            w5.getFuture(), w6.getFuture(), w7.getFuture()),
                                    source.iterator(s),
                                    i -> worker.process(i, w1::write, w2::write, w3::write, w4::write,
                                            w5::write, w6::write, w7::write));
                        }   // closing writer queues
                    }
                }.start();
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6,
            T7 extends AutoCloseable, O7,
            T8 extends AutoCloseable, O8>
    IterableLoop8<I, O1, O2, O3, O4, O5, O6, O7, O8> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7,
            SinkResource<T8, O8> sink8) {
        return new IterableLoop8<>() {
            @Override
            public void forEach(
                    Function<? super I,
                            ? extends Tuple8<
                                    ? extends O1,
                                    ? extends O2,
                                    ? extends O3,
                                    ? extends O4,
                                    ? extends O5,
                                    ? extends O6,
                                    ? extends O7,
                                    ? extends O8>> transformer) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     T5 t5 = sink5.get();
                     T6 t6 = sink6.get();
                     T7 t7 = sink7.get();
                     T8 t8 = sink8.get();
                     S s = source.get()) {
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QT8.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        val oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
                        oo.onT6(v -> sink6.accept(t6, v));
                        oo.onT7(v -> sink7.accept(t7, v));
                        oo.onT8(v -> sink8.accept(t8, v));
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
                            ? extends Tuple8<
                                    ? extends O1,
                                    ? extends O2,
                                    ? extends O3,
                                    ? extends O4,
                                    ? extends O5,
                                    ? extends O6,
                                    ? extends O7,
                                    ? extends O8>> transformer) {
                new DoParallelFair<Tuple8<? extends O1, ? extends O2, ? extends O3, ? extends O4,
                        ? extends O5, ? extends O6, ? extends O7, ? extends O8>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             T6 t6 = sink6.get();
                             T7 t7 = sink7.get();
                             T8 t8 = sink8.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                                oo.onT5(v -> sink5.accept(t5, v));
                                oo.onT6(v -> sink6.accept(t6, v));
                                oo.onT7(v -> sink7.accept(t7, v));
                                oo.onT8(v -> sink8.accept(t8, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachParallel(int maxThread, Function<? super I, ? extends Tuple8<? extends O1, ? extends O2, ? extends O3, ? extends O4, ? extends O5, ? extends O6, ? extends O7, ? extends O8>> transformer) {
                new DoParallelRaw<Tuple8<? extends O1, ? extends O2, ? extends O3, ? extends O4,
                        ? extends O5, ? extends O6, ? extends O7, ? extends O8>>(maxThread, transformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             T6 t6 = sink6.get();
                             T7 t7 = sink7.get();
                             T8 t8 = sink8.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                                oo.onT5(v -> sink5.accept(t5, v));
                                oo.onT6(v -> sink6.accept(t6, v));
                                oo.onT7(v -> sink7.accept(t7, v));
                                oo.onT8(v -> sink8.accept(t8, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(int maxThread, Function<? super I, ? extends Future<? extends Tuple8<? extends O1, ? extends O2, ? extends O3, ? extends O4, ? extends O5, ? extends O6, ? extends O7, ? extends O8>>> asyncTransformer) {
                new DoAsyncParallelFair<>(maxThread, asyncTransformer) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get();
                             T2 t2 = sink2.get();
                             T3 t3 = sink3.get();
                             T4 t4 = sink4.get();
                             T5 t5 = sink5.get();
                             T6 t6 = sink6.get();
                             T7 t7 = sink7.get();
                             T8 t8 = sink8.get();
                             S s = source.get()) {
                            doWork(source.iterator(s), oo -> {
                                oo.onT1(v -> sink1.accept(t1, v));
                                oo.onT2(v -> sink2.accept(t2, v));
                                oo.onT3(v -> sink3.accept(t3, v));
                                oo.onT4(v -> sink4.accept(t4, v));
                                oo.onT5(v -> sink5.accept(t5, v));
                                oo.onT6(v -> sink6.accept(t6, v));
                                oo.onT7(v -> sink7.accept(t7, v));
                                oo.onT8(v -> sink8.accept(t8, v));
                            });
                        }
                    }
                }.start();
            }

            @Override
            public void forEachAsync(
                    int maxThread,
                    AsyncWorker8<? super I,
                            Consumer<? super O1>,
                            Consumer<? super O2>,
                            Consumer<? super O3>,
                            Consumer<? super O4>,
                            Consumer<? super O5>,
                            Consumer<? super O6>,
                            Consumer<? super O7>,
                            Consumer<? super O8>> asyncWorker) {
                new DoAsyncWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                             T6 t6 = sink6.get(); PmQueueWriter<O6> w6 = openQueue(sink6, t6);
                             T7 t7 = sink7.get(); PmQueueWriter<O7> w7 = openQueue(sink7, t7);
                             T8 t8 = sink8.get(); PmQueueWriter<O8> w8 = openQueue(sink8, t8);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                            w5.getFuture(), w6.getFuture(), w7.getFuture(), w8.getFuture()),
                                    source.iterator(s), i -> asyncWorker.apply(i,
                                            w1::write, w2::write, w3::write, w4::write,
                                            w5::write, w6::write, w7::write, w8::write));
                        }   // closing writer queues
                    }
                }.start();
            }

            @Override
            public void forEach(Worker8<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>, Consumer<? super O4>, Consumer<? super O5>, Consumer<? super O6>, Consumer<? super O7>, Consumer<? super O8>> worker) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     T4 t4 = sink4.get();
                     T5 t5 = sink5.get();
                     T6 t6 = sink6.get();
                     T7 t7 = sink7.get();
                     T8 t8 = sink8.get();
                     S s = source.get()) {
                    Consumer<? super O1> w1 = sink1.consumer(t1);
                    Consumer<? super O2> w2 = sink2.consumer(t2);
                    Consumer<? super O3> w3 = sink3.consumer(t3);
                    Consumer<? super O4> w4 = sink4.consumer(t4);
                    Consumer<? super O5> w5 = sink5.consumer(t5);
                    Consumer<? super O6> w6 = sink6.consumer(t6);
                    Consumer<? super O7> w7 = sink7.consumer(t7);
                    Consumer<? super O8> w8 = sink8.consumer(t8);
                    Iterator<I> iterator = source.iterator(s);
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("QW8.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        worker.process(i, w1, w2, w3, w4, w5, w6, w7, w8);
                    }
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public ParallelLoop8<I, O1, O2, O3, O4, O5, O6, O7, O8> shutdownTimeout(long time, TimeUnit unit) {
                setShutdownTimeout(time, unit);
                return this;
            }

            @Override
            public void forEachParallel(
                    int maxThread,
                    Worker8<? super I,
                            Consumer<? super O1>,
                            Consumer<? super O2>,
                            Consumer<? super O3>,
                            Consumer<? super O4>,
                            Consumer<? super O5>,
                            Consumer<? super O6>,
                            Consumer<? super O7>,
                            Consumer<? super O8>> worker) {
                new DoParallelWrite(maxThread) {
                    @Override
                    protected void openResources() throws Exception {
                        try (T1 t1 = sink1.get(); PmQueueWriter<O1> w1 = openQueue(sink1, t1);
                             T2 t2 = sink2.get(); PmQueueWriter<O2> w2 = openQueue(sink2, t2);
                             T3 t3 = sink3.get(); PmQueueWriter<O3> w3 = openQueue(sink3, t3);
                             T4 t4 = sink4.get(); PmQueueWriter<O4> w4 = openQueue(sink4, t4);
                             T5 t5 = sink5.get(); PmQueueWriter<O5> w5 = openQueue(sink5, t5);
                             T6 t6 = sink6.get(); PmQueueWriter<O6> w6 = openQueue(sink6, t6);
                             T7 t7 = sink7.get(); PmQueueWriter<O7> w7 = openQueue(sink7, t7);
                             T8 t8 = sink8.get(); PmQueueWriter<O8> w8 = openQueue(sink8, t8);
                             S s = source.get()) {
                            doWork(List.of(w1.getFuture(), w2.getFuture(), w3.getFuture(), w4.getFuture(),
                                            w5.getFuture(), w6.getFuture(), w7.getFuture(), w8.getFuture()),
                                    source.iterator(s),
                                    i -> worker.process(i, w1::write, w2::write, w3::write, w4::write,
                                            w5::write, w6::write, w7::write, w8::write));
                        }   // closing writer queues
                    }
                }.start();
            }
        };
    }

    @Override
    public ParallelLoop0<I> shutdownTimeout(long time, TimeUnit unit) {
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
        new DoAsyncParallelFair<>(maxThread, asyncTransformer) {
            @Override
            protected void openResources() throws Exception {
                try (S s = source.get()) {
                    doWork(source.iterator(s), oo -> {
                    });
                }
            }
        }.start();
    }

    private void shutdown(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(time, unit)) {
                log.warn("### shutdown timeout expired, I terminate still active tasks");
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    /**
     * monitors list of future's writer listener (invoked from task scheduler)
     *
     * @param main     main thread (to be interrupted on listener exception)
     * @param statuses list of futures
     */
    private void monitor(Thread main, List<Future<?>> statuses) {
        for (val status : statuses) {
            if (status.isDone()) {
                try {
                    status.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String place = placeOf(cause.getStackTrace());
                    log.warn("W.### Error detected in the listener: {} [{}]", cause.getMessage(), place);
                    main.interrupt();
                    return;
                }
            }
        }
        log.debug("W.--- The state of the listeners is healthy");
    }

    private void probeStatuses(List<Future<?>> statuses, boolean hot) {
        int k = 0;
        do {
            int n = iterateStatus(statuses.iterator());
            if (hot || statuses.isEmpty()) {
                return;
            }
            sendMessage(++k, n);
            try {
                // if task is interrupted: sleep do nothing
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                try {
                    // now task IS NOT interrupted and sleep() DO SLEEP
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                Thread.currentThread().interrupt();
            }
        } while (true);
    }

    private void sendMessage(int j, int r) {
        if ((j & 0x0fff) != 0) {
            int n = 0;
            while (j != 0) {
                if ((j & 0x01) == 1) n++;
                j >>= 1;
            }
            if (n != 1) return;
        }
        log.debug("*.--- Waiting {} running task", r);
    }

    private int iterateStatus(Iterator<Future<?>> it) {
        int k = 0;
        while (it.hasNext()) {
            Future<?> status = it.next();
            if (status.isDone() /*|| status.isCancelled()*/) {
                try {
                    status.get();
                } catch (InterruptedException e) {
                    log.info("*.>>> task was interrupted. (dead branch?)");
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String place = placeOf(cause.getStackTrace());
                    log.warn("*.### Error detected in task execution: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                }
                it.remove();
            } else {
                k++;
            }
        }
        return k;
    }

    private abstract class DoAsyncParallelFair<R> {
        private final Function<? super I, ? extends Future<? extends R>> transformer;
        private final Thread main;
        private final BlockingQueue<Future<? extends R>> queue;
        private final Semaphore sm;

        DoAsyncParallelFair(int maxThread, Function<? super I, ? extends Future<? extends R>> asyncTransformer) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.transformer = asyncTransformer;
            this.main = Thread.currentThread();
            this.queue = new ArrayBlockingQueue<>(maxThread, true);
            this.sm = new Semaphore(maxThread);
        }

        public void start() {
            try {
                openResources();
                log.info("s.=== completed successfully.");
            } catch (BatchException e) {
                log.error("s.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("s.### abnormal program end.", e);
                throw new BatchException(e);
            }
        }

        protected abstract void openResources() throws Exception;

        protected void doWork(Iterator<I> iterator, Consumer<R> action) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            String jobName = MDC.get(JOB_NAME);
            String stepName = MDC.get(STEP_NAME);
            try {
                Future<?> future = service1.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    try {
                        while (iterator.hasNext()) {
                            I i = iterator.next();
                            if (terminateTest != null && terminateTest.test(i)) {
                                log.warn("s.>>> Loop terminated on condition");
                                break;
                            }
                            if (beforeAction != null) beforeAction.run();
                            sm.acquire();
                            addToQueue(transformer.apply(i));
                        }
                        if (Thread.currentThread().isInterrupted()) {
                            log.warn("s.>>> Loop interrupted ...");
                        } else {
                            log.info("s.--- All task submitted ...");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (RuntimeException e) {
                        log.warn("s.>>> Internal error.");
                        throw e;
                    } finally {
                        log.info("s.--- waiting for the end of the tasks ...");
                        awaitEmptyQueue();
                        if (!Thread.currentThread().isInterrupted()) {
                            log.info("s.--- interrupting the write listener");
                            main.interrupt();
                        }
                        MDC.clear();
                    }
                });
                try {
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        Future<? extends R> fo = queue.take();
                        doWrite(action, fo, future);
                    }
                } catch (InterruptedException e) {
                    log.info("s.--- write listener interrupted");
//                    Thread.currentThread().interrupt();
                }
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String place = placeOf(cause.getStackTrace());
                    log.warn("s.### Error detected in reader: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                service1.shutdown();
            }
        }

        private void doWrite(Consumer<R> action, Future<? extends R> fo, Future<?> future) throws InterruptedException {
            try {
                R oo = fo.get();
                action.accept(oo);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                String place = placeOf(cause.getStackTrace());
                log.warn("s.### Error detected in task execution: {} [{}]", cause.getMessage(), place);
                future.cancel(true);
                throw new BatchException(cause);
            } finally {
                sm.release();
            }
        }

        private void addToQueue(Future<? extends R> promise) {
            try {
                queue.put(promise);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void awaitEmptyQueue() {
            while (!queue.isEmpty()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private abstract class DoParallelFair<R> {
        private final int maxThread;
        private final Function<? super I, ? extends R> transformer;
        private final Thread main;
        private final BlockingQueue<Future<? extends R>> queue;

        DoParallelFair(int maxThread, Function<? super I, ? extends R> transformer) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.maxThread = maxThread;
            this.transformer = transformer;
            this.main = Thread.currentThread();
            this.queue = new ArrayBlockingQueue<>(maxThread, true);
        }

        public void start() {
            try {
                openResources();
                log.info("S.=== completed successfully.");
            } catch (BatchException e) {
                log.error("S.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("S.### abnormal program end.", e);
                throw new BatchException(e);
            }
        }

        protected abstract void openResources() throws Exception;

        protected void doWork(Iterator<I> iterator, Consumer<R> action) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            String jobName = MDC.get(JOB_NAME);
            String stepName = MDC.get(STEP_NAME);
            try {
                Future<?> future = service1.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    ExecutorService service2 = Executors.newFixedThreadPool(maxThread);
                    Semaphore sm = new Semaphore(maxThread);
                    try {
                        while (iterator.hasNext()) {
                            I i = iterator.next();
                            if (terminateTest != null && terminateTest.test(i)) {
                                log.warn("S.>>> Loop terminated on condition");
                                break;
                            }
                            if (beforeAction != null) beforeAction.run();
                            try {
                                sm.acquire();
                                Future<? extends R> promise = service2.submit(() -> {
                                    MDC.put(JOB_NAME, jobName);
                                    MDC.put(STEP_NAME, stepName);
                                    try {
                                        return transformer.apply(i);
                                    } finally {
                                        sm.release();
                                        MDC.clear();
                                    }
                                });
                                addToQueue(promise);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                        if (Thread.currentThread().isInterrupted()) {
                            log.warn("S.>>> Loop interrupted, shutdown taskExecutor ...");
                        } else {
                            log.info("S.--- All task submitted, shutdown taskExecutor ...");
                        }
                    } catch (RuntimeException e) {
                        log.warn("S.>>> Internal error.");
                        throw e;
                    } finally {
                        shutdown(service2);
                        log.info("S.--- waiting for the end of the tasks ...");
                        awaitEmptyQueue();
                        MDC.clear();
                        if (!Thread.currentThread().isInterrupted()) {
                            log.info("S.--- interrupting the write listener");
                            main.interrupt();
                        }
                    }
                });
                try {
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        Future<? extends R> fo = queue.take();
                        doWrite(action, fo, future);
                    }
                } catch (InterruptedException e) {
                    log.info("S.--- write listener interrupted");
//                    Thread.currentThread().interrupt();
                }
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String place = placeOf(cause.getStackTrace());
                    log.warn("S.### Error detected in reader: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                service1.shutdown();
            }
        }

        private void doWrite(Consumer<R> action, Future<? extends R> fo, Future<?> future) throws InterruptedException {
            try {
                R oo = fo.get();
                action.accept(oo);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                String place = placeOf(cause.getStackTrace());
                log.warn("S.### Error detected in task execution: {} [{}]", cause.getMessage(), place);
                future.cancel(true);
                throw new BatchException(cause);
            }
        }

        private void addToQueue(Future<? extends R> promise) {
            try {
                queue.put(promise);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void awaitEmptyQueue() {
            while (!queue.isEmpty()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private abstract class DoParallelWrite {
        private final int maxThread;
        private final ExecutorService writerService;

        public DoParallelWrite(int maxThread) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.maxThread = maxThread;
            this.writerService = Executors.newCachedThreadPool();
        }

        public void start() {
            try {
                doOpen();
            } catch (BatchException e) {
                log.error("W.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("W.### abnormal program end.", e);
                throw new BatchException(e);
            } finally {
                writerService.shutdown();
            }
        }

        private void doOpen() throws Exception {
            try {
                openResources();
                log.info("W.=== completed successfully.");
            } catch (InterruptedException e) {
                log.info("W.>>> thread interrupted");
            } finally {
                log.info("W.--- resources closed.");
            }
        }

        protected <T extends AutoCloseable, O> PmQueueWriter<O> openQueue(SinkResource<T, O> sink, T t) {
            return PmQueueWriter.of(maxThread, writerService, sink, t);
        }

        protected void doWork(
                List<Future<?>> writerStatuses,
                Iterator<I> iterator, Consumer<I> task) {
            Thread main = Thread.currentThread();
            ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
            schedule.scheduleAtFixedRate(() -> monitor(main, writerStatuses), 5, 5, TimeUnit.SECONDS);
            try {
                List<Future<?>> statuses = new LinkedList<>();
                ExecutorService taskService = Executors.newFixedThreadPool(maxThread);
                Semaphore sm = new Semaphore(maxThread);
                try {
                    while (iterator.hasNext()) {
                        I i = iterator.next();
                        if (terminateTest != null && terminateTest.test(i)) {
                            log.warn("W.>>> Loop terminated on condition");
                            break;
                        }
                        if (beforeAction != null) beforeAction.run();
                        Future<?> status = start(taskService, sm, () -> task.accept(i));
                        if (status == null) break;
                        statuses.add(status);
                        probeStatuses(statuses, true);
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        log.warn("W.>>> Loop interrupted, shutdown taskExecutor ...");
                    } else {
                        log.info("W.--- All task submitted, shutdown taskExecutor ...");
                    }
                } catch (RuntimeException e) {
                    log.warn("W.>>> Internal error.");
                    throw e;
                } finally {
                    shutdown(taskService);  // abnormal end on timeout
                }
                log.info("W.--- pending futures {}", statuses.size());
                probeStatuses(statuses, false);
                log.info("W.--- tasks terminated, flush & close ...");
            } finally {
                log.debug("W.--- the listener's monitor will be shut down ...");
                schedule.shutdown();
            }
        }

        protected abstract void openResources() throws Exception;

        private Future<?> start(ExecutorService taskService, Semaphore sm, Runnable runnable) {
            try {
                log.trace("··· task ready to be submitted");
                sm.acquire();
                log.trace("··· task going to be submitted");
                String jobName = MDC.get(JOB_NAME);
                String stepName = MDC.get(STEP_NAME);
                return taskService.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    try {
                        log.trace("··· task entry.");
                        runnable.run();
                    } finally {
                        sm.release();
                        log.trace("··· task exit.");
                        MDC.clear();
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }

    private abstract class DoParallelRaw<R> {
        private final int maxThread;
        private final Function<? super I, ? extends R> transformer;
        private final Thread main;
        private final BlockingQueue<R> queue;

        DoParallelRaw(int maxThread, Function<? super I, ? extends R> transformer) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.maxThread = maxThread;
            this.transformer = transformer;
            this.main = Thread.currentThread();
            this.queue = new ArrayBlockingQueue<>(maxThread, true);
        }

        public void start() {
            try {
                openResources();
                log.info("E.=== completed successfully.");
            } catch (BatchException e) {
                log.error("E.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("E.### abnormal program end.", e);
                throw new BatchException(e);
            }
        }

        protected abstract void openResources() throws Exception;

        protected void doWork(Iterator<I> iterator, Consumer<R> action) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            String jobName = MDC.get(JOB_NAME);
            String stepName = MDC.get(STEP_NAME);
            try {
                Future<?> future = service1.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    ExecutorService service2 = Executors.newFixedThreadPool(maxThread);
                    Semaphore sm = new Semaphore(maxThread);
                    try {
                        List<Future<?>> statuses = new LinkedList<>();
                        try {
                            while (iterator.hasNext()) {
                                I i = iterator.next();
                                if (terminateTest != null && terminateTest.test(i)) {
                                    log.warn("E.>>> Loop terminated on condition");
                                    break;
                                }
                                if (beforeAction != null) beforeAction.run();
                                try {
                                    sm.acquire();
                                    Future<?> status = service2.submit(() -> {
                                        MDC.put(JOB_NAME, jobName);
                                        MDC.put(STEP_NAME, stepName);
                                        try {
                                            queue.put(transformer.apply(i));
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                        } finally {
                                            sm.release();
                                            MDC.clear();
                                        }
                                    });
                                    statuses.add(status);
                                    probeStatuses(statuses, true);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                            if (Thread.currentThread().isInterrupted()) {
                                log.warn("E.>>> Loop interrupted, shutdown taskExecutor ...");
                            } else {
                                log.info("E.--- All task submitted, shutdown taskExecutor ...");
                            }
                        } catch (RuntimeException e) {
                            log.warn("E.>>> Internal error.");
                            throw e;
                        } finally {
                            shutdown(service2);
                        }
                        log.info("E.--- pending futures {}", statuses.size());
                        probeStatuses(statuses, false);
                        log.info("E.--- tasks terminated, flush & close ...");
                    } finally {
                        awaitEmptyQueue();
                        log.info("E.--- interrupting the write listener");
                        MDC.clear();
                        main.interrupt();
                    }
                });
                try {
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        R r = queue.take();
                        action.accept(r);
                    }
                } catch (InterruptedException e) {
                    log.info("E.--- write listener interrupted");
//                    Thread.currentThread().interrupt();
                }
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof BatchException) {
                        BatchException batchException = (BatchException) cause;
                        cause = batchException.getCause();
                        String place = placeOf(cause.getStackTrace());
                        log.warn("E.### Error forwarded in reader: {} [{}]", cause.getMessage(), place);
                        throw batchException;
                    }
                    String place = placeOf(cause.getStackTrace());
                    log.warn("E.### Error detected in reader: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                service1.shutdown();
            }
        }

        private void awaitEmptyQueue() {
            while (!queue.isEmpty()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private abstract class DoParallelSlim {
        private final int maxThread;
        private final Consumer<? super I> action;

        DoParallelSlim(int maxThread, Consumer<? super I> action) {
            this.maxThread = maxThread;
            this.action = action;
        }

        public void start() {
            try {
                openResources();
                log.info("Z.=== completed successfully.");
            } catch (BatchException e) {
                log.error("Z.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("Z.### abnormal program end.", e);
                throw new BatchException(e);
            }
        }

        protected abstract void openResources() throws Exception;

        protected void doWork(Iterator<I> iterator) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            String jobName = MDC.get(JOB_NAME);
            String stepName = MDC.get(STEP_NAME);
            try {
                Future<?> future = service1.submit(() -> {
                    MDC.put(JOB_NAME, jobName);
                    MDC.put(STEP_NAME, stepName);
                    ExecutorService service2 = Executors.newFixedThreadPool(maxThread);
                    Semaphore sm = new Semaphore(maxThread);
                    List<Future<?>> statuses = new LinkedList<>();
                    try {
                        while (iterator.hasNext()) {
                            I i = iterator.next();
                            if (terminateTest != null && terminateTest.test(i)) {
                                log.warn("Z.>>> Loop terminated on condition");
                                break;
                            }
                            if (beforeAction != null) beforeAction.run();
                            try {
                                sm.acquire();
                                Future<?> status = service2.submit(() -> {
                                    MDC.put(JOB_NAME, jobName);
                                    MDC.put(STEP_NAME, stepName);
                                    try {
                                        action.accept(i);
                                    } finally {
                                        sm.release();
                                        MDC.clear();
                                    }
                                });
                                statuses.add(status);
                                probeStatuses(statuses, true);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                        if (Thread.currentThread().isInterrupted()) {
                            log.warn("Z.>>> Loop interrupted, shutdown taskExecutor ...");
                        } else {
                            log.info("Z.--- All task submitted, shutdown taskExecutor ...");
                        }
                    } finally {
                        shutdown(service2);
                    }
                    log.info("Z.--- pending futures {}", statuses.size());
                    probeStatuses(statuses, false);
                    log.info("Z.--- tasks terminated, flush & close ...");
                    MDC.clear();
                });
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof BatchException) {
                        BatchException batchException = (BatchException) cause;
                        cause = batchException.getCause();
                        String place = placeOf(cause.getStackTrace());
                        log.warn("Z.### Error forwarded in reader: {} [{}]", cause.getMessage(), place);
                        throw batchException;
                    }
                    String place = placeOf(cause.getStackTrace());
                    log.warn("Z.### Error detected in reader: {} [{}]", cause.getMessage(), place);
                    throw new BatchException(cause);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                service1.shutdown();
            }
        }
    }

    private abstract class DoAsyncWrite {
        private final int maxThread;
        private final ExecutorService writerService;

        public DoAsyncWrite(int maxThread) {
            if (maxThread < 1)
                throw new IllegalArgumentException();
            this.maxThread = maxThread;
            this.writerService = Executors.newCachedThreadPool();
        }

        public void start() {
            try {
                doOpen();
            } catch (BatchException e) {
                log.error("w.### abnormal program end.", e);
                throw e;
            } catch (Exception e) {
                log.error("w.### abnormal program end.", e);
                throw new BatchException(e);
            } finally {
                writerService.shutdown();
            }
        }

        private void doOpen() throws Exception {
            try {
                openResources();
                log.info("w.=== completed successfully.");
            } catch (InterruptedException e) {
                log.info("w.>>> thread interrupted");
            } finally {
                log.info("w.--- resources closed.");
            }
        }

        protected <T extends AutoCloseable, O> PmQueueWriter<O> openQueue(SinkResource<T, O> sink, T t) {
            return PmQueueWriter.of(maxThread, writerService, sink, t);
        }

        protected void doWork(
                List<Future<?>> writerStatuses,
                Iterator<I> iterator, Function<I, Future<?>> task) {
            Thread main = Thread.currentThread();
            ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
            schedule.scheduleAtFixedRate(() -> monitor(main, writerStatuses), 5, 5, TimeUnit.SECONDS);
            try {
                final Semaphore sm = new Semaphore(maxThread);
                final List<Future<?>> statuses = new LinkedList<>();
                while (iterator.hasNext()) {
                    I i = iterator.next();
                    if (terminateTest != null && terminateTest.test(i)) {
                        log.warn("w.>>> Loop terminated on condition");
                        break;
                    }
                    if (beforeAction != null) beforeAction.run();
                    runTask(task, i, sm, statuses);
                }
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("w.>>> Loop interrupted, shutdown taskExecutor ...");
                } else {
                    log.info("w.--- All task submitted, shutdown taskExecutor ...");
                }
                log.info("w.--- pending futures {}", statuses.size());
                probeStatuses(statuses, sm, false);
                log.info("w.--- tasks terminated, flush & close ...");
            } catch (RuntimeException e) {
                log.warn("w.>>> Internal error.");
                throw e;
            } finally {
                log.debug("w.--- the listener's monitor will be shut down ...");
                schedule.shutdown();
            }
        }

        private void runTask(Function<I, Future<?>> task, I i, Semaphore sm, List<Future<?>> statuses) {
            try {
                while (!sm.tryAcquire(5, TimeUnit.MILLISECONDS)) {
                    probeStatuses(statuses, sm, true);
                }
                statuses.add(task.apply(i));
                probeStatuses(statuses, sm, true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void probeStatuses(List<Future<?>> statuses, Semaphore sm, boolean hot) {
            int k = 0;
            do {
                int n = iterateStatus(statuses.iterator(), sm);
                if (hot || statuses.isEmpty()) {
                    return;
                }
                sendMessage(++k, n);
                try {
                    // if task is interrupted: sleep do nothing
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    try {
                        // now task IS NOT interrupted and sleep() DO SLEEP
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    Thread.currentThread().interrupt();
                }
            } while (true);
        }

        private int iterateStatus(Iterator<Future<?>> it, Semaphore sm) {
            int k = 0;
            while (it.hasNext()) {
                Future<?> status = it.next();
                if (status.isDone() /*|| status.isCancelled()*/) {
                    try {
                        status.get();
                    } catch (InterruptedException e) {
                        log.info("w.>>> task was interrupted. (dead branch?)");
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        Throwable cause = e.getCause();
                        String place = placeOf(cause.getStackTrace());
                        log.warn("w.### Error detected in task execution: {} [{}]", cause.getMessage(), place);
                        throw new BatchException(cause);
                    } finally {
                        sm.release();
                    }
                    it.remove();
                } else {
                    k++;
                }
            }
            return k;
        }

        protected abstract void openResources() throws Exception;

    }
}
