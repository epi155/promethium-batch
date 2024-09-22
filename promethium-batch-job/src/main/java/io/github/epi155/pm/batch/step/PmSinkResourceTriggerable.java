package io.github.epi155.pm.batch.step;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

class PmSinkResourceTriggerable<U extends AutoCloseable, O> extends PmSinkResource<U, O> {
    private final Runnable action;

    PmSinkResourceTriggerable(Supplier<U> ctor, BiConsumer<U, O> writer, Runnable action) {
        super(ctor, writer);
        this.action = action;
    }

    @Override
    public void accept(U u, O o) {
        super.accept(u, o);
        action.run();
    }
}
