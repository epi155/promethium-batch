package io.github.epi155.pm.batch.step;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

class PmCloseableIterable<I> implements AutoCloseable, Iterable<I> {
    private final Iterable<I> iterable;

    PmCloseableIterable(Iterable<I> iterable) {
        this.iterable = iterable;
    }

    @Override
    public void close() {

    }

    @NotNull
    @Override
    public Iterator<I> iterator() {
        return iterable.iterator();
    }
}
