package io.github.epi155.batch.plugin;

import lombok.AllArgsConstructor;

import java.io.File;

@AllArgsConstructor
public class GenerateContext {
    public final File srcDir;
    public final String job;
    public final String step;
    public final String pgm;
}
