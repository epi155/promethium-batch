package io.github.epi155.batch.plugin;

import java.io.PrintWriter;
import java.io.Writer;
import java.nio.CharBuffer;

public class IndentPrintWriter implements PrintModel {
    private final PrintWriter pw;
    private final String tab;
    private int indent = 0;

    public IndentPrintWriter(int step, Writer writer) {
        this.tab = CharBuffer.allocate(step).toString().replace('\u0000', ' ');
        this.pw = new PrintWriter(writer);
    }

    @Override
    public void printf(String format, Object... objects) {
        indent();
        pw.printf(format, objects);
    }

    @Override
    public void putf(String format, Object... objects) {
        pw.printf(format, objects);
    }

    private void indent() {
        for (int k = 0; k < indent; k++) {
            pw.print(tab);
        }
    }

    @Override
    public void more() {
        indent++;
    }

    @Override
    public void println() {
        pw.println();
    }

    @Override
    public void less() {
        if (indent > 0) indent--;
    }

    @Override
    public void ends() {
        if (indent > 0) {
            indent--;
            indent();
            pw.println("}");
        }
    }

    @Override
    public void javadocOpen() {
        printf("/**%n");
    }

    @Override
    public void javadocClose() {
        printf(" */%n");
    }

    @Override
    public void override() {
        printf("@Override%n");
    }

}
