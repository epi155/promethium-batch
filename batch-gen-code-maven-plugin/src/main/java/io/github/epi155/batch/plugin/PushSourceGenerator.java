package io.github.epi155.batch.plugin;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
public class PushSourceGenerator extends ClassSourceGenerator {

    public PushSourceGenerator(File baseDir, String packageName) {
        super(baseDir, packageName);
    }

    protected void createClass(PrintModel ipw, int maxOut) throws FileNotFoundException {
        writeImport(ipw);
        writeDeclareClass(ipw);
        writeConstructor(ipw);

        for (int k = 1; k <= maxOut; k++) {
            new AsyncWorkerGenerator(baseDir, packageName).generate("AsyncWorker" + k, k);
            new WorkerGenerator(baseDir, packageName).generate("Worker" + k, k);
            if (k > 1) {
                new TupleGenerator(baseDir, packageName).generate("Tuple" + k, k);
            }
            new ParallelLoopGenerator(baseDir, packageName).generate("ParallelLoop" + k, k);
            new IterableLoopGenerator(baseDir, packageName).generate("IterableLoop" + k, k);

            new PmIterableLoopGenerator(baseDir, packageName).generate("PmIterableLoop" + k, k);
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

    private void writeConstructor(PrintModel ipw) {
        ipw.printf("PmPushSource(SourceResource<S, I> source) {%n");
        ipw.more();
        ipw.printf("super(source);%n");
        ipw.ends();
    }

    private void writeDeclareClass(PrintModel ipw) {
        ipw.println();
        ipw.printf("@Slf4j%n");
        ipw.printf("abstract class PmPushSource<S extends AutoCloseable, I> extends PmPushNone<S, I> {%n");
        ipw.more();
    }

    private void writeImport(PrintModel ipw) {
        ipw.printf("import lombok.extern.slf4j.Slf4j;%n");
    }
}
