package io.github.epi155.batch.plugin;

import java.io.File;

public class AsyncWorkerGenerator extends ClassSourceGenerator {
    public AsyncWorkerGenerator(File baseDir, String packageName) {
        super(baseDir, packageName);
    }

    @Override
    protected void createClass(PrintModel ipw, int k) {
        writeImport(ipw);
        writeDocInterface(ipw, k);
        writeDeclareInterface(ipw, k);
        ipw.more();
        writeDocMethod(ipw, k);
        writeMethodApi(ipw, k);
        ipw.ends();
    }

    private void writeMethodApi(PrintModel ipw, int k) {
        ipw.printf("Future<?> apply(I i,");
        for (int i = 1; i <= k; i++) {
            ipw.putf("W%1$d w%1$d", i);
            ipw.putf(i < k ? "," : ");%n");
        }
    }

    private void writeDocMethod(PrintModel ipw, int k) {
        ipw.javadocOpen();
        ipw.printf(" * provide 1 input item and %d writer for asynchronous process%n", k);
        ipw.printf(" *%n");
        ipw.printf(" * @param i  input item%n");
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param w%1$d #%1$d writer%n", i);
        }
        ipw.printf(" * @return {@link Future} instance of asynchronous process%n");
        ipw.javadocClose();
    }

    private void writeDeclareInterface(PrintModel ipw, int k) {
        ipw.printf("public interface AsyncWorker%d<I,", k);
        for (int i = 1; i <= k; i++) {
            ipw.putf("W%d", i);
            ipw.putf(i < k ? "," : "> {%n");
        }
    }

    private void writeDocInterface(PrintModel ipw, int k) {
        ipw.javadocOpen();
        ipw.printf(" * interface to provide 1 input item and %d writer for asynchronous process%n", k);
        ipw.printf(" *%n");
        ipw.printf(" * @param <I>  input type%n");
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param <W%1$d> #%1$d output writer%n", i);
        }
        ipw.javadocClose();
    }

    private void writeImport(PrintModel ipw) {
        ipw.printf("import java.util.concurrent.Future;%n");
    }
}
