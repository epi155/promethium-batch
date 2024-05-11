package io.github.epi155.pm.batch.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.PrintWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

class JobCount extends StatsCount implements JobTrace {
    private static final String L_STEP = "Step Name";
    private final ConcurrentLinkedQueue<StepInfo> stepInfos = new ConcurrentLinkedQueue<>();

    public JobCount(String jobName) {
        super(jobName);
    }

    @Override
    protected void recap(PrintWriter pw) {
        int wid = Math.max(L_STEP.length(), 3 + stepInfos.stream().map(StepInfo::getStepName).map(String::length).max(Integer::compareTo).orElse(0));
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
        pw.printf("^------^-------------------------------^-------------------------------^-------------------%n");
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

    Optional<Integer> getReturnCode(String stepName) {
        for (StepInfo info : stepInfos) {
            if (info.stepName.equals(stepName) && info instanceof StepDone) {
                return Optional.of(((StepDone) info).returnCode);
            }
        }
        return Optional.empty();
    }

    @AllArgsConstructor
    @Getter
    abstract static class StepInfo {
        protected final String stepName;
        protected final Instant tmStart;

        protected abstract void info(PrintWriter pw, int width);
    }

    static class StepDone extends StepInfo {
        private final int returnCode;
        private final Instant tmEnd;

        public StepDone(String stepName, int rc, Instant tmStart, Instant tmEnd) {
            super(stepName, tmStart);
            this.returnCode = rc;
            this.tmEnd = tmEnd;
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

    static class StepSkip extends StepInfo {

        public StepSkip(String stepName) {
            super(stepName, Instant.now());
        }

        @Override
        protected void info(PrintWriter pw, int width) {
            pw.print(stepName);
            pw.print(".".repeat(width - stepName.length()));
            pw.printf("! skip ! %-29s !                               !%n",
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            .format(LocalDateTime.ofInstant(tmStart, ZoneOffset.systemDefault())));
        }
    }

    static class StepCmnd extends StepInfo {
        private final int returnCode;

        public StepCmnd(String stepName, int rc) {
            super(stepName, Instant.now());
            this.returnCode = rc;
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
}
