package io.github.epi155.pm.batch.step;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

class PmSinkBufResource<U extends AutoCloseable, O>
        extends PmSinkResource<U, O> implements SinkBufResource<U, O> {
    private final Consumer<U> flusher;
    private U u;

    public PmSinkBufResource(Supplier<U> ctor, BiConsumer<U, O> writer, Consumer<U> flusher) {
        super(ctor, writer);
        this.flusher = flusher;
    }

    @Override
    public U get() {
        this.u = super.get();
        return u;
    }

    @Override
    public void flush() {
        if (u != null) {
            flusher.accept(u);
        }
    }
}
