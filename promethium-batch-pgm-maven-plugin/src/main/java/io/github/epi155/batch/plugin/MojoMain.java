package io.github.epi155.batch.plugin;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;

import static java.lang.Math.max;

@Setter
@Mojo(name = "generate",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        requiresDependencyCollection = ResolutionScope.COMPILE
)
@Slf4j
public class MojoMain extends AbstractMojo {
    @Parameter(defaultValue = "${plugin}", readonly = true, required = true)
    protected org.apache.maven.plugin.descriptor.PluginDescriptor plugin;
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/batch",
            property = "maven.pm.batch.generate-directory", required = true)
    private File generateDirectory;
    @Parameter(defaultValue = "io.github.epi155.pm.batch.pgm",
            property = "maven.pm.batch.pgm-package-name", required = true)
    private String pgmPackageName;
    @Parameter(defaultValue = "io.github.epi155.pm.batch.step",
            property = "maven.pm.batch.step-package-name", required = true)
    private String stepPackageName;
    @Parameter(defaultValue = "io.github.epi155.pm.batch.job",
            property = "maven.pm.batch.job-package-name", required = true)
    private String jobPackageName;
    @Parameter(property = "maven.pm.batch.min-out", required = true, defaultValue = "1")
    private int minOut;
    @Parameter(property = "maven.pm.batch.max-out", required = true)
    private int maxOut;
    @Parameter(property = "maven.pm.batch.mu-min-inp", required = true, defaultValue = "2")
    private int muMinInp;
    @Parameter(property = "maven.pm.batch.mu-max-inp", required = true, defaultValue = "3")
    private int muMaxInp;
    @Parameter(property = "maven.pm.batch.mu-min-out", required = true, defaultValue = "0")
    private int muMinOut;
    @Parameter(property = "maven.pm.batch.mu-max-out", required = true, defaultValue = "8")
    private int muMaxOut;
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;
    /**
     * If set to true (default), adds target directory as a compile source root
     * of this Maven project.
     */
    @SuppressWarnings("CanBeFinal")
    @Parameter(defaultValue = "true", property = "maven.pm.batch.add-compile-source-root")
    private boolean addCompileSourceRoot = true;

    /**
     * If set to true, adds target directory as a test compile source root of
     * this Maven project. Default value is false.
     */
    @SuppressWarnings("CanBeFinal")
    @Parameter(defaultValue = "false", property = "maven.pm.batch.add-test-compile-source-root")
    private boolean addTestCompileSourceRoot = false;

    @Override
    public void execute() throws MojoExecutionException {
        MojoContext.context.set(new MojoContext(plugin.getGroupId(), plugin.getArtifactId(), plugin.getVersion()));
        GenerateContext gcx = new GenerateContext(generateDirectory, jobPackageName, stepPackageName, pgmPackageName);
        Range range = new Range(minOut, maxOut);
        Range muInpRange = new Range(max(2,muMinInp), muMaxInp);
        Range muOutRange = new Range(max(0,muMinOut), muMaxOut);
        try {
            /*-------------------------*/
            CodeGenerator.generatePgm(gcx, muInpRange);
            CodeGenerator.generateSingle(gcx, range);
            CodeGenerator.generateMulti(generateDirectory, gcx, muInpRange, muOutRange);
            /*-------------------------*/
            setupMavenPaths(generateDirectory);

            log.info("Done.");
        } catch (Exception e) {
            log.error(e.toString());
            throw new MojoExecutionException("Failed to execute plugin", e);
        } finally {
            MojoContext.context.remove();
        }
    }

    private void setupMavenPaths(File srcMain) {
        if (addCompileSourceRoot) {
            project.addCompileSourceRoot(srcMain.getPath());
        }
        if (addTestCompileSourceRoot) {
            project.addTestCompileSourceRoot(srcMain.getPath());
        }
    }
}
