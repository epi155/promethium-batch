package io.github.epi155.pm.batch.step;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

class PmSourceResourceStream<U extends Stream<I>, I> implements SourceResource<U, I> {
    private final Stream<I> stream;
    private final Iterator<I> iterator;

    public PmSourceResourceStream(Stream<I> stream) {
        this.stream = stream;
        this.iterator = stream.iterator();
    }

    @SuppressWarnings("unchecked")
	@Override
    public U get() {
        return (U) stream;
    }

    @Override
    public Iterator<I> iterator(U u) {
        if (!u.equals(stream)) throw new IllegalStateException();
        return iterator;
    }

    @Override
    public Supplier<I> supplier(U u) {
        if (!u.equals(stream)) throw new IllegalStateException();
        return () -> iterator.hasNext() ? iterator.next() : null;
    }
}
