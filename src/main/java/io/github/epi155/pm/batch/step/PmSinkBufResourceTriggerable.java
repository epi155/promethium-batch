package io.github.epi155.pm.batch.step;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

class PmSinkBufResourceTriggerable<U extends AutoCloseable, O>
        extends PmSinkBufResource<U, O> {
    private final Runnable action;

    public PmSinkBufResourceTriggerable(Supplier<U> ctor, BiConsumer<U, O> writer, Consumer<U> flusher, Runnable action) {
        super(ctor, writer, flusher);
        this.action = action;
    }

    @Override
    public void accept(U u, O o) {
        super.accept(u, o);
        action.run();
    }

}
