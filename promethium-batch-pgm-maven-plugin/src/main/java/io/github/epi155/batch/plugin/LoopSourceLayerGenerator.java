package io.github.epi155.batch.plugin;

import java.io.File;
import java.io.FileNotFoundException;

public class LoopSourceLayerGenerator extends ClassSourceGeneratorZro {
    public LoopSourceLayerGenerator(File baseDir, GenerateContext gcx) {
        super(baseDir, gcx);
    }

    @Override
    protected void createClass(PrintModel ipw) throws FileNotFoundException {
        writeDocInterface(ipw);
        writeDeclareInterface(ipw);
        ipw.more();
        writeMethods(ipw);
        ipw.ends();
    }

    private void writeMethods(PrintModel ipw) {
        ipw.javadocOpen();
        ipw.println(" * performs the indicated action after reading each element read before main processing");
        ipw.println(" *");
        ipw.println(" * @param action action to perform");
        ipw.println(" * @return new instance of {@link LoopSourceStd}");
        ipw.javadocClose();
        ipw.println("LoopSourceStd<I> before(Runnable action);");
    }

    private void writeDeclareInterface(PrintModel ipw) {
        ipw.println("public interface LoopSourceLayer<I> extends LoopSourceStd<I> {");
    }

    private void writeDocInterface(PrintModel ipw) {
        ipw.javadocOpen();
        ipw.println(" * interface to manage the elements of a single input resource.");
        ipw.println(" * <p>");
        ipw.println(" * it is possible to process the input immediately or to define one or more");
        ipw.println(" * output resources where the input processing can be sent");
        ipw.println(" *");
        ipw.println(" * @param <I> input type");
        ipw.javadocClose();
    }
}
