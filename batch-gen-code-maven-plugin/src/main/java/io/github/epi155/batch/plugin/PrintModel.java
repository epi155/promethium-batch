package io.github.epi155.batch.plugin;

public interface PrintModel {
    void printf(String format, Object... objects);

    void putf(String format, Object... objects);

    void more();

    void println();

    void less();

    void ends();

    void javadocOpen();

    void javadocClose();

    void override();
}
