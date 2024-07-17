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
    @Parameter(defaultValue = "io.github.epi155.pm.batch.step",
            property = "maven.pm.batch.package-name", required = true)
    private String packageName;
    @Parameter(property = "maven.pm.batch.max-out", required = true)
    private int maxOut;
    @Parameter(property = "maven.pm.batch.mu-max-inp", required = true, defaultValue = "3")
    private int muMaxInp;
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
        try {
            /*-------------------------*/
            CodeGenerator.generatePgm(generateDirectory, packageName, muMaxInp);
            CodeGenerator.generateSingle(generateDirectory, packageName, maxOut);
            CodeGenerator.generateMulti(generateDirectory, packageName, muMaxInp, muMaxOut);
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
