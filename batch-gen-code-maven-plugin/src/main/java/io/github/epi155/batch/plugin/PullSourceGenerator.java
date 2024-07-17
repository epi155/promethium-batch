package io.github.epi155.batch.plugin;

import java.io.File;
import java.io.FileNotFoundException;

public class PullSourceGenerator extends ClassSourceGenerator {
    private final int nmInp;

    public PullSourceGenerator(File baseDir, String packageName, int nmInp) {
        super(baseDir, packageName);
        this.nmInp =  nmInp;
    }

    @Override
    protected void createClass(PrintModel ipw, int max) throws FileNotFoundException {
        writeDocInterface(ipw);
        writeDeclareInterface(ipw);
        ipw.more();
        writeDocProceedMethod(ipw);
        writeProceedMethodApi(ipw);
        for(int k=1; k<=max; k++) {
            writeDocIntoMethod(ipw, k);
            writeIntoMethodApi(ipw, k);
        }
        ipw.ends();
    }

    private void writeIntoMethodApi(PrintModel ipw, int k) {
        ipw.printf("<%n");
        ipw.more();
        ipw.more();
        for (int i=1; i<=k; i++) {
            ipw.printf("T%1$d extends AutoCloseable, O%1$d", i);
            ipw.putf(i<k ? ",%n" : ">%n");
        }
        ipw.less();
        ipw.less();
        ipw.printf("PullProcess%do%d<", nmInp, k);
        for (int i=1; i<=nmInp; i++) {
            ipw.putf("I%d,", i);
        }
        for (int i=1; i<=k; i++) {
            ipw.putf("O%d", i);
            ipw.putf(i<k ? "," : "> into(%n");
        }
        ipw.more();
        ipw.more();
        for (int i=1; i<=k; i++) {
            ipw.printf("SinkResource<T%1$d, O%1$d> sink%1$d", i);
            ipw.putf(i<k ? ",%n" : ");%n");
        }
        ipw.less();
        ipw.less();
    }

    private void writeDocIntoMethod(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * sets the sinks resource to process the data%n");
        ipw.printf(" * %n");
        for(int i=1; i<=k; i++) {
            ipw.printf(" * @param sink%1$d #%1$d sink resource%n", i);
        }
        for(int i=1; i<=k; i++) {
            ipw.printf(" * @param <T%1$d>  #%1$d sink resource type%n", i);
            ipw.printf(" * @param <O%1$d>  #%1$d sink element type%n", i);
        }
        ipw.printf(" * @return instance of {@link PullProcess%do%d}%n", nmInp, k);
        ipw.javadocClose();
    }

    private void writeProceedMethodApi(PrintModel ipw) {
        ipw.printf("void proceed(PullWorker%do0<", nmInp);
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("I%d", i);
            ipw.putf(i<nmInp ? "," : "> worker);%n");
        }
    }

    private void writeDocProceedMethod(PrintModel ipw) {
        ipw.javadocOpen();
        ipw.printf(" * processes the data%n");
        ipw.printf(" * <pre>%n");
        ipw.printf(" * Pgm.from(");
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("src%d", i);
            ipw.putf(i < nmInp ? "," : ")");
        }
        ipw.putf(".proceed((");
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("rd%d", i);
            if (i<nmInp) ipw.putf(",");
        }
        ipw.putf(") -> {%n");
        for(int i=1; i<=nmInp; i++) {
            ipw.printf(" *     val i%1$d = rd%1$d.get();%n", i);
        }
        ipw.printf(" *     ...%n");
        ipw.printf(" * });%n");
        ipw.printf(" * </pre>%n");
        ipw.printf(" *%n");
        ipw.printf(" * @param worker worker who read the input values%n");
        ipw.javadocClose();
    }

    private void writeDeclareInterface(PrintModel ipw) {
        ipw.println();
        ipw.printf("public interface PullSource%d<", nmInp);
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("I%d", i);
            ipw.putf(i<nmInp ? "," : "> {%n");
        }
    }

    private void writeDocInterface(PrintModel ipw) {
        ipw.javadocOpen();
        ipw.printf(" * interface to manage the elements of two input resources.%n", nmInp);
        ipw.printf(" * <p>%n");
        ipw.printf(" * unlike the single source, where it is possible to push the data towards the processing process, in the case of%n");
        ipw.printf(" * multiple sources it is the processing process that decides from which source, and when, to pull the data to be%n");
        ipw.printf(" * processed%n");
        ipw.printf(" *%n");
        for(int i=1; i<=nmInp; i++) {
            ipw.printf(" * @param <I%1$d> element type of the #%1$d resource%n", i);
        }
        ipw.javadocClose();
    }
}
