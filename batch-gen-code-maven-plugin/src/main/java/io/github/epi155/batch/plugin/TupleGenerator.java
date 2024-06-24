package io.github.epi155.batch.plugin;

import java.io.File;

public class TupleGenerator extends ClassSourceGenerator {
    public TupleGenerator(File baseDir, String packageName) {
        super(baseDir, packageName);

    }

    @Override
    protected void createClass(PrintModel ipw, int k) {
        if (k < 2) throw new IllegalArgumentException("Tuple dimension invalid " + k + "(MUST be greatest than 1)");
        writeImport(ipw);
        ipw.println();
        writeDocInterface(ipw, k);
        writeDeclareClass(ipw, k);
        ipw.more();
        writeFields(ipw, k);
        for (int i = 1; i <= k; i++) {
            ipw.println();
            writeDocMethod(ipw, i);
            writeMethodApi(ipw, i);
            ipw.more();
            writeMethodBody(ipw, i);
            ipw.ends();
        }
        ipw.ends();

    }

    private void writeMethodBody(PrintModel ipw, int i) {
        ipw.printf("if (t%d != null)%n", i);
        ipw.more();
        ipw.printf("action.accept(t%d);%n", i);
        ipw.less();
    }

    private void writeMethodApi(PrintModel ipw, int i) {
        ipw.printf("public void onT%1$d(Consumer<? super O%1$d> action) {%n", i);
    }

    private void writeDocMethod(PrintModel ipw, int i) {
        ipw.javadocOpen();
        ipw.printf(" * performs the indicated action if the component #%d of the tuple is different from null%n", i);
        ipw.printf(" *%n");
        ipw.printf(" * @param action action to perform%n");
        ipw.javadocClose();
    }

    private void writeFields(PrintModel ipw, int k) {
        for (int i = 1; i <= k; i++) {
            ipw.printf("private final O%1$d t%1$d;%n", i);
        }
    }

    private void writeDeclareClass(PrintModel ipw, int k) {
        ipw.printf("@Getter%n");
        ipw.printf("@AllArgsConstructor(staticName = \"of\")%n");
        ipw.printf("@Builder(setterPrefix = \"with\")%n");
        ipw.printf("public class Tuple%d<", k);
        for (int i = 1; i <= k; i++) {
            ipw.putf("O%d", i);
            ipw.putf(i < k ? "," : "> {%n");
        }
    }

    private void writeDocInterface(PrintModel ipw, int k) {
        ipw.javadocOpen();
        ipw.printf(" * tuple with %d elements%n", k);
        ipw.printf(" *%n");
        for (int i = 1; i <= k; i++) {
            ipw.printf(" * @param <O%1$d> #%1$d element type%n", i);
        }
        ipw.javadocClose();
    }

    private void writeImport(PrintModel ipw) {
        ipw.printf("import lombok.AllArgsConstructor;%n");
        ipw.printf("import lombok.Builder;%n");
        ipw.printf("import lombok.Getter;%n");
        ipw.println();
        ipw.printf("import java.util.function.Consumer;%n");
    }
}
