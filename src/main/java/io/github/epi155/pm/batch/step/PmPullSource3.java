package io.github.epi155.pm.batch.step;

import io.github.epi155.pm.batch.job.BatchException;

import java.util.function.Consumer;

class PmPullSource3<S1 extends AutoCloseable, I1,
        S2 extends AutoCloseable, I2,
        S3 extends AutoCloseable, I3>
        implements PullSource3<I1, I2, I3> {
    private final SourceResource<S1, I1> source1;
    private final SourceResource<S2, I2> source2;
    private final SourceResource<S3, I3> source3;

    public PmPullSource3(SourceResource<S1, I1> source1, SourceResource<S2, I2> source2, SourceResource<S3, I3> source3) {
        this.source1 = source1;
        this.source2 = source2;
        this.source3 = source3;
    }

    private <O, T extends AutoCloseable> Consumer<? super O> consumerOf(SinkResource<T, O> sink, T t) {
        return o -> sink.accept(t, o);
    }

    @Override
    public <T extends AutoCloseable, O> PullProcess3o1<I1, I2, I3, O> into(SinkResource<T, O> sink) {
        return worker -> {
            try (T t = sink.get();
                 S1 s1 = source1.get();
                 S2 s2 = source2.get();
                 S3 s3 = source3.get()
            ) {
                worker.proceed(
                        source1.supplier(s1),
                        source2.supplier(s2),
                        source3.supplier(s3),
                        consumerOf(sink, t));
            } catch (Exception e) {
                throw new BatchException(e);
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1, T2 extends AutoCloseable, O2> PullProcess3o2<I1, I2, I3, O1, O2> into(SinkResource<T1, O1> sink1, SinkResource<T2, O2> sink2) {
        return worker -> {
            try (T1 t1 = sink1.get();
                 T2 t2 = sink2.get();
                 S1 s1 = source1.get();
                 S2 s2 = source2.get();
                 S3 s3 = source3.get()
            ) {
                worker.proceed(
                        source1.supplier(s1),
                        source2.supplier(s2),
                        source3.supplier(s3),
                        consumerOf(sink1, t1),
                        consumerOf(sink2, t2));
            } catch (Exception e) {
                throw new BatchException(e);
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3>
    PullProcess3o3<I1, I2, I3, O1, O2, O3> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3) {
        return worker -> {
            try (T1 t1 = sink1.get();
                 T2 t2 = sink2.get();
                 T3 t3 = sink3.get();
                 S1 s1 = source1.get();
                 S2 s2 = source2.get();
                 S3 s3 = source3.get()
            ) {
                worker.proceed(
                        source1.supplier(s1),
                        source2.supplier(s2),
                        source3.supplier(s3),
                        consumerOf(sink1, t1),
                        consumerOf(sink2, t2),
                        consumerOf(sink3, t3));
            } catch (Exception e) {
                throw new BatchException(e);
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4>
    PullProcess3o4<I1, I2, I3, O1, O2, O3, O4> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4) {
        return worker -> {
            try (T1 t1 = sink1.get();
                 T2 t2 = sink2.get();
                 T3 t3 = sink3.get();
                 T4 t4 = sink4.get();
                 S1 s1 = source1.get();
                 S2 s2 = source2.get();
                 S3 s3 = source3.get()
            ) {
                worker.proceed(
                        source1.supplier(s1),
                        source2.supplier(s2),
                        source3.supplier(s3),
                        consumerOf(sink1, t1),
                        consumerOf(sink2, t2),
                        consumerOf(sink3, t3),
                        consumerOf(sink4, t4));
            } catch (Exception e) {
                throw new BatchException(e);
            }
        };
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5>
    PullProcess3o5<I1, I2, I3, O1, O2, O3, O4, O5> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5) {
        return worker -> {
            try (T1 t1 = sink1.get();
                 T2 t2 = sink2.get();
                 T3 t3 = sink3.get();
                 T4 t4 = sink4.get();
                 T5 t5 = sink5.get();
                 S1 s1 = source1.get();
                 S2 s2 = source2.get();
                 S3 s3 = source3.get()
            ) {
                worker.proceed(
                        source1.supplier(s1),
                        source2.supplier(s2),
                        source3.supplier(s3),
                        consumerOf(sink1, t1),
                        consumerOf(sink2, t2),
                        consumerOf(sink3, t3),
                        consumerOf(sink4, t4),
                        consumerOf(sink5, t5));
            } catch (Exception e) {
                throw new BatchException(e);
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
    PullProcess3o6<I1, I2, I3, O1, O2, O3, O4, O5, O6> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6) {
        return worker -> {
            try (T1 t1 = sink1.get();
                 T2 t2 = sink2.get();
                 T3 t3 = sink3.get();
                 T4 t4 = sink4.get();
                 T5 t5 = sink5.get();
                 T6 t6 = sink6.get();
                 S1 s1 = source1.get();
                 S2 s2 = source2.get();
                 S3 s3 = source3.get()
            ) {
                worker.proceed(
                        source1.supplier(s1),
                        source2.supplier(s2),
                        source3.supplier(s3),
                        consumerOf(sink1, t1),
                        consumerOf(sink2, t2),
                        consumerOf(sink3, t3),
                        consumerOf(sink4, t4),
                        consumerOf(sink5, t5),
                        consumerOf(sink6, t6));
            } catch (Exception e) {
                throw new BatchException(e);
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
    PullProcess3o7<I1, I2, I3, O1, O2, O3, O4, O5, O6, O7> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7) {
        return worker -> {
            try (T1 t1 = sink1.get();
                 T2 t2 = sink2.get();
                 T3 t3 = sink3.get();
                 T4 t4 = sink4.get();
                 T5 t5 = sink5.get();
                 T6 t6 = sink6.get();
                 T7 t7 = sink7.get();
                 S1 s1 = source1.get();
                 S2 s2 = source2.get();
                 S3 s3 = source3.get()
            ) {
                worker.proceed(
                        source1.supplier(s1),
                        source2.supplier(s2),
                        source3.supplier(s3),
                        consumerOf(sink1, t1),
                        consumerOf(sink2, t2),
                        consumerOf(sink3, t3),
                        consumerOf(sink4, t4),
                        consumerOf(sink5, t5),
                        consumerOf(sink6, t6),
                        consumerOf(sink7, t7));
            } catch (Exception e) {
                throw new BatchException(e);
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
    PullProcess3o8<I1, I2, I3, O1, O2, O3, O4, O5, O6, O7, O8> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7,
            SinkResource<T8, O8> sink8) {
        return worker -> {
            try (T1 t1 = sink1.get();
                 T2 t2 = sink2.get();
                 T3 t3 = sink3.get();
                 T4 t4 = sink4.get();
                 T5 t5 = sink5.get();
                 T6 t6 = sink6.get();
                 T7 t7 = sink7.get();
                 T8 t8 = sink8.get();
                 S1 s1 = source1.get();
                 S2 s2 = source2.get();
                 S3 s3 = source3.get()
            ) {
                worker.proceed(
                        source1.supplier(s1),
                        source2.supplier(s2),
                        source3.supplier(s3),
                        consumerOf(sink1, t1),
                        consumerOf(sink2, t2),
                        consumerOf(sink3, t3),
                        consumerOf(sink4, t4),
                        consumerOf(sink5, t5),
                        consumerOf(sink6, t6),
                        consumerOf(sink7, t7),
                        consumerOf(sink8, t8));
            } catch (Exception e) {
                throw new BatchException(e);
            }
        };
    }

    @Override
    public void proceed(PullWorker3o0<I1, I2, I3> worker) {
        try (S1 s1 = source1.get();
             S2 s2 = source2.get();
             S3 s3 = source3.get()
        ) {
            worker.proceed(
                    source1.supplier(s1),
                    source2.supplier(s2),
                    source3.supplier(s3));
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }
}
