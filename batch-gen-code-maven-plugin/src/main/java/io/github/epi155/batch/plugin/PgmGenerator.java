package io.github.epi155.batch.plugin;

import java.io.File;
import java.io.FileNotFoundException;

public class PgmGenerator extends ClassSourceGenerator{
    private final int maxInp;

    public PgmGenerator(File baseDir, String packageName, int maxInp) {
        super(baseDir, packageName);
        this.maxInp = maxInp;
    }

    @Override
    protected void createClass(PrintModel ipw, int dummy) throws FileNotFoundException {
        writeDocInterface(ipw);
        writeDeclareInterface(ipw);
        ipw.more();
        writeDocPushMethod(ipw);
        writePushMethodApi(ipw);
        for(int k=2; k<=maxInp; k++) {
            writeDocPullMethod(ipw, k);
            writePullMethodApi(ipw, k);
        }
        ipw.ends();

    }

    private void writePullMethodApi(PrintModel ipw, int k) {
        ipw.printf("static <%n");
        ipw.more();
        ipw.more();
        for(int i=1; i<=k; i++) {
            ipw.printf("S%1$d extends AutoCloseable, I%1$d", i);
            ipw.putf(i<k ? ",%n" : ">%n");
        }
        ipw.less();
        ipw.less();
        ipw.printf("PullSource%d<", k);
        for(int i=1; i<=k; i++) {
            ipw.putf("I%d", i);
            ipw.putf(i<k ? "," : "> from(%n");
        }
        ipw.more();
        ipw.more();
        for(int i=1; i<=k; i++) {
            ipw.printf("SourceResource<S%1$d, I%1$d> src%1$d", i);
            ipw.putf(i<k ? ",%n" : ") {%n");
        }
        ipw.less();
        ipw.printf("return new PmPullSource%d<>(", k);
        for(int i=1; i<=k; i++) {
            ipw.putf("src%d", i);
            ipw.putf(i<k ? "," : ");%n");
        }
        ipw.ends();
    }

    private void writeDocPullMethod(PrintModel ipw, int k) {
        ipw.println();
        ipw.javadocOpen();
        ipw.printf(" * Sets %d source resource<br>%n", k);
        ipw.printf(" * <pre>%n");
        ipw.printf(" * Pgm<b>.from(");
        for(int i=1; i<=k; i++) {
            ipw.putf("src%d", i);
            if (i<k) ipw.putf(",");
        }
        ipw.putf(")</b>.into(...).proceed((");
        for(int i=1; i<=k; i++) {
            ipw.putf("it%d,", i);
        }
        ipw.putf("...) -&gt; ...)%n");
        ipw.printf(" * </pre>%n");
        ipw.printf(" * %n");
        for(int i=1; i<=k; i++) {
            ipw.printf(" * @param src%1$d the #%1$d source resource%n", i);
        }
        for(int i=1; i<=k; i++) {
            ipw.printf(" * @param <S%1$d>    #%1$d source resource type%n", i);
            ipw.printf(" * @param <I%1$d>    #%1$d source element type%n", i);
        }
        ipw.printf(" * @return instance of {@link PullSource%d}%n", k);
        ipw.javadocClose();
    }

    private void writePushMethodApi(PrintModel ipw) {
        ipw.printf("static <S extends AutoCloseable, I> LoopSource<I> from(SourceResource<S, I> src) {%n");
        ipw.more();
        ipw.printf("return new PmLoopSource<>(src);%n");
        ipw.ends();
    }

    private void writeDocPushMethod(PrintModel ipw) {
        ipw.javadocOpen();
        ipw.printf(" * Sets a source resource<br>%n");
        ipw.printf(" * <pre>%n");
        ipw.printf(" * Pgm<b>.from(src)</b>.into(...).forEach(it -&gt; ...)%n");
        ipw.printf(" * </pre>%n");
        ipw.printf(" * %n");
        ipw.printf(" * @param src the source resource%n");
        ipw.printf(" * @param <S>    source resource type%n");
        ipw.printf(" * @param <I>    source element type%n");
        ipw.printf(" * @return instance of {@link LoopSource}%n");
        ipw.javadocClose();
    }

    private void writeDeclareInterface(PrintModel ipw) {
        ipw.printf("public interface Pgm {%n");
    }

    private void writeDocInterface(PrintModel ipw) {
        ipw.javadocOpen();
        ipw.printf(" * root interface for the batch process%n");
        ipw.javadocClose();
    }
}
