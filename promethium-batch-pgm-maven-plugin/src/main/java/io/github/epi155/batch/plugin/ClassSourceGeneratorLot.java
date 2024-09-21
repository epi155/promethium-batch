package io.github.epi155.batch.plugin;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static io.github.epi155.batch.plugin.CommonWriter.writePackage;

@Slf4j
public abstract class ClassSourceGeneratorLot extends ClassSourceGenerator {

    protected ClassSourceGeneratorLot(File baseDir, GenerateContext gcx) {
        super(baseDir, gcx);
    }

    public void generate(String className, Range range) throws FileNotFoundException {
        File clsFile = new File(baseDir, className + DOT_JAVA);
        try (PrintWriter pw = new PrintWriter(clsFile)) {
            writePackage(pw, pgmPackageName);
            StringWriter swCls = new StringWriter();
            IndentPrintWriter ipw = new IndentPrintWriter(4, swCls);

            createClass(ipw, range);
            pw.print(swCls);
        }
        log.info("{} Created.", className);
    }

    protected abstract void createClass(PrintModel ipw, Range range) throws FileNotFoundException;
}
