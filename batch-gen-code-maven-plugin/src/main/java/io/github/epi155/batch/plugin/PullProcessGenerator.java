package io.github.epi155.batch.plugin;

import java.io.File;
import java.io.FileNotFoundException;

public class PullProcessGenerator extends ClassSourceGenerator {
    private final int nmInp;

    public PullProcessGenerator(File baseDir, String packageName, int nmInp) {
        super(baseDir, packageName);
        this.nmInp = nmInp;
    }

    @Override
    protected void createClass(PrintModel ipw, int k) throws FileNotFoundException {
        writeDocInterface(ipw, k);
        writeDeclareInterface(ipw, k);
        ipw.more();
        writeDocMethod(ipw, k);
        writeMethodApi(ipw, k);
        ipw.ends();
    }

    private void writeMethodApi(PrintModel ipw, int k) {
        ipw.printf("void proceed(PullWorker%do%d<", nmInp, k);
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("I%d", i);
            if (i<nmInp) ipw.putf(",");
        }
        for(int i=1; i<=k; i++) {
            ipw.putf(",O%d", i);
        }
        ipw.putf("> worker);%n");
    }

    private void writeDocMethod(PrintModel ipw, int k) {
        ipw.javadocOpen();
        ipw.printf(" * processes the data%n");
        ipw.printf(" * <pre>%n");
        ipw.putf(" * Pgm.from(");
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("src%d", i);
            ipw.putf(i < nmInp ? "," : ")");
        }
        if (k>0) {
            ipw.putf("into(");
            for(int i=1; i<=k; i++) {
                ipw.putf("snk%d", i);
                ipw.putf(i < k ? "," : ")");
            }
        }
        ipw.putf(".proceed((");
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("rd%d", i);
            if (i<nmInp) ipw.putf(",");
        }
        for(int i=1; i<=k; i++) {
            ipw.putf(",wr%d", i);
        }
        ipw.putf(") -> {");
        for(int i=1; i<=nmInp; i++) {
            ipw.printf(" *     val i%1$d = rd%1$d.get();%n", i);
        }
        ipw.printf(" *     ...%n");
        for(int i=1; i<=k; i++) {
            ipw.printf(" *     wr%1$d.accept(o%1$d);%n", i);
        }
        ipw.printf(" * });%n");
        ipw.printf(" * </pre>%n");
        ipw.printf(" *%n");
        ipw.printf(" * @param worker worker who read the input values and writes the output value%n");
        ipw.javadocClose();
    }

    private void writeDeclareInterface(PrintModel ipw, int k) {
        ipw.printf("public interface PullProcess%do%d<", nmInp, k);
        for(int i=1; i<=nmInp; i++) {
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
        ipw.printf(" * interface to manage %d input to %d output processing%n", nmInp, k);
        ipw.printf(" *%n");
        for(int i=1; i<=nmInp; i++) {
            ipw.printf(" * @param <I%1$d> #%1$d input type%n", i);
        }
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param <O%1$d> #%1$d output type%n", i);
        }
        ipw.javadocClose();
    }
}
