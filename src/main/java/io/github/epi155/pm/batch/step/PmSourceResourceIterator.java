package io.github.epi155.pm.batch.step;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

class PmSourceResourceIterator<U extends AutoCloseable, I> implements SourceResource<U, I> {
    private final Supplier<U> ctor;
    private final Function<? super U, Iterator<I>> reader;

    PmSourceResourceIterator(Supplier<U> ctor, Function<? super U, @NotNull Iterator<I>> reader) {
        this.ctor = ctor;
        this.reader = reader;
    }

    @Override
    public U get() {
        return ctor.get();
    }

    @Override
    public Iterator<I> iterator(U u) {
        return reader.apply(u);
    }

    @Override
    public Supplier<I> supplier(U u) {
        Iterator<I> it = reader.apply(u);
        return () -> it.hasNext() ? it.next() : null;
    }
}
