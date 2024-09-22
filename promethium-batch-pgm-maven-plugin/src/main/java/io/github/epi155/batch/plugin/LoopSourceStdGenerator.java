package io.github.epi155.batch.plugin;

import java.io.File;

public class LoopSourceStdGenerator extends ClassSourceGeneratorLot {
    public LoopSourceStdGenerator(File baseDir, GenerateContext gcx) {
        super(baseDir, gcx);
    }

    @Override
    protected void createClass(PrintModel ipw, Range range) {
        writeImport(ipw);
        writeDocInterface(ipw);
        writeDeclareInterface(ipw);
        ipw.more();
        for (int k: range) {
            if (k<1) continue;
            writeDocInto(ipw, k);
            writeMethodInto(ipw, k);
        }
        ipw.ends();
    }

    private void writeImport(PrintModel ipw) {
        if (! pgmPackageName.equals(stepPackageName)) {
            ipw.printf("import %s.LoopSourceZro;%n", stepPackageName);
            ipw.printf("import %s.SinkResource;%n", stepPackageName);
            ipw.println();
        }
    }

    private void writeMethodInto(PrintModel ipw, int k) {
        methodGenerics(ipw, k);
        methodSignature(ipw, k);
        methodArguments(ipw, k);
    }

    private void methodArguments(PrintModel ipw, int k) {
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("SinkResource<T%1$d, O%1$d> sink%1$d", i);
            ipw.putf(i < k ? ",%n" : ");%n");
        }
        ipw.less();
        ipw.less();
    }

    private void methodSignature(PrintModel ipw, int k) {
        ipw.printf("IterableLoop%d<I,", k);
        for (int i = 1; i <= k; i++) {
            ipw.putf(" O%d", i);
            ipw.putf(i < k ? "," : "> into(%n");
        }
    }

    private void methodGenerics(PrintModel ipw, int k) {
        ipw.printf("<");
        for (int i = 1; i <= k; i++) {
            if (i == 1) {
                ipw.putf("T%1$d extends AutoCloseable, O%1$d", i);
            } else {
                ipw.printf("T%1$d extends AutoCloseable, O%1$d", i);
            }
            ipw.putf(i < k ? ",%n" : ">%n");
            if (i == 1 && k > 1) {
                ipw.more();
                ipw.more();
            }
        }
        if (k > 1) {
            ipw.less();
            ipw.less();
        }
    }

    private void writeDocInto(PrintModel ipw, int k) {
        ipw.javadocOpen();
        ipw.printf(" * set %d output resources%n", k);
        ipw.printf(" * <pre>Pgm.from(source)<b>.into(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("sink%d", i);
            ipw.putf(i < k ? "," : ")</b>.forEach(src -&gt; ...)</pre>");
        }
        ipw.printf(" *%n");
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param sink%1$d #%1$dt sink resource%n", i);
        }
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param <T%1$d>  #%1$d sink resource type%n", i);
            ipw.printf(" * @param <O%1$d>  #%1$d sink element type%n", i);
        }
        ipw.printf(" * @return instance of {@link IterableLoop%d}%n", k);
        ipw.javadocClose();
    }

    private void writeDeclareInterface(PrintModel ipw) {
        ipw.printf("public interface LoopSourceStd<I> extends LoopSourceZro<I> {%n");
    }

    private void writeDocInterface(PrintModel ipw) {
        ipw.javadocOpen();
        ipw.printf(" * interface to manage the elements of a single input resource.%n");
        ipw.printf(" * <p>%n");
        ipw.printf(" * it is possible to process the input immediately or to define one or more%n");
        ipw.printf(" * output resources where the input processing can be sent%n");
        ipw.printf(" *%n");
        ipw.printf(" * @param <I> input type%n");
        ipw.javadocClose();
    }

}
