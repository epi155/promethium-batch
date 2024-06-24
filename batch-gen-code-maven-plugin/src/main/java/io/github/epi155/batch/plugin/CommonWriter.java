package io.github.epi155.batch.plugin;

import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CommonWriter {
    private CommonWriter() {
    }

    public static void writePackage(PrintWriter pw, String packageName) {
        writeCopyright(pw);
        pw.printf("package %s;%n%n", packageName);
    }

    private static void writeCopyright(PrintWriter pw) {
        String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        MojoContext mc = MojoContext.context.get();
        pw.println("/*");
        pw.printf(" * Code Generated at %s%n", now);
        pw.printf(" * Plugin: %s:%s:%s%n", mc.getGroup(), mc.getArtifact(), mc.getVersion());
        pw.println(" */");
    }
}
