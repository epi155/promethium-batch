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
 * interface to manage a buffered sink (writer) type resource
 *
 * @param <U> resource type
 * @param <O> type of element consumed by the resource
 */
public interface SinkBufResource<U extends AutoCloseable, O> extends SinkResource<U, O> {
    /**
     * static constructor
     *
     * <pre>
     * val sink = SinkBufResource.of(
     *      () -> Files.newBufferedWriter(Path.of("foo.txt")),
     *      (wr, s) -> wr::write);
     *      wr -> wr::flush);
     * </pre>{@code try/catch} omitted
     *
     * @param ctor   resource constructor provider
     * @param writer method of the resource to send data to
     * @param flusher method of the resource to flush buffered data
     * @return instance of {@link SinkBufResource}
     * @param <U>    resource type
     * @param <O>    element type consumed by the resource
     */
    static <U extends AutoCloseable, O>
    SinkBufResource<U, O> of(Supplier<U> ctor, BiConsumer<U, O> writer, Consumer<U> flusher) {
        return new PmSinkBufResource<>(ctor, writer, flusher);
    }

    /**
     * static constructor
     *
     * <pre>
     * val sink = SinkBufResource.of(
     *      () -> Files.newBufferedWriter(Path.of("foo.txt")),
     *      (wr, s) -> wr::write);
     *      wr -> wr::flush);
     * </pre>{@code try/catch} omitted
     *
     * @param ctor   resource constructor provider
     * @param writer method of the resource to send data to
     * @param flusher method of the resource to flush buffered data
     * @param action action invoked after writing the data
     * @return instance of {@link SinkBufResource}
     * @param <U>    resource type
     * @param <O>    element type consumed by the resource
     */
    static <U extends AutoCloseable, O>
    SinkBufResource<U, O> of(Supplier<U> ctor, BiConsumer<U, O> writer, Consumer<U> flusher, Runnable action) {
        return new PmSinkBufResourceTriggerable<>(ctor, writer, flusher, action);
    }

    /**
     * static constructor of {@code <BufferedWriter, String>}
     *
     * @param file file
     * @param cs   charset
     * @param inc  action after write (usually counter++)
     * @return instance of {@link SinkBufResource}
     */
    static SinkBufResource<BufferedWriter, String> bufferedWriter(File file, Charset cs, Runnable inc) {
        return SinkBufResource.of(
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
                },
                w -> {
                    try {
                        w.flush();
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
     * @return instance of {@link SinkBufResource}
     */
    static SinkBufResource<BufferedWriter, String> bufferedWriter(File file, Runnable inc) {
        return SinkBufResource.of(
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
                },
                w -> {
                    try {
                        w.flush();
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
     * @return instance of {@link SinkBufResource}
     */
    static SinkBufResource<BufferedWriter, String> bufferedWriter(File file, Charset cs) {
        return SinkBufResource.of(
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
                },
                w -> {
                    try {
                        w.flush();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
    }

    /**
     * static constructor of {@code <BufferedWriter, String>} with default charset
     *
     * @param file file
     * @return instance of {@link SinkBufResource}
     */
    static SinkBufResource<BufferedWriter, String> bufferedWriter(File file) {
        return SinkBufResource.of(
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
                },
                w -> {
                    try {
                        w.flush();
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
     * @return instance of {@link SinkBufResource}
     */
    static <O> SinkBufResource<BufferedWriter, O> bufferedWriter(File file, Function<O, String> enc, Charset cs, Runnable inc) {
        return SinkBufResource.of(
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
                },
                w -> {
                    try {
                        w.flush();
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
     * @return instance of {@link SinkBufResource}
     */
    static <O> SinkBufResource<BufferedWriter, O> bufferedWriter(File file, Function<O, String> enc, Charset cs) {
        return SinkBufResource.of(
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
                },
                w -> {
                    try {
                        w.flush();
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
     * @return instance of {@link SinkBufResource}
     */
    static <O> SinkBufResource<BufferedWriter, O> bufferedWriter(File file, Function<O, String> enc, Runnable inc) {
        return SinkBufResource.of(
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
                },
                w -> {
                    try {
                        w.flush();
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
     * @return instance of {@link SinkBufResource}
     */
    static <O> SinkBufResource<BufferedWriter, O> bufferedWriter(File file, Function<O, String> enc) {
        return SinkBufResource.of(
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
                },
                w -> {
                    try {
                        w.flush();
                    } catch (IOException e) {
                        throw new BatchIOException(e);
                    }
                });
    }

    /**
     * Flushes this resource by writing any buffered output to the underlying resource.
     */
    void flush();
}
