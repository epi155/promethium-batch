package io.github.epi155.batch.plugin;

import java.io.File;
import java.io.FileNotFoundException;

public class PullWorkerGenerator extends ClassSourceGenerator {
    private final int nmInp;

    protected PullWorkerGenerator(File baseDir, String packageName, int nmInp) {
        super(baseDir, packageName);
        this.nmInp = nmInp;
    }

    @Override
    protected void createClass(PrintModel ipw, int k) throws FileNotFoundException {
        writeImport(ipw);
        writeDocInterface(ipw, k);
        writeDeclareInterface(ipw, k);
        ipw.more();
        writeDocMethod(ipw, k);
        writeMethodApi(ipw, k);
        ipw.ends();
    }

    private void writeMethodApi(PrintModel ipw, int k) {
        ipw.printf("void proceed(%n");
        ipw.more();
        ipw.more();
        for(int i=1; i<=nmInp; i++) {
            ipw.printf("Supplier<? extends I%1$d> rd%1$d", i);
            if (i<nmInp) ipw.putf(",%n");
        }
        for (int i = 1; i <= k; i++) {
            ipw.putf(",%n");
            ipw.printf("Consumer<? super O%1$d> wr%1$d", i);
        }
        ipw.putf(");%n");
        ipw.less();
        ipw.less();
    }

    private void writeDocMethod(PrintModel ipw, int k) {
        ipw.javadocOpen();
        ipw.printf(" * provide %d readers and %d writer%n", nmInp, k);
        ipw.printf(" *%n");
        for (int i = 1; i <= nmInp; i++) {
            ipw.printf(" * @param rd%1$d #%1$d reader%n", i);
        }
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param wr%1$d #%1$d writer%n", i);
        }
        ipw.javadocClose();
    }

    private void writeDeclareInterface(PrintModel ipw, int k) {
        ipw.printf("public interface PullWorker%do%d<", nmInp, k);
        for (int i = 1; i <= nmInp; i++) {
            ipw.putf("I%d", i);
            if (i<nmInp) ipw.putf(",");
        }
        for (int i = 1; i <= k; i++) {
            ipw.putf(",O%d", i);
        }
        ipw.putf("> {%n");
    }

    private void writeDocInterface(PrintModel ipw, int k) {
        ipw.javadocOpen();
        ipw.printf(" * interface to provide %d readers and %d writer%n", nmInp, k);
        ipw.printf(" *%n");
        for (int i = 1; i <= nmInp; i++) {
            ipw.printf(" * @param <I%1$d> input type for #%1$d reader%n", i);
        }
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param <O%1$d> output type for writer #%1$d%n", i);
        }
        ipw.javadocClose();
    }

    private void writeImport(PrintModel ipw) {
        ipw.printf("import java.util.function.Consumer;%n");
        ipw.printf("import java.util.function.Supplier;%n");
        ipw.println();
    }
}
