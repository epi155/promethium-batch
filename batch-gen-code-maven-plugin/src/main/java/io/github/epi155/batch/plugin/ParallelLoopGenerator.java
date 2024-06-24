package io.github.epi155.batch.plugin;

import java.io.File;

public class ParallelLoopGenerator extends ClassSourceGenerator {
    public ParallelLoopGenerator(File baseDir, String packageName) {
        super(baseDir, packageName);
    }

    @Override
    protected void createClass(PrintModel ipw, int k) {
        writeImport(ipw);
        ipw.println();
        writeDocInterface(ipw, k);
        writeDeclareInterface(ipw, k);
        ipw.more();
        writeParallelWorker(ipw, k);
        writeParallelTupleFair(ipw, k);
        writeParallelTupleRaw(ipw, k);
        writeAsyncWorker(ipw, k);
        writeAsyncTuple(ipw, k);
        ipw.ends();
    }

    private void writeAsyncTuple(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * performs repeated transformation from input to output using asynchronous task%n");
        ipw.printf(" * <p>first ends first writes%n");
        ipw.printf(" * <pre>Pgm.from(src).into(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("snk%d", i);
            ipw.putf(i < k ? "," : ").forEachAsync(n,i -> { ... });</pre>%n");
        }
        ipw.printf(" *%n");
        ipw.printf(" * @param maxThread maximum number of parallel processing%n");
        if (k == 1) {
            ipw.printf(" * @param asyncTransformer asynchronous function that transforms input into outputs%n");
        } else {
            ipw.printf(" * @param asyncTransformer asynchronous function that transforms input into {@link Tuple%d} outputs%n", k);
        }
        ipw.javadocClose();

        ipw.printf("void forEachAsync(int maxThread, Function<%n");
        ipw.more();
        ipw.more();
        ipw.printf("? super I,%n");
        if (k == 1) {
            ipw.printf("? extends Future<? extends O%d>> asyncTransformer);%n", k);
        } else {
            ipw.printf("? extends Future<? extends Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">>> asyncTransformer);%n");
            }
            ipw.less();
            ipw.less();
        }
        ipw.less();
        ipw.less();
    }

    private void writeAsyncWorker(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * performs repeated action from input to output using asynchronous worker%n");
        ipw.printf(" * <pre>Pgm.from(src).into(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("snk%d", i);
            ipw.putf(i < k ? "," : ").forEachAsync(n,(i,");
        }
        for (int i = 1; i <= k; i++) {
            ipw.putf("wr%d", i);
            ipw.putf(i < k ? "," : ") -> { ... });</pre>%n");
        }
        ipw.printf(" *%n");
        ipw.printf(" * @param maxThread maximum number of parallel processing%n");
        ipw.printf(" * @param asyncWorker asyncWorker who takes the input value and writes the output using the consumer%n");
        ipw.javadocClose();

        ipw.printf("void forEachAsync(int maxThread, AsyncWorker%d<? super I,%n", k);
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("Consumer<? super O%d>", i);
            ipw.putf(i < k ? ",%n" : "> asyncWorker);%n");
        }
        ipw.less();
        ipw.less();
    }

    private void writeParallelTupleRaw(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * performs repeated transformation from input to outputs in parallel%n");
        ipw.printf(" * <p>first ends first writes%n");
        ipw.printf(" * <pre>Pgm.from(src).into(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("snk%d", i);
            ipw.putf(i < k ? "," : ").forEachParallel(n,i -> { ... });</pre>%n");
        }
        ipw.printf(" *%n");
        ipw.printf(" * @param maxThread maximum number of parallel processing%n");
        if (k == 1) {
            ipw.printf(" * @param transformer function that transforms input into outputs%n");
        } else {
            ipw.printf(" * @param transformer function that transforms input into {@link Tuple%d} outputs%n", k);
        }
        ipw.javadocClose();

        ipw.printf("void forEachParallel(int maxThread, Function<%n");
        ipw.more();
        ipw.more();
        ipw.printf("? super I,%n");
        if (k == 1) {
            ipw.printf("? extends O%d> transformer);%n", k);
        } else {
            ipw.printf("? extends Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">> transformer);%n");
            }
            ipw.less();
            ipw.less();
        }
        ipw.less();
        ipw.less();
    }

    private void writeParallelTupleFair(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * performs repeated transformation from input to outputs in parallel%n");
        ipw.printf(" * <p>first starts first writes%n");
        ipw.printf(" * <pre>Pgm.from(src).into(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("snk%d", i);
            ipw.putf(i < k ? "," : ").forEachParallelFair(n,i -> { ... });</pre>%n");
        }
        ipw.printf(" *%n");
        ipw.printf(" * @param maxThread maximum number of parallel processing%n");
        if (k == 1) {
            ipw.printf(" * @param transformer function that transforms input into outputs%n");
        } else {
            ipw.printf(" * @param transformer function that transforms input into {@link Tuple%d} outputs%n", k);
        }
        ipw.javadocClose();

        ipw.printf("void forEachParallelFair(int maxThread, Function<%n");
        ipw.more();
        ipw.more();
        ipw.printf("? super I,%n");
        if (k == 1) {
            ipw.printf("? extends O%d> transformer);%n", k);
        } else {
            ipw.printf("? extends Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">> transformer);%n");
            }
            ipw.less();
            ipw.less();
        }
        ipw.less();
        ipw.less();
    }

    private void writeParallelWorker(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * performs repeated action from input to output in parallel%n");
        ipw.printf(" * <pre>Pgm.from(src).into(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("snk%d", i);
            ipw.putf(i < k ? "," : ").forEachParallel(n,(i,");
        }
        for (int i = 1; i <= k; i++) {
            ipw.putf("wr%d", i);
            ipw.putf(i < k ? "," : ") -> { ... });</pre>%n");
        }
        ipw.printf(" *%n");
        ipw.printf(" * @param maxThread maximum number of parallel processing%n");
        ipw.printf(" * @param worker    worker who takes the input value and writes the outputs using the consumers%n");
        ipw.javadocClose();

        ipw.printf("void forEachParallel(int maxThread, Worker%d<? super I,%n", k);
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("Consumer<? super O%d>", i);
            ipw.putf(i < k ? ",%n" : "> worker);%n");
        }
        ipw.less();
        ipw.less();
    }

    private void writeDeclareInterface(PrintModel ipw, int k) {
        ipw.printf("public interface ParallelLoop%d<I,", k);
        for (int i = 1; i <= k; i++) {
            ipw.putf("O%d", i);
            ipw.putf(i < k ? "," : "> {%n");
        }
    }

    private void writeDocInterface(PrintModel ipw, int k) {
        ipw.javadocOpen();
        ipw.printf(" * interface to provide 1 input item and %d writer for asynchronous process%n", k);
        ipw.printf(" *%n");
        ipw.printf(" * @param <I>  input type%n");
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param <O%1$d> #%1$d output type%n", i);
        }
        ipw.javadocClose();
    }

    private void writeImport(PrintModel ipw) {
        ipw.printf("import java.util.concurrent.Future;%n");
        ipw.printf("import java.util.function.Consumer;%n");
        ipw.printf("import java.util.function.Function;%n");
    }
}
