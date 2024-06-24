package io.github.epi155.batch.plugin;

import lombok.Data;

@Data
public class MojoContext {
    public static final ThreadLocal<MojoContext> context = new ThreadLocal<>();
    private final String group;
    private final String artifact;
    private final String version;
}
