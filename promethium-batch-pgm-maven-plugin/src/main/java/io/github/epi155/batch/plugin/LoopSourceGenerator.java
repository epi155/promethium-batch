package io.github.epi155.batch.plugin;

import java.io.File;
import java.io.FileNotFoundException;

public class LoopSourceGenerator extends ClassSourceGeneratorZro {
    public LoopSourceGenerator(File baseDir, GenerateContext gcx) {
        super(baseDir, gcx);
    }

    @Override
    protected void createClass(PrintModel ipw) throws FileNotFoundException {
        writeImport(ipw);
        writeDocInterface(ipw);
        writeDeclareInterface(ipw);
        ipw.more();
        writeMethods(ipw);
        ipw.ends();
    }

    private void writeImport(PrintModel ipw) {
        ipw.println("import java.util.function.Function;");
        ipw.println("import java.util.function.Predicate;");
        ipw.println();
    }

    private void writeMethods(PrintModel ipw) {
        ipw.javadocOpen();
        ipw.println(" * Sets a condition to interrupt the processing cycle");
        ipw.println(" * <p>even if the value being processed is passed as an argument,");
        ipw.println(" * the condition may depend on an external parameter,");
        ipw.println(" * for example the reaching of a maximum execution time");
        ipw.println(" *");
        ipw.println(" * @param test test condition");
        ipw.println(" * @return {@code true} to stop the processing loop, {@code false} to continue as usual");
        ipw.javadocClose();
        ipw.println("LoopSourceLayer<I> terminate(Predicate<? super I> test);");

        ipw.println();

        ipw.javadocOpen();
        ipw.println(" * transforms the item read from the source before offering it to the main processing loop.");
        ipw.println(" *");
        ipw.println(" * @param map transformation function");
        ipw.println(" * @param <J> new item type");
        ipw.println(" * @return new instance of {@link LoopSource}");
        ipw.javadocClose();
        ipw.println("<J> LoopSource<J> map(Function<? super I, ? extends J> map);");
    }

    private void writeDeclareInterface(PrintModel ipw) {
        ipw.println("public interface LoopSource<I> extends LoopSourceLayer<I> {");
    }

    private void writeDocInterface(PrintModel ipw) {
        ipw.javadocOpen();
        ipw.println(" * terminable interface to manage the elements of a single input resource.");
        ipw.println(" * <p>");
        ipw.println(" * it is possible to process the input immediately or to define one or more");
        ipw.println(" * output resources where the input processing can be sent");
        ipw.println(" *");
        ipw.println(" * @param <I> input type");
        ipw.javadocClose();
    }
}
