package io.github.epi155.batch.plugin;

import java.io.File;

public class IterableLoopGenerator extends ClassSourceGenerator {

    public IterableLoopGenerator(File baseDir, String packageName) {
        super(baseDir, packageName);
    }

    @Override
    protected void createClass(PrintModel ipw, int k) {
        writeImport(ipw);
        ipw.println();
        writeDocInterface(ipw, k);
        writeDeclareInterface(ipw, k);
        ipw.more();
        writeWorker(ipw, k);
        writeTuple(ipw, k);
        writeShutdownTmo(ipw, k);
        ipw.ends();
    }

    private void writeShutdownTmo(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * sets the shutdown timeout for parallel processing%n");
        ipw.printf(" *%n");
        ipw.printf(" * @param time time amount%n");
        ipw.printf(" * @param unit time unit%n");
        ipw.printf(" * @return instance of {@link ParallelLoop%d} for run parallel processing%n", k);
        ipw.javadocClose();

        ipw.printf("ParallelLoop%d<I,%n", k);
        for (int i = 1; i <= k; i++) {
            ipw.putf(" O%d", i);
            ipw.putf(i < k ? "," : "> shutdownTimeout(long time, TimeUnit unit);%n");
        }
    }

    private void writeTuple(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * performs repeated transformation from input to outputs sequentially%n");
        ipw.printf(" * <pre>Pgm.from(src).into(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("snk%d", i);
            ipw.putf(i < k ? "," : ").forEach(n,i -> { ... });</pre>%n");
        }
        ipw.printf(" *%n");
        if (k == 1) {
            ipw.printf(" * @param transformer function that transforms input into outputs%n");
        } else {
            ipw.printf(" * @param transformer function that transforms input into {@link Tuple%d} outputs%n", k);
        }
        ipw.javadocClose();

        ipw.printf("void forEach(Function<%n");
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

    private void writeWorker(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * performs repeated action from input to outputs sequentially%n");
        ipw.printf(" * <pre>Pgm.from(src).into(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("snk%d", i);
            ipw.putf(i < k ? "," : ").forEach(n,(i,");
        }
        for (int i = 1; i <= k; i++) {
            ipw.putf("wr%d", i);
            ipw.putf(i < k ? "," : ") -> { ... });</pre>%n");
        }
        ipw.printf(" *%n");
        ipw.printf(" * @param worker    worker who takes the input value and writes the outputs using the consumers%n");
        ipw.javadocClose();

        ipw.printf("void forEach(Worker%d<? super I,%n", k);
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
        ipw.printf("public interface IterableLoop%d<I,", k);
        for (int i = 1; i <= k; i++) {
            ipw.putf(" O%d", i);
            ipw.putf(i < k ? "," : ">%n");
        }
        ipw.more();
        ipw.more();
        ipw.printf("extends ParallelLoop%d<I,", k);
        for (int i = 1; i <= k; i++) {
            ipw.putf(" O%d", i);
            ipw.putf(i < k ? "," : "> {%n");
        }
        ipw.less();
        ipw.less();
    }

    private void writeDocInterface(PrintModel ipw, int k) {
        ipw.javadocOpen();
        ipw.printf(" * interface to handle a repeated transformation from 1 input to %d outputs%n", k);
        ipw.printf(" *%n");
        ipw.printf(" * @param <I>  input type%n");
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param <O%1$d> #%1$d output type%n", i);
        }
        ipw.javadocClose();
    }

    private void writeImport(PrintModel ipw) {
        ipw.printf("import java.util.concurrent.TimeUnit;%n");
        ipw.printf("import java.util.function.Consumer;%n");
        ipw.printf("import java.util.function.Function;%n");
    }

}
