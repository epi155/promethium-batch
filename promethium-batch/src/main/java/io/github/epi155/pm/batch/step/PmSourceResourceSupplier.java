package io.github.epi155.pm.batch.step;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

class PmSourceResourceSupplier<U extends AutoCloseable, I> implements SourceResource<U, I> {
    private final Supplier<U> ctor;
    private final Function<U, Supplier<I>> reader;

    public PmSourceResourceSupplier(Supplier<U> ctor, Function<U, Supplier<I>> reader) {
        this.ctor = ctor;
        this.reader = reader;
    }

    @Override
    public U get() {
        return ctor.get();
    }

    @Override
    public Iterator<I> iterator(U u) {
        return new Iterator<>() {
            private I readyItem;

            @Override
            public boolean hasNext() {
                if (readyItem != null)
                    return true;
                readyItem = reader.apply(u).get();
                return readyItem != null;
            }

            @Override
            public I next() {
                if (readyItem != null) {
                    I item = readyItem;
                    readyItem = null;
                    return item;
                }
                throw new NoSuchElementException();
            }
        };
    }

    @Override
    public Supplier<I> supplier(U u) {
        return reader.apply(u);
    }

}
