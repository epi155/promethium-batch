package io.github.epi155.pm.batch.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.io.PrintWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class JobCount extends StatsCount implements JobTrace {
    private static final String JAVA_BASE = "java.base";
    private static final String L_STEP = "Name";
    private final ConcurrentLinkedQueue<StepInfo> stepInfos = new ConcurrentLinkedQueue<>();
    private final Instant tiStart;
    private int maxcc;

    public JobCount(String jobName) {
        super(jobName);
        this.tiStart = Instant.now();
    }

    @Override
    protected void recap(PrintWriter pw) {
        Instant tiStop = Instant.now();
        Duration lapse = Duration.between(tiStart, tiStop);
        int lenStep = stepInfos.stream().map(StepInfo::getStepName).map(String::length).max(Integer::compareTo).orElse(0);

        int wid = IntStream.of(L_STEP.length(), 3 + lenStep, 3 + name().length()).max().orElse(0);
        int lpad = (wid - L_STEP.length()) / 2;
        int rpad = wid - L_STEP.length() - lpad;

        pw.print(" ".repeat(lpad));
        pw.print(L_STEP);
        pw.print(" ".repeat(rpad));
        pw.printf("!  rc  !     Date-Time Start/Skip      !         Date-Time End         !       Lapse       %n");

        pw.print("-".repeat(wid));
        pw.printf("+------+-------------------------------+-------------------------------+-------------------%n");

        stepInfos.stream().sorted(Comparator.comparing(a -> a.tmStart)).forEach(it -> it.info(pw, wid));
        pw.print("-".repeat(wid));
        pw.printf("+------+-------------------------------+-------------------------------+-------------------%n");
        pw.print(name());
        pw.print(".".repeat(wid - name().length()));
        pw.printf("! %4d ! %-29s ! %-29s ! %s", maxcc,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        .format(LocalDateTime.ofInstant(tiStart, ZoneId.systemDefault())),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        .format(LocalDateTime.ofInstant(tiStop, ZoneId.systemDefault())),
                DateTimeFormatter.ISO_LOCAL_TIME
                        .format(lapse.addTo(LocalTime.of(0, 0)))
        );
        List<StepFail> errors = stepInfos.stream().filter(StepFail.class::isInstance).map(it -> (StepFail) it).collect(Collectors.toList());
        if (! errors.isEmpty()) {
            pw.println();
            pw.print("=".repeat(wid));
            pw.printf("+======^===============================^===============================^===================%n");
            errors.forEach(it -> {
                pw.print(it.stepName);
                pw.print(".".repeat(wid - it.stepName.length()));
                pw.printf("! %s", cause(it));
            });
            pw.println();
            pw.print("=".repeat(wid));
            pw.print("^==========================================================================================");
        } else {
            pw.println();
            pw.print("-".repeat(wid));
            pw.print("^------^-------------------------------^-------------------------------^-------------------");
        }
    }

    private String cause(StepFail fail) {
        Throwable fault = fail.error;
        for(;;) {
            Throwable cause = fault.getCause();
            if (cause==null)
                break;
            fault = cause;
        }
        val stes = fault.getStackTrace();
        val matcher = JobContext.matcher.get();
        for (StackTraceElement ste : stes) {
            String module = ste.getModuleName();
            if (!JAVA_BASE.equals(module) && !ste.isNativeMethod() &&
                    (matcher==null || matcher.match(ste.getClassName()))) {
                String claz = ste.getClassName();
                String meth = ste.getMethodName();
                String file = ste.getFileName();
                int line = ste.getLineNumber();
                return String.format("%s @%s->%s(%s:%d) [%s]", fault, claz, meth, file, line, JobContext.MatchByLib.libOf(claz));
            }
        }
        return fault.toString();
    }

    public void add(String name, int returnCode, Instant tiStart, Instant tiEnd, Throwable error) {
        stepInfos.add(new StepFail(name, returnCode, tiStart, tiEnd, error));
    }
    public void add(String name, int returnCode, Instant tiStart, Instant tiEnd) {
        stepInfos.add(new StepDone(name, returnCode, tiStart, tiEnd));
    }

    public void add(String name, int returnCode) {
        stepInfos.add(new StepCmnd(name, returnCode));
    }

    public void add(String name) {
        stepInfos.add(new StepSkip(name));
    }

    @Override
    public String fullName(String name) {
        return name;
    }

    @Override
    public String getPrefix() {
        return "";
    }

    Optional<Integer> getReturnCode(String stepName) {
        for (StepInfo info : stepInfos) {
            if (info.stepName.equals(stepName) && info instanceof StepDone) {
                return Optional.of(((StepDone) info).returnCode);
            }
        }
        return Optional.empty();
    }

    public void maxcc(int maxcc) {
        this.maxcc = maxcc;
    }

    protected List<StepError> stepErrors() {
        return stepInfos.stream()
                .filter(StepFail.class::isInstance)
                .map(it -> (StepFail) it)
                .map(PmStepError::new)
                .collect(Collectors.toList());
    }

    @AllArgsConstructor
    @Getter
    abstract static class StepInfo {
        protected final String stepName;
        protected final Instant tmStart;
        protected abstract Instant tmSort();

        protected abstract void info(PrintWriter pw, int width);
    }
    static class StepFail extends StepDone {
        private final Throwable error;

        public StepFail(String stepName, int rc, Instant tmStart, Instant tmEnd, Throwable error) {
            super(stepName, rc, tmStart, tmEnd);
            this.error = error;
        }
    }

    @ToString
    static class StepDone extends StepInfo {
        protected final int returnCode;
        private final Instant tmEnd;

        public StepDone(String stepName, int rc, Instant tmStart, Instant tmEnd) {
            super(stepName, tmStart);
            this.returnCode = rc;
            this.tmEnd = tmEnd;
        }

        @Override
        protected Instant tmSort() {
            return tmStart; // or tmEnd ?
        }

        @Override
        protected void info(PrintWriter pw, int width) {
            Duration lapse = Duration.between(tmStart, tmEnd);
            pw.print(stepName);
            pw.print(".".repeat(width - stepName.length()));
            pw.printf("! %4d ! %-29s ! %-29s ! %s%n", returnCode,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            .format(LocalDateTime.ofInstant(tmStart, ZoneId.systemDefault())),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            .format(LocalDateTime.ofInstant(tmEnd, ZoneId.systemDefault())),
                    DateTimeFormatter.ISO_LOCAL_TIME
                            .format(lapse.addTo(LocalTime.of(0, 0)))
            );
        }
    }

    @ToString
    static class StepSkip extends StepInfo {

        public StepSkip(String stepName) {
            super(stepName, Instant.now());
        }

        @Override
        protected Instant tmSort() {
            return tmStart;
        }

        @Override
        protected void info(PrintWriter pw, int width) {
            pw.print(stepName);
            pw.print(".".repeat(width - stepName.length()));
            pw.printf("! skip ! %-29s !                               !%n",
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            .format(LocalDateTime.ofInstant(tmStart, ZoneId.systemDefault())));
        }
    }

    @ToString
    static class StepCmnd extends StepInfo {
        private final int returnCode;

        public StepCmnd(String stepName, int rc) {
            super(stepName, Instant.now());
            this.returnCode = rc;
        }

        @Override
        protected Instant tmSort() {
            return tmStart;
        }

        @Override
        protected void info(PrintWriter pw, int width) {
            pw.print(stepName);
            pw.print(".".repeat(width - stepName.length()));
            pw.printf("! %4d ! %-29s !                               !%n", returnCode,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            .format(LocalDateTime.ofInstant(tmStart, ZoneId.systemDefault()))
            );
        }
    }

    @Getter
    @ToString
    private static class PmStepError implements StepError {
        private final String name;
        private final int returnCode;
        private final Throwable error;
        public PmStepError(StepFail it) {
            this.name = it.stepName;
            this.returnCode = it.returnCode;
            this.error = it.error;
        }
    }
}
