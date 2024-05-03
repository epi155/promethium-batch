package io.github.epi155.pm.batch.step;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * interface to manage a source type resource (reader).
 *
 * @param <U> resource type
 * @param <I> type of item produced by the resource
 */
public interface SourceResource<U extends AutoCloseable, I> {
    /**
     * static constructor
     *
     * @param ctor   resource constructor provider
     * @param reader iterator method of the resource from which to receive data
     * @param <U>    resource type
     * @param <I>    type of item produced by the resource
     * @return instance of {@link SourceResource}
     */
    static <U extends AutoCloseable, I> @NotNull SourceResource<U, I> fromIterator(@NotNull Supplier<U> ctor, Function<U, @NotNull Iterator<I>> reader) {
        return new PmSourceResourceIterator<>(ctor, reader);
    }

    /**
     * static constructor (Iterable resource)
     *
     * @param ctor resource constructor provider
     * @param <U>  resource type
     * @param <I>  type of item produced by the resource
     * @return instance of {@link SourceResource}
     */
    static <U extends AutoCloseable & Iterable<I>, I> @NotNull SourceResource<U, I> fromIterator(@NotNull Supplier<U> ctor) {
        return new PmSourceResourceIterator<>(ctor, Iterable::iterator);
    }

    /**
     * static constructor (Stream resource)
     *
     * @param stream the stream of data
     * @param <U>    resource type
     * @param <I>    type of item produced by the resource
     * @return instance of {@link SourceResource}
     */
    static <U extends Stream<I>, I> @NotNull SourceResource<U, I> fromStream(@NotNull Stream<I> stream) {
        return new PmSourceResourceStream<>(stream);
    }

    /**
     * static constructor simple resource (does not require closing the resource)
     *
     * @param iterable iterable method from which to receive data
     * @param <I>      type of item produced by the resource
     * @return instance of {@link SourceResource}
     */
    static <I> @NotNull SourceResource<?, I> fromIterator(@NotNull Iterable<I> iterable) {
        Supplier<PmCloseableIterable<I>> ctor = () -> new PmCloseableIterable<>(iterable);
        return new PmSourceResourceIterator<>(ctor, PmCloseableIterable::iterator);
    }

    /**
     * static constructor
     *
     * @param ctor   resource constructor provider
     * @param reader supplier method of the resource from which to receive data
     * @param <U>    resource type
     * @param <I>    type of item produced by the resource
     * @return instance of {@link SourceResource}
     */
    static <U extends AutoCloseable, I> @NotNull SourceResource<U, I> fromSupplier(@NotNull Supplier<U> ctor, Function<U, @NotNull Supplier<I>> reader) {
        return new PmSourceResourceSupplier<>(ctor, reader);
    }

    /**
     * static constructor (Supplier resource)
     *
     * @param ctor resource constructor provider
     * @param <U>  resource type
     * @param <I>  type of item produced by the resource
     * @return instance of {@link SourceResource}
     */
    static <U extends AutoCloseable & Supplier<I>, I> @NotNull SourceResource<U, I> fromSupplier(@NotNull Supplier<U> ctor) {
        return new PmSourceResourceSupplier<>(ctor, u -> u);
    }

    /**
     * provides the native resource
     *
     * @return native resource
     */
    U get();

    /**
     * iterator of the produced data
     *
     * @param u native resource
     * @return iterator of the produced data
     */
    Iterator<I> iterator(U u);

    /**
     * supplier of the produced data
     *
     * @param u native resource
     * @return supplier of the produced data
     */
    Supplier<I> supplier(U u);
}
