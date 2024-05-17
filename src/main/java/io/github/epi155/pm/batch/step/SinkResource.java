package io.github.epi155.pm.batch.step;

import io.github.epi155.pm.batch.job.BatchIOException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
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
     * static constructor
     *
     * @param ctor   resource constructor provider
     * @param writer method of the resource to send data to
     * @param action action invoked after writing the data
     * @param <U>    resource type
     * @param <O>    element type consumed by the resource
     * @return instance of {@link SinkResource}
     */
    static <U extends AutoCloseable, O> SinkResource<U, O> of(
            Supplier<U> ctor,
            BiConsumer<U, O> writer,
            Runnable action) {
        return new PmSinkResourceTriggerable<>(ctor, writer, action);
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
     * static constructor simple resource (does not require closing the resource)
     * <pre>val snk = SinkResource.of(System.out::println, c::inc);</pre>
     *
     * @param writer data consumer
     * @param action action invoked after writing the data
     * @param <O>    element type consumed
     * @return instance of {@link SinkResource}
     */
    static <O> SinkResource<?, O> of(Consumer<O> writer, Runnable action) {
        Supplier<PmCloseableConsumer<O>> ctor = () -> new PmCloseableConsumer<>(writer);
        return new PmSinkResourceTriggerable<>(ctor, PmCloseableConsumer::writer, action);
    }

    /**
     * static constructor of {@code <BufferedWriter, String>}
     *
     * @param file file
     * @param cs   charset
     * @param inc  action after write (usually counter++)
     * @return instance of {@link SinkResource}
     */
    static SinkResource<BufferedWriter, String> bufferedWriter(File file, Charset cs, Runnable inc) {
        return SinkResource.of(
                () -> {
                    try {
                        return Files.newBufferedWriter(file.toPath(), cs);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                (w, o) -> {
                    try {
                        w.write(o);
                        w.newLine();
                        inc.run();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
    }

    /**
     * static constructor of {@code <BufferedWriter, String>} with default charset
     *
     * @param file file
     * @param inc  action after write (usually counter++)
     * @return instance of {@link SinkResource}
     */
    static SinkResource<BufferedWriter, String> bufferedWriter(File file, Runnable inc) {
        return SinkResource.of(
                () -> {
                    try {
                        return Files.newBufferedWriter(file.toPath());
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                (w, o) -> {
                    try {
                        w.write(o);
                        w.newLine();
                        inc.run();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
    }

    /**
     * static constructor of {@code <BufferedWriter, String>}
     *
     * @param file file
     * @param cs   charset
     * @return instance of {@link SinkResource}
     */
    static SinkResource<BufferedWriter, String> bufferedWriter(File file, Charset cs) {
        return SinkResource.of(
                () -> {
                    try {
                        return Files.newBufferedWriter(file.toPath(), cs);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                (w, o) -> {
                    try {
                        w.write(o);
                        w.newLine();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
    }

    /**
     * static constructor of {@code <BufferedWriter, String>} with default charset
     *
     * @param file file
     * @return instance of {@link SinkResource}
     */
    static SinkResource<BufferedWriter, String> bufferedWriter(File file) {
        return SinkResource.of(
                () -> {
                    try {
                        return Files.newBufferedWriter(file.toPath());
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                (w, o) -> {
                    try {
                        w.write(o);
                        w.newLine();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
    }

    /**
     * static constructor of {@code <BufferedWriter, O>} with encoder O to String
     *
     * @param file file
     * @param enc  encoder function
     * @param cs   charset
     * @param inc  action after write (usually counter++)
     * @param <O>  type write
     * @return instance of {@link SinkResource}
     */
    static <O> SinkResource<BufferedWriter, O> bufferedWriter(File file, Function<O, String> enc, Charset cs, Runnable inc) {
        return SinkResource.of(
                () -> {
                    try {
                        return Files.newBufferedWriter(file.toPath(), cs);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                (w, o) -> {
                    try {
                        w.write(enc.apply(o));
                        w.newLine();
                        inc.run();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
    }

    /**
     * static constructor of {@code <BufferedWriter, O>} with encoder O to String
     *
     * @param file file
     * @param enc  encoder function
     * @param cs   charset
     * @param <O>  type write
     * @return instance of {@link SinkResource}
     */
    static <O> SinkResource<BufferedWriter, O> bufferedWriter(File file, Function<O, String> enc, Charset cs) {
        return SinkResource.of(
                () -> {
                    try {
                        return Files.newBufferedWriter(file.toPath(), cs);
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                (w, o) -> {
                    try {
                        w.write(enc.apply(o));
                        w.newLine();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
    }

    /**
     * static constructor of {@code <BufferedWriter, O>}
     * with encoder O to String (default charset)
     *
     * @param file file
     * @param enc  encoder function
     * @param inc  action after write (usually counter++)
     * @param <O>  type write
     * @return instance of {@link SinkResource}
     */
    static <O> SinkResource<BufferedWriter, O> bufferedWriter(File file, Function<O, String> enc, Runnable inc) {
        return SinkResource.of(
                () -> {
                    try {
                        return Files.newBufferedWriter(file.toPath());
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                (w, o) -> {
                    try {
                        w.write(enc.apply(o));
                        w.newLine();
                        inc.run();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
    }

    /**
     * static constructor of {@code <BufferedWriter, O>}
     * with encoder O to String (default charset)
     *
     * @param file file
     * @param enc  encoder function
     * @param <O>  type write
     * @return instance of {@link SinkResource}
     */
    static <O> SinkResource<BufferedWriter, O> bufferedWriter(File file, Function<O, String> enc) {
        return SinkResource.of(
                () -> {
                    try {
                        return Files.newBufferedWriter(file.toPath());
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                },
                (w, o) -> {
                    try {
                        w.write(enc.apply(o));
                        w.newLine();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
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

//    /**
//     * data consumer
//     *
//     * @param u native resource
//     * @return data consumer (method to send data to)
//     */
//    Consumer<? super O> consumer(U u);
}
