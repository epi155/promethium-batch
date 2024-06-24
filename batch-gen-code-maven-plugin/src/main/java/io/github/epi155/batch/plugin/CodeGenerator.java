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

    public static void generate(File generateDirectory, String packageName, int maxOut) throws MojoExecutionException, FileNotFoundException {
        File baseDir = makeDirectory(generateDirectory, packageName);

        new PushSourceGenerator(baseDir, packageName).generate("PmPushSource", maxOut);
        new LoopSourceStdGenerator(baseDir, packageName).generate("LoopSourceStd", maxOut);

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

}
