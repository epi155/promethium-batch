package io.github.epi155.pm.batch.job;

import lombok.AllArgsConstructor;
import lombok.val;

import java.io.PrintWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class JobCount extends StatsCount {
    private final List<StepInfo> stepInfos = new ArrayList<>();

    public JobCount(String jobName) {
        super(jobName);
    }

    @Override
    protected void recap(PrintWriter pw) {
        stepInfos.sort(Comparator.comparing(a -> a.tmStart));
        pw.printf("      Step Name      ! rc !         ts start/skip         !            ts end             !       lapse       %n");
        pw.printf("---------------------+----+-------------------------------+-------------------------------+-------------------%n");
        stepInfos.forEach(it -> it.info(pw));
        pw.printf("---------------------^----^-------------------------------^-------------------------------^-------------------%n");
    }

    void add(String name, int returnCode, Instant tiStart, Instant tiEnd) {
        stepInfos.add(new StepDone(name, returnCode, tiStart, tiEnd));
    }

    void add(String name) {
        stepInfos.add(new StepSkip(name));
    }

    Optional<Integer> getReturnCode(String stepName) {
        for (val info : stepInfos) {
            if (info.stepName.equals(stepName) && info instanceof StepDone) {
                return Optional.of(((StepDone) info).returnCode);
            }
        }
        return Optional.empty();
    }

    @AllArgsConstructor
    static abstract
    class StepInfo {
        protected final String stepName;
        protected final Instant tmStart;

        protected abstract void info(PrintWriter pw);
    }

    static
    class StepDone extends StepInfo {
        private final int returnCode;
        private final Instant tmEnd;

        public StepDone(String stepName, int rc, Instant tmStart, Instant tmEnd) {
            super(stepName, tmStart);
            this.returnCode = rc;
            this.tmEnd = tmEnd;
        }

        @Override
        protected void info(PrintWriter pw) {
            Duration lapse = Duration.between(tmStart, tmEnd);

            pw.printf("%-20s ! %2d ! %-29s ! %-29s ! %s%n", stepName, returnCode,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            .format(LocalDateTime.ofInstant(tmStart, ZoneOffset.systemDefault())),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            .format(LocalDateTime.ofInstant(tmEnd, ZoneOffset.systemDefault())),
                    DateTimeFormatter.ISO_LOCAL_TIME
                            .format(lapse.addTo(LocalTime.of(0, 0)))
            );
        }
    }

    static
    class StepSkip extends StepInfo {

        public StepSkip(String stepName) {
            super(stepName, Instant.now());
        }

        @Override
        protected void info(PrintWriter pw) {
            pw.printf("%-20s !skip! %-29s !                               !%n", stepName,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            .format(LocalDateTime.ofInstant(tmStart, ZoneOffset.systemDefault())));
        }
    }
}
