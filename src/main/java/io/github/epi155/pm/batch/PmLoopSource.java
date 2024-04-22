package io.github.epi155.pm.batch;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.epi155.pm.batch.BatchException.placeOf;

@Slf4j
class PmLoopSource<S extends AutoCloseable, I> implements LoopSource<I> {
    private final SourceResource<S, I> source;
    private long time = 30;
    private TimeUnit unit = TimeUnit.SECONDS;

    PmLoopSource(SourceResource<S, I> source) {
        this.source = source;
    }

    @Override
    public <T extends AutoCloseable, O> IterableLoop<I, O> into(SinkResource<T, O> sink) {
        return new IterableLoop<>() {

            @Override
            public void forEach(Function<? super I, ? extends O> transformer) {
                try (T t = sink.get();
                     S s = source.get()) {
                    source.iterator(s).forEachRemaining(i -> sink.accept(t, transformer.apply(i)));
                } catch (BatchException e) {
                    throw e;
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            public void forEachParallelFair(int maxThread, Function<? super I, ? extends O> transformer) {
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelFair<I, O>(maxThread, transformer) {
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelRaw<I, O>(maxThread, transformer) {
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
            public ParallelLoop<I, O> shutdownTimeout(long time, TimeUnit unit) {
                setShutdownTimeout(time, unit);
                return this;
            }

            @Override
            public void forEach(Worker<? super I, Consumer<? super O>> worker) {
                try (T t = sink.get();
                     S s = source.get()) {
                    Consumer<? super O> wr = sink.consumer(t);
                    source.iterator(s).forEachRemaining(i -> worker.process(i, wr));
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public void forEachParallel(
                    int maxThread,
                    Worker<? super I, Consumer<? super O>> worker) {
                if (maxThread < 1)
                    throw new IllegalArgumentException();
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
                    source.iterator(s).forEachRemaining(i -> {
                        Tuple2<? extends O1, ? extends O2> oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                    });
                } catch (BatchException e) {
                    throw e;
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple2<? extends O1, ? extends O2>> transformer) {
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelFair<I, Tuple2<? extends O1, ? extends O2>>(maxThread, transformer) {
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelRaw<I, Tuple2<? extends O1, ? extends O2>>(maxThread, transformer) {
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
            public void forEach(Worker2<? super I, Consumer<? super O1>, Consumer<? super O2>> worker) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     S s = source.get()) {
                    Consumer<? super O1> w1 = sink1.consumer(t1);
                    Consumer<? super O2> w2 = sink2.consumer(t2);
                    source.iterator(s).forEachRemaining(i -> worker.process(i, w1, w2));
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
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
                    source.iterator(s).forEachRemaining(i -> {
                        Tuple3<? extends O1, ? extends O2, ? extends O3> oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                    });
                } catch (BatchException e) {
                    throw e;
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple3<? extends O1, ? extends O2, ? extends O3>> transformer) {
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelFair<I, Tuple3<? extends O1, ? extends O2, ? extends O3>>(maxThread, transformer) {
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelRaw<I, Tuple3<? extends O1, ? extends O2, ? extends O3>>(maxThread, transformer) {
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
            public void forEach(Worker3<? super I, Consumer<? super O1>, Consumer<? super O2>, Consumer<? super O3>> worker) {
                try (T1 t1 = sink1.get();
                     T2 t2 = sink2.get();
                     T3 t3 = sink3.get();
                     S s = source.get()) {
                    Consumer<? super O1> w1 = sink1.consumer(t1);
                    Consumer<? super O2> w2 = sink2.consumer(t2);
                    Consumer<? super O3> w3 = sink3.consumer(t3);
                    source.iterator(s).forEachRemaining(i -> worker.process(i, w1, w2, w3));
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
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
                    source.iterator(s).forEachRemaining(i -> {
                        Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4> oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                    });
                } catch (BatchException e) {
                    throw e;
                } catch (Exception e) {
                    throw new BatchException(e);
                }
            }

            @Override
            public void forEachParallelFair(int maxThread, Function<? super I, ? extends Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4>> transformer) {
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelFair<I, Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4>>(maxThread, transformer) {
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelRaw<I, Tuple4<? extends O1, ? extends O2, ? extends O3, ? extends O4>>(maxThread, transformer) {
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
                    source.iterator(s).forEachRemaining(i -> worker.process(i, w1, w2, w3, w4));
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
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
                    source.iterator(s).forEachRemaining(i -> {
                        Tuple5<? extends O1,
                                ? extends O2,
                                ? extends O3,
                                ? extends O4,
                                ? extends O5> oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
                    });
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelFair<I, Tuple5<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelRaw<I, Tuple5<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                    source.iterator(s).forEachRemaining(i -> worker.process(i, w1, w2, w3, w4, w5));
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
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
                    source.iterator(s).forEachRemaining(i -> {
                        Tuple6<? extends O1,
                                ? extends O2,
                                ? extends O3,
                                ? extends O4,
                                ? extends O5,
                                ? extends O6> oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
                        oo.onT6(v -> sink6.accept(t6, v));
                    });
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelFair<I, Tuple6<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelRaw<I, Tuple6<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                    source.iterator(s).forEachRemaining(i -> worker.process(i, w1, w2, w3, w4, w5, w6));
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
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
                    source.iterator(s).forEachRemaining(i -> {
                        Tuple7<? extends O1,
                                ? extends O2,
                                ? extends O3,
                                ? extends O4,
                                ? extends O5,
                                ? extends O6,
                                ? extends O7> oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
                        oo.onT6(v -> sink6.accept(t6, v));
                        oo.onT7(v -> sink7.accept(t7, v));
                    });
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelFair<I, Tuple7<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelRaw<I, Tuple7<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                    source.iterator(s).forEachRemaining(i -> worker.process(i, w1, w2, w3, w4, w5, w6, w7));
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
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
                    source.iterator(s).forEachRemaining(i -> {
                        Tuple8<? extends O1,
                                ? extends O2,
                                ? extends O3,
                                ? extends O4,
                                ? extends O5,
                                ? extends O6,
                                ? extends O7,
                                ? extends O8> oo = transformer.apply(i);
                        oo.onT1(v -> sink1.accept(t1, v));
                        oo.onT2(v -> sink2.accept(t2, v));
                        oo.onT3(v -> sink3.accept(t3, v));
                        oo.onT4(v -> sink4.accept(t4, v));
                        oo.onT5(v -> sink5.accept(t5, v));
                        oo.onT6(v -> sink6.accept(t6, v));
                        oo.onT7(v -> sink7.accept(t7, v));
                        oo.onT8(v -> sink8.accept(t8, v));
                    });
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelFair<I, Tuple8<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
                new DoParallelRaw<I, Tuple8<? extends O1, ? extends O2, ? extends O3, ? extends O4,
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
                    source.iterator(s).forEachRemaining(i -> worker.process(i, w1, w2, w3, w4, w5, w6, w7, w8));
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
                if (maxThread < 1)
                    throw new IllegalArgumentException();
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
    public void forEach(Consumer<? super I> action) {
        try (S s = source.get()) {
            source.iterator(s).forEachRemaining(action);
        } catch (BatchException e) {
            throw e;
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public void forEachParallel(int maxThread, Consumer<? super I> action) {
        if (maxThread < 1)
            throw new IllegalArgumentException();
        try (S s = source.get()) {
            Semaphore sm = new Semaphore(maxThread);
            Phaser ph = new Phaser(1);
            source.iterator(s).forEachRemaining(i -> {
                try {
                    ph.register();
                    sm.acquire();
                    new Thread(() -> {
                        try {
                            action.accept(i);
                        } finally {
                            sm.release();
                            ph.arriveAndDeregister();
                        }
                    }).start();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            ph.arriveAndAwaitAdvance();
        } catch (Exception e) {
            throw new BatchException(e);
        }
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

    private Future<?> start(ExecutorService taskService, Semaphore sm, Runnable runnable) {
        try {
            log.trace("··· task ready to be submitted");
            sm.acquire();
            log.trace("··· task going to be submitted");
            return taskService.submit(() -> {
                try {
                    log.trace("··· task entry.");
                    runnable.run();
                } finally {
                    sm.release();
                    log.trace("··· task exit.");
                }
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
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
                    throw new BatchException((cause));
                }
                it.remove();
            } else {
                k++;
            }
        }
        return k;
    }

    private abstract class DoParallelFair<T, R> {
        private final int maxThread;
        private final Function<? super T, ? extends R> transformer;
        private final Thread main;
        private final BlockingQueue<Future<? extends R>> queue;

        DoParallelFair(int maxThread, Function<? super T, ? extends R> transformer) {
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

        protected void doWork(Iterator<T> iterator, Consumer<R> action) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            try {
                Future<?> future = service1.submit(() -> {
                    ExecutorService service2 = Executors.newFixedThreadPool(maxThread);
                    Semaphore sm = new Semaphore(maxThread);
                    try {
                        while (iterator.hasNext()) {
                            T t = iterator.next();
                            try {
                                sm.acquire();
                                Future<? extends R> promise = service2.submit(() -> {
                                    try {
                                        return transformer.apply(t);
                                    } finally {
                                        sm.release();
                                    }
                                });
                                try {
                                    queue.put(promise);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
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
                    } finally {
                        shutdown(service2);
                        log.info("S.--- waiting for the end of the tasks ...");
                        awaitEmptyQueue();
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
                        try {
                            R oo = fo.get();
                            action.accept(oo);
                        } catch (ExecutionException e) {
                            Throwable cause = e.getCause();
                            String place = placeOf(cause.getStackTrace());
                            log.warn("S.### Error detected in task execution: {} [{}]", cause.getMessage(), place);
                            future.cancel(true);
                            throw new BatchException((cause));
                        }
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
                    throw new BatchException((cause));
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

    private abstract class DoParallelWrite {
        private final int maxThread;
        private final ExecutorService writerService;

        public DoParallelWrite(int maxThread) {
            this.maxThread = maxThread;
            this.writerService = Executors.newCachedThreadPool();
        }

        public void start() {
            try {
                try {
                    openResources();
                    log.info("W.=== completed successfully.");
                } catch (InterruptedException e) {
                    log.info("W.>>> thread interrupted");
                } finally {
                    log.info("W.--- resources closed.");
                }
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

        protected <T extends AutoCloseable, O> PmQueueWriter<O> openQueue(SinkResource<T, O> sink, T t) {
            return PmQueueWriter.of(maxThread, writerService, sink, t);
        }

        //        protected <O> PmQueueWriter<O> openQueue(Consumer<? super O> sink) {
//            return PmQueueWriter.of(maxThread, writerService, sink);
//        }
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
                        Future<?> status = PmLoopSource.this.start(taskService, sm, () -> task.accept(i));
                        if (status == null) break;
                        statuses.add(status);
                        probeStatuses(statuses, true);
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        log.warn("W.>>> Loop interrupted, shutdown taskExecutor ...");
                    } else {
                        log.info("W.--- All task submitted, shutdown taskExecutor ...");
                    }
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

    }

    private abstract class DoParallelRaw<T, R> {
        private final int maxThread;
        private final Function<? super T, ? extends R> transformer;
        private final Thread main;
        private final BlockingQueue<R> queue;

        DoParallelRaw(int maxThread, Function<? super T, ? extends R> transformer) {
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

        protected void doWork(Iterator<T> iterator, Consumer<R> action) {
            ExecutorService service1 = Executors.newFixedThreadPool(1);
            try {
                Future<?> future = service1.submit(() -> {
                    ExecutorService service2 = Executors.newFixedThreadPool(maxThread);
                    Semaphore sm = new Semaphore(maxThread);
                    try {
                        List<Future<?>> statuses = new LinkedList<>();
                        try {
                            while (iterator.hasNext()) {
                                T t = iterator.next();
                                try {
                                    sm.acquire();
                                    Future<?> status = service2.submit(() -> {
                                        try {
                                            queue.put(transformer.apply(t));
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                        } finally {
                                            sm.release();
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
                        } finally {
                            shutdown(service2);
                        }
                        log.info("E.--- pending futures {}", statuses.size());
                        probeStatuses(statuses, false);
                        log.info("E.--- tasks terminated, flush & close ...");
                    } finally {
                        awaitEmptyQueue();
                        log.info("E.--- interrupting the write listener");
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
                    throw new BatchException((cause));
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
}
