package io.github.epi155.pm.batch.step;

import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class PmPushSource<S extends AutoCloseable, I> extends PmPushNone<S, I> {

    PmPushSource(SourceResource<S, I> source) {
        super(source);
    }

    @Override
    public <T extends AutoCloseable, O> IterableLoop<I, O> into(SinkResource<T, O> sink) {
        return new PmIterableLoop<>(this, sink);
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2>
    IterableLoop2<I, O1, O2> into(SinkResource<T1, O1> sink1, SinkResource<T2, O2> sink2) {
        return new PmIterableLoop2<>(this, sink1, sink2);
    }

    @Override
    public <T1 extends AutoCloseable, O1,
            T2 extends AutoCloseable, O2,
            T3 extends AutoCloseable, O3>
    IterableLoop3<I, O1, O2, O3> into(
            SinkResource<T1, O1> sink1,
            SinkResource<T2, O2> sink2,
            SinkResource<T3, O3> sink3) {
        return new PmIterableLoop3<>(this, sink1, sink2, sink3);
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
        return new PmIterableLoop4<>(this, sink1, sink2, sink3, sink4);
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
        return new PmIterableLoop5<>(this, sink1, sink2, sink3, sink4, sink5);
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
        return new PmIterableLoop6<>(this, sink1, sink2, sink3, sink4, sink5, sink6);
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
        return new PmIterableLoop7<>(this, sink1, sink2, sink3, sink4, sink5, sink6, sink7);
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
        return new PmIterableLoop8<>(this, sink1, sink2, sink3, sink4, sink5, sink6, sink7, sink8);
    }
}
