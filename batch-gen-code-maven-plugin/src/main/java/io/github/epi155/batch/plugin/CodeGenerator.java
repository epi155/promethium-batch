package io.github.epi155.batch.plugin;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.maven.plugin.MojoExecutionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

@Slf4j
public class CodeGenerator {
    private CodeGenerator() {
    }

    public static void generateSingle(File generateDirectory, String jobPackageName, String stepPackageName, int maxOut) throws MojoExecutionException, FileNotFoundException {
        File baseDir = makeDirectory(generateDirectory, stepPackageName);

        new PushSourceGenerator(baseDir, jobPackageName, stepPackageName).generate("PmPushSource", maxOut);
        new LoopSourceStdGenerator(baseDir, stepPackageName).generate("LoopSourceStd", maxOut);
    }

    public static @NotNull File makeDirectory(@NotNull File base, @Nullable String packg) throws MojoExecutionException {
        if (!base.exists()) {
            log.debug("Source Directory <{}> does not exist, creating", base.getAbsolutePath());
            if (!base.mkdirs())
                throw new MojoExecutionException("Error creating Source Directory <" + base.getName() + ">");
        }
        if (!base.isDirectory())
            throw new MojoExecutionException("Source Directory <" + base.getName() + "> is not a Directory");
        if (packg == null) return base;
        StringTokenizer st = new StringTokenizer(packg, ".");
        String cwd = base.getAbsolutePath();
        while (st.hasMoreElements()) {
            val d = st.nextElement();
            val tmp = cwd + File.separator + d;
            mkdir(tmp);
            cwd = tmp;
        }
        return new File(cwd);
    }

    private static void mkdir(String tmp) throws MojoExecutionException {
        val f = new File(tmp);
        if (!f.exists() && !f.mkdir())
            throw new MojoExecutionException("Cannot create directory <" + tmp + ">");
    }

    public static void generateMulti(File generateDirectory, String jobPackageName, String stepPackageName, int maxInp, int maxOut) throws MojoExecutionException, FileNotFoundException {
        File baseDir = makeDirectory(generateDirectory, stepPackageName);

        for(int k=2; k<=maxInp; k++) {
            new PullSourceGenerator(baseDir, stepPackageName, k).generate("PullSource"+k, maxOut);
            new PmPullSourceGenerator(baseDir, jobPackageName, stepPackageName, k).generate("PmPullSource"+k, maxOut);
        }
    }

    public static void generatePgm(File generateDirectory, String packageName, int maxInp) throws MojoExecutionException, FileNotFoundException {
        File baseDir = makeDirectory(generateDirectory, packageName);
        new PgmGenerator(baseDir, packageName, maxInp).generate("Pgm", 0);
    }
}
