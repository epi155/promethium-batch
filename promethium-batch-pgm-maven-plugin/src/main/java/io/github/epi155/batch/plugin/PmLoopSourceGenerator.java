package io.github.epi155.batch.plugin;

import java.io.File;
import java.io.FileNotFoundException;

public class PmLoopSourceGenerator extends ClassSourceGeneratorZro {
    public PmLoopSourceGenerator(File baseDir, GenerateContext gcx) {
        super(baseDir, gcx);
    }

    @Override
    protected void createClass(PrintModel ipw) throws FileNotFoundException {
        writeImport(ipw);
        writeDeclareClass(ipw);
        ipw.more();
        writeConstructor(ipw);
        writeMethodImpl(ipw);
        ipw.ends();
    }

    private void writeMethodImpl(PrintModel ipw) {
        ipw.override();
        ipw.println("public <J> LoopSource<J> map(Function<? super I, ? extends J> map) {");
        ipw.more();
        ipw.println("SourceResource<S, J> mapSource = new SourceResource<S,J>() {");
        ipw.more();
        ipw.println("private S s;");
        ipw.println("private Supplier<I> supplier;");
        ipw.println("private Iterator<I> iterator;");
        ipw.println();
        ipw.override();
        ipw.println("public S get() {");
        ipw.more();
        ipw.println("this.s = source.get();");
        ipw.println("this.iterator = source.iterator(s);");
        ipw.println("this.supplier = source.supplier(s);");
        ipw.println("return s;");
        ipw.ends();
        ipw.println();
        ipw.override();
        ipw.println("public Iterator<J> iterator(S s) {");
        ipw.more();
        ipw.println("if (!s.equals(this.s)) throw new IllegalStateException();");
        ipw.println("return new Iterator<J>() {");
        ipw.more();
        ipw.override();
        ipw.println("public boolean hasNext() {");
        ipw.more();
        ipw.println("return iterator.hasNext();");
        ipw.ends();
        ipw.override();
        ipw.println("public J next() {");
        ipw.more();
        ipw.println("return map.apply(iterator.next());");
        ipw.ends();
        ipw.endsc();
        ipw.ends();
        ipw.override();
        ipw.println("public Supplier<J> supplier(S s) {");
        ipw.more();
        ipw.println("if (!s.equals(this.s)) throw new IllegalStateException();");
        ipw.println("return () -> map.apply(supplier.get());");
        ipw.ends();
        ipw.endsc();
        ipw.println("return new PmLoopSource<>(mapSource);");
        ipw.ends();
    }

    private void writeConstructor(PrintModel ipw) {
        ipw.println("PmLoopSource(SourceResource<S, I> source) {");
        ipw.more();
        ipw.println("super(source);");
        ipw.ends();
    }

    private void writeDeclareClass(PrintModel ipw) {
        ipw.println("class PmLoopSource<S extends AutoCloseable, I> extends PmPushSource<S, I> implements LoopSource<I> {");
    }

    private void writeImport(PrintModel ipw) {
        if (! pgmPackageName.equals(stepPackageName)) {
            ipw.printf("import %s.SourceResource;%n", stepPackageName);
            ipw.println();
        }
        ipw.println("import java.util.Iterator;");
        ipw.println("import java.util.function.Function;");
        ipw.println("import java.util.function.Supplier;");
        ipw.println();
    }
}
