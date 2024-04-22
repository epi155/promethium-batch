package io.github.epi155.pm.batch;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

class PmSinkResource<U extends AutoCloseable, O> implements SinkResource<U, O> {
    private final Supplier<U> ctor;
    private final BiConsumer<U, O> consumer;

    public PmSinkResource(Supplier<U> ctor, BiConsumer<U, O> consumer) {
        this.ctor = ctor;
        this.consumer = consumer;
    }

    @Override
    public U get() {
        return ctor.get();
    }

    @Override
    public void accept(U u, O o) {
        consumer.accept(u, o);
    }

    @Override
    public Consumer<? super O> consumer(U u) {
        return o -> accept(u, o);
    }
}
