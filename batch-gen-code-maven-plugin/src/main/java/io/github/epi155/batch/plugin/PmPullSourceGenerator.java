package io.github.epi155.batch.plugin;

import java.io.File;
import java.io.FileNotFoundException;

public class PmPullSourceGenerator extends ClassSourceGenerator {
    private static final String END_ARGS = ") {%n";
    private final int nmInp;

    public PmPullSourceGenerator(File baseDir, String packageName, int nmInp) {
        super(baseDir, packageName);
        this.nmInp = nmInp;
    }

    @Override
    protected void createClass(PrintModel ipw, int max) throws FileNotFoundException {
        writeImport(ipw);
        writeDeclareClass(ipw);
        writeFields(ipw);
        writeConstructor(ipw);

        for(int k=0; k<=max; k++) {
            new PullWorkerGenerator(baseDir, packageName, nmInp).generate("PullWorker"+nmInp+"o" + k, k);
            if (k==0) {
                writeMethodProceed(ipw);
            } else {
                new PullProcessGenerator(baseDir, packageName, nmInp).generate("PullProcess"+nmInp+"o" + k, k);
                writeMethodInto(ipw, k);
            }
        }
        ipw.ends();
    }

    private void writeMethodInto(PrintModel ipw, int k) {
        ipw.println();
        ipw.override();
        ipw.printf("public <%n");
        ipw.more();
        ipw.more();
        for(int i=1; i<=k; i++) {
            ipw.printf("T%1$d extends AutoCloseable, O%1$d", i);
            ipw.putf(i<k ? ",%n": ">%n");
        }
        ipw.less();
        ipw.less();
        ipw.printf("PullProcess%do%d<", nmInp, k);
        genericsPost(ipw, k);
        ipw.putf("> into(%n");
        ipw.more();
        ipw.more();
        for(int i=1; i<=k; i++) {
            ipw.printf("SinkResource<T%1$d, O%1$d> sink%1$d", i);
            ipw.putf(i<k ? ",%n": END_ARGS);
        }
        ipw.less();
        ipw.printf("return worker -> {%n");
        ipw.more();
        ipw.printf("try (%n");
        ipw.more();
        ipw.more();
        for(int i=1; i<=k; i++) {
            ipw.printf("T%1$d t%1$d = sink%1$d.get();%n", i);
        }
        for(int i=1; i<=nmInp; i++) {
            ipw.printf("S%1$d s%1$d = source%1$d.get()", i);
            ipw.putf(i<nmInp ? ";%n" : "%n");
        }
        ipw.less();
        ipw.less();
        ipw.printf(END_ARGS);
        ipw.more();
        ipw.printf("worker.proceed(%n");
        ipw.more();
        ipw.more();
        for(int i=1; i<=nmInp; i++) {
            ipw.printf("source%1$d.supplier(s%1$d),%n", i);
        }
        for(int i=1; i<=k; i++) {
            ipw.printf("consumerOf(sink%1$d, t%1$d)", i);
            ipw.putf(i<k ? ",%n": ");%n");
        }
        ipw.less();
        ipw.less();
        ipw.less();
        ipw.printf("} catch (Exception e) {%n");
        ipw.more();
        ipw.printf("throw new BatchException(e);%n");
        ipw.ends();
        ipw.less();
        ipw.printf("};%n");
        ipw.ends();
    }

    private void genericsPost(PrintModel ipw, int k) {
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("I%d",i);
            if (i<nmInp) ipw.putf(",");
        }
        for(int i=1; i<=k; i++) {
            ipw.putf(",O%d", i);
        }
    }

    private void writeMethodProceed(PrintModel ipw) {
        ipw.println();
        ipw.override();
        ipw.printf("public void proceed(PullWorker%do0<", nmInp);
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("I%d", i);
            ipw.putf(i<nmInp ? "," : "> worker) {%n");
        }
        ipw.more();
        ipw.printf("try (%n");
        ipw.more();
        ipw.more();
        for(int i=1; i<=nmInp; i++) {
            ipw.printf("S%1$d s%1$d = source%1$d.get()", i);
            ipw.putf(i<nmInp ? ";%n" : "%n");
        }
        ipw.less();
        ipw.less();
        ipw.printf(END_ARGS);
        ipw.more();
        ipw.printf("worker.proceed(");
        for (int i=1; i<=nmInp; i++) {
            ipw.putf("source%1$d.supplier(s%1$d)", i);
            ipw.putf(i<nmInp ? "," : ");%n");
        }
        ipw.less();
        ipw.printf("} catch (Exception e) {%n");
        ipw.more();
        ipw.printf("throw new BatchException(e);%n");
        ipw.ends();
        ipw.ends();
    }

    private void writeConstructor(PrintModel ipw) {
        ipw.println();
        ipw.printf("public PmPullSource%d(%n", nmInp);
        ipw.more();
        ipw.more();
        for(int i=1; i<=nmInp; i++) {
            ipw.printf("SourceResource<S%1$d, I%1$d> source%1$d", i);
            ipw.putf(i<nmInp ? ",%n" : END_ARGS);
        }
        ipw.less();
        for(int i=1; i<=nmInp; i++) {
            ipw.printf("this.source%1$d = source%1$d;%n", i);
        }
        ipw.ends();
    }

    private void writeFields(PrintModel ipw) {
        for(int i=1; i<=nmInp; i++) {
            ipw.printf("private final SourceResource<S%1$d, I%1$d> source%1$d;%n", i);
        }
    }

    private void writeDeclareClass(PrintModel ipw) {
        ipw.println();
        ipw.printf("class PmPullSource%d<%n", nmInp);
        ipw.more();
        ipw.more();
        for(int i=1; i<=nmInp; i++) {
            ipw.printf("S%1$d extends AutoCloseable, I%1$d", i);
            ipw.putf(i<nmInp ? ",%n" : ">%n");
        }
        ipw.printf("implements PullSource%d<", nmInp);
        for(int i=1; i<=nmInp; i++) {
            ipw.putf("I%d", i);
            ipw.putf(i<nmInp ? "," : "> {%n");
        }
        ipw.less();
    }

    private void writeImport(PrintModel ipw) {
        ipw.printf("import static io.github.epi155.pm.batch.step.PmPushCore.consumerOf;%n");
    }
}
