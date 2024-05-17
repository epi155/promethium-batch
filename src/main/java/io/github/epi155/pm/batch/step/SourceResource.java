package io.github.epi155.pm.batch.step;

import io.github.epi155.pm.batch.job.BatchIOException;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
     * static constructor of {@code SourceResource<BufferedReader, String>}
     *
     * @param file file
     * @param cs   charset
     * @param inc  action after read (usually counter++)
     * @return instance of {@link SourceResource}
     */
    static @NotNull SourceResource<BufferedReader, String> bufferedReader(File file, Charset cs, Runnable inc) {
        return SourceResource.fromSupplier(
                () -> {
                    try {
                        return Files.newBufferedReader(file.toPath(), cs);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                b -> () -> {
                    try {
                        String s = b.readLine();
                        if (s != null) inc.run();
                        return s;
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                }
        );
    }

    /**
     * static constructor of {@code SourceResource<BufferedReader, String>} with default charset
     *
     * @param file file
     * @param inc  action after read (usually counter++)
     * @return instance of {@link SourceResource}
     */
    static @NotNull SourceResource<BufferedReader, String> bufferedReader(File file, Runnable inc) {
        return SourceResource.fromSupplier(
                () -> {
                    try {
                        return Files.newBufferedReader(file.toPath());
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                b -> () -> {
                    try {
                        String s = b.readLine();
                        if (s != null) inc.run();
                        return s;
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                }
        );
    }

    /**
     * static constructor of {@code SourceResource<BufferedReader, String>}
     *
     * @param file file
     * @param cs   charset
     * @return instance of {@link SourceResource}
     */
    static @NotNull SourceResource<BufferedReader, String> bufferedReader(File file, Charset cs) {
        return SourceResource.fromSupplier(
                () -> {
                    try {
                        return Files.newBufferedReader(file.toPath(), cs);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                b -> () -> {
                    try {
                        return b.readLine();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                }
        );
    }

    /**
     * static constructor of {@code SourceResource<BufferedReader, String>} with default charset
     *
     * @param file file
     * @return instance of {@link SourceResource}
     */
    static @NotNull SourceResource<BufferedReader, String> bufferedReader(File file) {
        return SourceResource.fromSupplier(
                () -> {
                    try {
                        return Files.newBufferedReader(file.toPath());
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                b -> () -> {
                    try {
                        return b.readLine();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                }
        );
    }

    /**
     * static constructor of {@code SourceResource<BufferedReader, I>} with decoder String to I
     *
     * @param file file
     * @param dec  decoder function
     * @param cs   charset
     * @param inc  action after read (usually counter++)
     * @param <I>  type read
     * @return instance of {@link SourceResource}
     */
    static <I> @NotNull SourceResource<BufferedReader, I> bufferedReader(File file, Function<String, I> dec, Charset cs, Runnable inc) {
        return SourceResource.fromSupplier(
                () -> {
                    try {
                        return Files.newBufferedReader(file.toPath(), cs);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                b -> () -> {
                    try {
                        String s = b.readLine();
                        if (s != null) inc.run();
                        return dec.apply(s);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                }
        );
    }

    /**
     * static constructor of {@code SourceResource<BufferedReader, I>} with decoder String to I
     *
     * @param file file
     * @param dec  decoder function
     * @param cs   charset
     * @param <I>  type read
     * @return instance of {@link SourceResource}
     */
    static <I> @NotNull SourceResource<BufferedReader, I> bufferedReader(File file, Function<String, I> dec, Charset cs) {
        return SourceResource.fromSupplier(
                () -> {
                    try {
                        return Files.newBufferedReader(file.toPath(), cs);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                b -> () -> {
                    try {
                        String s = b.readLine();
                        return dec.apply(s);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                }
        );
    }

    /**
     * static constructor of {@code SourceResource<BufferedReader, I>}
     * with decoder String to I (default charset)
     *
     * @param file file
     * @param dec  decoder function
     * @param inc  action after read (usually counter++)
     * @param <I>  type read
     * @return instance of {@link SourceResource}
     */
    static <I> @NotNull SourceResource<BufferedReader, I> bufferedReader(File file, Function<String, I> dec, Runnable inc) {
        return SourceResource.fromSupplier(
                () -> {
                    try {
                        return Files.newBufferedReader(file.toPath());
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                b -> () -> {
                    try {
                        String s = b.readLine();
                        if (s != null) inc.run();
                        return dec.apply(s);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                }
        );
    }

    /**
     * static constructor of {@code SourceResource<BufferedReader, I>}
     * with decoder String to I (default charset)
     *
     * @param file file
     * @param dec  decoder function
     * @param <I>  type read
     * @return instance of {@link SourceResource}
     */
    static <I> @NotNull SourceResource<BufferedReader, I> bufferedReader(File file, Function<String, I> dec) {
        return SourceResource.fromSupplier(
                () -> {
                    try {
                        return Files.newBufferedReader(file.toPath());
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                b -> () -> {
                    try {
                        String s = b.readLine();
                        return dec.apply(s);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                }
        );
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
