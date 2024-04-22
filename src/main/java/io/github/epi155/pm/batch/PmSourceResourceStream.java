package io.github.epi155.pm.batch;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

class PmSourceResourceStream<U extends Stream<I>, I> implements SourceResource<U, I> {
    private final Stream<I> stream;

    public PmSourceResourceStream(@NotNull Stream<I> stream) {
        this.stream = stream;
    }

    @Override
    public U get() {
        //noinspection unchecked
        return (U) stream;
    }

    @Override
    public Iterator<I> iterator(U u) {
        return u.iterator();
    }

    @Override
    public Supplier<I> supplier(U u) {
        Iterator<I> it = u.iterator();
        return () -> it.hasNext() ? it.next() : null;
    }
}
