package io.github.epi155.pm.batch;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @param <S1>
 * @param <I1>
 * @param <S2>
 * @param <I2>
 */
class PmPullSource2<
        S1 extends AutoCloseable, I1,
        S2 extends AutoCloseable, I2>
        implements PullSource2<I1, I2> {
    private final SourceResource<S1, I1> source1;
    private final SourceResource<S2, I2> source2;

    public PmPullSource2(@NotNull SourceResource<S1, I1> source1, @NotNull SourceResource<S2, I2> source2) {
        this.source1 = source1;
        this.source2 = source2;
    }

    @Override
    public void proceed(PullWorker2o0<I1, I2> worker) {
        try (S1 s1 = source1.get();
             S2 s2 = source2.get()
        ) {
            worker.proceed(source1.supplier(s1), source2.supplier(s2));
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public <T extends AutoCloseable, O> PullProcess2o1<I1, I2, O> into(SinkResource<T, O> sink) {
        try (T t = sink.get();
             S1 s1 = source1.get();
             S2 s2 = source2.get()
        ) {
            Supplier<I1> si1 = source1.supplier(s1);
            Supplier<I2> si2 = source2.supplier(s2);
            Consumer<O> ct = (d) -> sink.accept(t, d);
            return worker -> worker.proceed(si1, si2, ct);
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2>
    PullProcess2o2<I1, I2, O1, O2> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             S1 s1 = source1.get();
             S2 s2 = source2.get()
        ) {
            Supplier<I1> si1 = source1.supplier(s1);
            Supplier<I2> si2 = source2.supplier(s2);
            Consumer<O1> ct1 = (d) -> sink1.accept(t1, d);
            Consumer<O2> ct2 = (d) -> sink2.accept(t2, d);
            return worker -> worker.proceed(si1, si2, ct1, ct2);
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3>
    PullProcess2o3<I1, I2, O1, O2, O3> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             S1 s1 = source1.get();
             S2 s2 = source2.get()
        ) {
            return worker -> worker.proceed(
                    source1.supplier(s1),
                    source2.supplier(s2),
                    sink1.consumer(t1),
                    sink2.consumer(t2),
                    sink3.consumer(t3)
            );
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4>
    PullProcess2o4<I1, I2, O1, O2, O3, O4> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             T4 t4 = sink4.get();
             S1 s1 = source1.get();
             S2 s2 = source2.get()
        ) {
            return worker -> worker.proceed(
                    source1.supplier(s1),
                    source2.supplier(s2),
                    sink1.consumer(t1),
                    sink2.consumer(t2),
                    sink3.consumer(t3),
                    sink4.consumer(t4)
            );
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5>
    PullProcess2o5<I1, I2, O1, O2, O3, O4, O5> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             T4 t4 = sink4.get();
             T5 t5 = sink5.get();
             S1 s1 = source1.get();
             S2 s2 = source2.get()
        ) {
            return worker -> worker.proceed(
                    source1.supplier(s1),
                    source2.supplier(s2),
                    sink1.consumer(t1),
                    sink2.consumer(t2),
                    sink3.consumer(t3),
                    sink4.consumer(t4),
                    sink5.consumer(t5)
            );
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6>
    PullProcess2o6<I1, I2, O1, O2, O3, O4, O5, O6> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             T4 t4 = sink4.get();
             T5 t5 = sink5.get();
             T6 t6 = sink6.get();
             S1 s1 = source1.get();
             S2 s2 = source2.get()
        ) {
            return worker -> worker.proceed(
                    source1.supplier(s1),
                    source2.supplier(s2),
                    sink1.consumer(t1),
                    sink2.consumer(t2),
                    sink3.consumer(t3),
                    sink4.consumer(t4),
                    sink5.consumer(t5),
                    sink6.consumer(t6)
            );
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3,
            T4 extends AutoCloseable, O4,
            T5 extends AutoCloseable, O5,
            T6 extends AutoCloseable, O6,
            T7 extends AutoCloseable, O7>
    PullProcess2o7<I1, I2, O1, O2, O3, O4, O5, O6, O7> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             T4 t4 = sink4.get();
             T5 t5 = sink5.get();
             T6 t6 = sink6.get();
             T7 t7 = sink7.get();
             S1 s1 = source1.get();
             S2 s2 = source2.get()
        ) {
            return worker -> worker.proceed(
                    source1.supplier(s1),
                    source2.supplier(s2),
                    sink1.consumer(t1),
                    sink2.consumer(t2),
                    sink3.consumer(t3),
                    sink4.consumer(t4),
                    sink5.consumer(t5),
                    sink6.consumer(t6),
                    sink7.consumer(t7)
            );
        } catch (Exception e) {
            throw new BatchException(e);
        }
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
    PullProcess2o8<I1, I2, O1, O2, O3, O4, O5, O6, O7, O8> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3,
            SinkResource<T4, O4> sink4,
            SinkResource<T5, O5> sink5,
            SinkResource<T6, O6> sink6,
            SinkResource<T7, O7> sink7,
            SinkResource<T8, O8> sink8) {
        try (T1 t1 = sink1.get();
             T2 t2 = sink2.get();
             T3 t3 = sink3.get();
             T4 t4 = sink4.get();
             T5 t5 = sink5.get();
             T6 t6 = sink6.get();
             T7 t7 = sink7.get();
             T8 t8 = sink8.get();
             S1 s1 = source1.get();
             S2 s2 = source2.get()
        ) {
            return worker -> worker.proceed(
                    source1.supplier(s1),
                    source2.supplier(s2),
                    sink1.consumer(t1),
                    sink2.consumer(t2),
                    sink3.consumer(t3),
                    sink4.consumer(t4),
                    sink5.consumer(t5),
                    sink6.consumer(t6),
                    sink7.consumer(t7),
                    sink8.consumer(t8)
            );
        } catch (Exception e) {
            throw new BatchException(e);
        }
    }
}
