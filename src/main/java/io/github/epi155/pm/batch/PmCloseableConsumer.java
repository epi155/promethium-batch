package io.github.epi155.pm.batch;

import java.util.function.Consumer;

class PmCloseableConsumer<O> implements AutoCloseable {
    private final Consumer<O> action;

    PmCloseableConsumer(Consumer<O> action) {
        this.action = action;
    }

    @Override
    public void close() {

    }

    public void writer(O o) {
        action.accept(o);
    }
}
