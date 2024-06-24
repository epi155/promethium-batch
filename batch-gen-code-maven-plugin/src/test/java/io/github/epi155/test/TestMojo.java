package io.github.epi155.test;

import io.github.epi155.batch.plugin.MojoMain;
import lombok.SneakyThrows;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class TestMojo {
    @Test
    void generateTest() {
        MojoMain mojo = new MojoMain();
        mojo.setGenerateDirectory(new File("target/generated-test-sources/batch"));
        mojo.setPlugin(new PluginDescriptor() {
            public String getGroupId() {
                return "io.github.epi155";
            }

            public String getArtifactId() {
                return "batch-maven-plugin";
            }

            public String getVersion() {
                return "TEST";
            }
        });
        mojo.setPackageName("io.github.epi155.pm.batch.step");
        mojo.setMaxOut(8);

        File pomFile = new File("pom.xml");
        MavenProject project = getProject(pomFile.toPath());
        mojo.setProject(project);

        mojo.setAddCompileSourceRoot(false);
        mojo.setAddTestCompileSourceRoot(false);

        Assertions.assertDoesNotThrow(mojo::execute);
    }

    @SneakyThrows
    MavenProject getProject(Path pomPath) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try (InputStream is = Files.newInputStream(pomPath)) {
            Model model = reader.read(is);
            return new MavenProject(model);
        }
    }

}
