package io.github.epi155.pm.batch.step;

import java.util.Iterator;

class PmCloseableIterable<I> implements AutoCloseable, Iterable<I> {
    private final Iterable<I> iterable;

    PmCloseableIterable(Iterable<I> iterable) {
        this.iterable = iterable;
    }

    @Override
    public void close() {
        // noop
    }

    @Override
    public Iterator<I> iterator() {
        return iterable.iterator();
    }
}
