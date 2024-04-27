package io.github.epi155.pm.batch;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
class PmLoopSource<S extends AutoCloseable, I> extends PmPushSource<S, I> {

    PmLoopSource(SourceResource<S, I> source) {
        super(source);
    }

    @Override
    public <J> LoopSource<J> map(Function<? super I, ? extends J> map) {
        SourceResource<S, J> mapSource = new SourceResource<>() {
            private S s;
            private Supplier<I> supplier;
            private Iterator<I> iterator;

            @Override
            public S get() {
                this.s = source.get();
                this.iterator = source.iterator(s);
                this.supplier = source.supplier(s);
                return s;
            }

            @Override
            public Iterator<J> iterator(S s) {
                if (!s.equals(this.s)) throw new IllegalStateException();
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public J next() {
                        return map.apply(iterator.next());
                    }
                };
            }

            @Override
            public Supplier<J> supplier(S s) {
                if (!s.equals(this.s)) throw new IllegalStateException();
                return () -> map.apply(supplier.get());
            }
        };
        return new PmLoopSource<>(mapSource);
    }
}
