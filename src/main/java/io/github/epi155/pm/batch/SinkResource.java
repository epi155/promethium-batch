package io.github.epi155.pm.batch;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * interface to manage a sink (writer) type resource
 *
 * @param <U> resource type
 * @param <O> type of element consumed by the resource
 */
public interface SinkResource<U extends AutoCloseable, O> {
    /**
     * static constructor
     *
     * <pre>
     * val sink = SinkResource.of(
     *      () -> Files.newBufferedWriter(Path.of("foo.txt")),
     *      (wr, s) -> wr::write);
     * </pre>{@code try/catch} omitted
     *
     * @param ctor   resource constructor provider
     * @param writer method of the resource to send data to
     * @param <U>    resource type
     * @param <O>    element type consumed by the resource
     * @return instance of {@link SinkResource}
     */
    static <U extends AutoCloseable, O> SinkResource<U, O> of(Supplier<U> ctor, BiConsumer<U, O> writer) {
        return new PmSinkResource<>(ctor, writer);
    }

    /**
     * static constructor simple resource (does not require closing the resource)
     * <pre>
     * val sink = SinkResource.of(System.out::println);
     * </pre>
     *
     * @param writer data consumer
     * @param <O>    element type consumed
     * @return instance of {@link SinkResource}
     */
    static <O> SinkResource<?, O> of(Consumer<O> writer) {
        Supplier<PmCloseableConsumer<O>> ctor = () -> new PmCloseableConsumer<>(writer);
        return new PmSinkResource<>(ctor, PmCloseableConsumer::writer);
    }

    /**
     * provides the native resource
     *
     * @return native resource
     */
    U get();

    /**
     * sends (writes) the data to the resource
     *
     * @param u native resource
     * @param o data to send (write) to the resource
     */
    void accept(U u, O o);

    /**
     * data consumer
     *
     * @param u native resource
     * @return data consumer (method to send data to)
     */
    Consumer<? super O> consumer(U u);
}
