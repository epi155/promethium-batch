package io.github.epi155.batch.plugin;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
public class PushSourceGenerator extends ClassSourceGeneratorLot {

    private final GenerateContext gcx;

    public PushSourceGenerator(File baseDir, GenerateContext gcx) {
        super(baseDir, gcx);
        this.gcx = gcx;
    }

    protected void createClass(PrintModel ipw, Range range) throws FileNotFoundException {
        writeImport(ipw);
        writeDeclareClass(ipw);
        writeConstructor(ipw);
        writeMethodImpl(ipw);

        for (int k: range) {
            if (k<1) continue;
            new AsyncWorkerGenerator(baseDir, gcx).generate("AsyncWorker" + k, k);
            new WorkerGenerator(baseDir, gcx).generate("Worker" + k, k);
            if (k > 1) {
                new TupleGenerator(baseDir, gcx).generate("Tuple" + k, k);
            }
            new AsyncLoopGenerator(baseDir, gcx).generate("AsyncLoop" + k, k);
            new ParallelLoopGenerator(baseDir, gcx).generate("ParallelLoop" + k, k);
            new IterableLoopGenerator(baseDir, gcx).generate("IterableLoop" + k, k);

            new PmIterableLoopGenerator(baseDir, gcx).generate("PmIterableLoop" + k, k);
            writeMethodInto(ipw, k);
        }
        ipw.ends();
    }

    private void writeMethodInto(PrintModel ipw, int n) {
        ipw.println();
        ipw.override();
        ipw.printf("public <");
        genericsAnte(ipw, n);

        ipw.printf("IterableLoop%d<I,", n);
        for (int k = 1; k <= n; k++) {
            ipw.putf(" O%d", k);
            ipw.putf(k < n ? "," : ">");
        }
        ipw.putf(" into(%n");
        ipw.more();
        ipw.more();
        for (int k = 1; k <= n; k++) {
            ipw.printf("SinkResource<T%1$d, O%1$d> sink%1$d", k);
            ipw.putf(k < n ? ",%n" : ") {%n");
        }
        ipw.less();

        ipw.printf("return new PmIterableLoop%d<>(this, ", n);
        for (int k = 1; k <= n; k++) {
            ipw.putf(" sink%d", k);
            ipw.putf(k < n ? "," : ");%n");
        }
        ipw.ends();

    }

    private void genericsAnte(PrintModel ipw, int n) {
        for (int k = 1; k <= n; k++) {
            if (k == 1) {
                ipw.putf("T%1$d extends AutoCloseable, O%1$d", k);
            } else {
                ipw.printf("T%1$d extends AutoCloseable, O%1$d", k);
            }
            if (k < n) {
                ipw.putf(",%n");
                if (k == 1) {
                    ipw.more();
                    ipw.more();
                }
            } else {
                ipw.putf(">%n");
                if (n > 1) {
                    ipw.less();
                    ipw.less();
                }
            }
        }
    }

    private void writeMethodImpl(PrintModel ipw) {
        ipw.override();
        ipw.println("public LoopSourceStd<I> before(Runnable action) {");
        ipw.more();
        ipw.println("this.beforeAction = action;");
        ipw.println("return this;");
        ipw.ends();

        ipw.override();
        ipw.println("public LoopSourceLayer<I> terminate(Predicate<? super I> test) {");
        ipw.more();
        ipw.println("this.terminateTest = test;");
        ipw.println("return this;");
        ipw.ends();
    }

    private void writeConstructor(PrintModel ipw) {
        ipw.println("PmPushSource(SourceResource<S, I> source) {");
        ipw.more();
        ipw.println("super(source);");
        ipw.ends();
    }

    private void writeDeclareClass(PrintModel ipw) {
        ipw.println();
        ipw.println("@Slf4j");
        ipw.println("abstract class PmPushSource<S extends AutoCloseable, I> extends PmPushNone<S, I> implements LoopSource<I> {");
        ipw.more();
    }

    private void writeImport(PrintModel ipw) {
        if (! pgmPackageName.equals(stepPackageName)) {
            ipw.printf("import %s.PmPushNone;%n", stepPackageName);
            ipw.printf("import %s.SinkResource;%n", stepPackageName);
            ipw.printf("import %s.SourceResource;%n", stepPackageName);
            ipw.println();
        }
        ipw.println("import lombok.extern.slf4j.Slf4j;");
        ipw.println();
        ipw.println("import java.util.function.Predicate;");
    }
}
