package io.github.epi155.batch.plugin;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static io.github.epi155.batch.plugin.CommonWriter.writePackage;

@Slf4j
public abstract class ClassSourceGenerator {
    private static final String DOT_JAVA = ".java";
    protected final File baseDir;
    protected final String packageName;

    protected ClassSourceGenerator(File baseDir, String packageName) {
        this.baseDir = baseDir;
        this.packageName = packageName;
    }

    public void generate(String className, int maxOut) throws FileNotFoundException {
        File clsFile = new File(baseDir, className + DOT_JAVA);
        try (PrintWriter pw = new PrintWriter(clsFile)) {
            writePackage(pw, packageName);
            StringWriter swCls = new StringWriter();
            IndentPrintWriter ipw = new IndentPrintWriter(4, swCls);

            createClass(ipw, maxOut);
            pw.print(swCls);
        }
        log.info("{} Created.", className);
    }

    protected abstract void createClass(PrintModel ipw, int max) throws FileNotFoundException;
}
