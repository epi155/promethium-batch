# batch-gen-code-maven-plugin

In general a batch step has **N** output channels.
If there are **N** output channels, you need **N** dedicated classes or a class with **N** dedicated methods, or dedicated methods with **N**+ε arguments.
These classes are relatively trivial and writing code for them is simple, but it wastes time that could be put to better use.
Time waste is quadratic, *O(N<sup>2</sup>)*, and writing these classes directly becomes unmanageable as **N** grows.

The plugin allows you to generate all these trivial classes automatically, simply by setting the maximum number of output channels.

## plugin usage

~~~yml
    <plugin>
        <groupId>io.github.epi155</groupId>
        <artifactId>batch-gen-code-maven-plugin</artifactId>
        <version> ... </version>
        <executions>
            <execution>
                <goals>
                    <goal>generate</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <maxOut>8</maxOut>
        </configuration>
    </plugin>
~~~

The only parameter you need to set is `maxOut` (maximum number of output channels).

Since the maximum number of arguments of a static method is 255, the maximum value that can be used for `maxOut` is **253** (`this, input, out1, ..., out253`).
The size of the jar, and the compilation time, grows quadratically with the maximum number of output channels, `maxOut`.

For completeness we show all the parameters that can be set of the plugin:

~~~java
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/batch",
            property = "maven.pm.batch.generate-directory", required = true)
    private File generateDirectory;
    @Parameter(defaultValue = "io.github.epi155.pm.batch.step",
            property = "maven.pm.batch.package-name", required = true)
    private String packageName;
    @Parameter(property = "maven.pm.batch.max-out", required = true)
    private int maxOut;
    @Parameter(defaultValue = "true", property = "maven.pm.batch.add-compile-source-root")
    private boolean addCompileSourceRoot;
    @Parameter(defaultValue = "false", property = "maven.pm.batch.add-test-compile-source-root")
    private boolean addTestCompileSourceRoot;
~~~