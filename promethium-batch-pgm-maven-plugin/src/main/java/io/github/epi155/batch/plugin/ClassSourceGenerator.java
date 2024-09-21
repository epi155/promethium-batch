package io.github.epi155.batch.plugin;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public abstract class ClassSourceGenerator {
    protected static final String DOT_JAVA = ".java";
    protected final File baseDir;
    protected final String jobPackageName;
    protected final String stepPackageName;
    protected final String pgmPackageName;

    protected ClassSourceGenerator(File baseDir, GenerateContext gcx) {
        this.baseDir = baseDir;
        this.jobPackageName = gcx.job;
        this.stepPackageName = gcx.step;
        this.pgmPackageName = gcx.pgm;
    }

}
