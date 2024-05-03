package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.step.BatchException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.*;
import java.util.function.*;

@Slf4j
class PmJCL implements JCL {
    private final ErrorCodeFactory factory;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final List<Future<StepStatus>> futures = new LinkedList<>();

    private PmJCL() {
        ServiceLoader<ErrorCodeProvider> loader = ServiceLoader.load(ErrorCodeProvider.class);
        this.factory = loader.findFirst().orElseGet(PmErrorCode::new).getInstance();
    }

    public static PmJCL getInstance() {
        return PmJCL.Helper.INSTANCE;
    }

    @Override
    public int rcOk() {
        return factory.rcOk();
    }

    @Override
    public int rcWarning() {
        return factory.rcWarning();
    }

    @Override
    public int rcErrorStep() {
        return factory.rcErrorStep();
    }

    @Override
    public int rcErrorJob() {
        return factory.rcErrorJob();
    }

    @Override
    public int rcErrorIO() {
        return factory.rcErrorIO();
    }

    @Override
    public int rcErrorSQL() {
        return factory.rcErrorSQL();
    }

    private StepStatus wrapper(StepCount c, IntSupplier submit) {
        Instant tiStart = Instant.now();
        int returnCode = Integer.MAX_VALUE;
        try {
            returnCode = submit.getAsInt();
        } catch (BatchException e) {
            log.error("Execution Error", e);
            returnCode = e.getReturnCode();
        } catch (Exception e) {
            log.error("Unhandled Error", e);
            returnCode = factory.rcErrorStep();
        } finally {
            c.setReturnCode(returnCode);
            c.recap();
            Instant tiEnd = Instant.now();
            Duration lapse = Duration.between(tiStart, tiEnd);
            log.info("Step end: {}", DateTimeFormatter.ISO_LOCAL_TIME.format(lapse.addTo(LocalTime.of(0, 0))));
        }
        return PmStepStatus.of(returnCode);
    }

    public <P, C extends StepCount> StepStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        log.info("Step {} start: {}", c.name(), p);
        return wrapper(c, () -> pgm.apply(p, c));
    }

    public <C extends StepCount> StepStatus execPgm(C c, ToIntFunction<C> pgm) {
        log.info("Step {} start", c.name());
        return wrapper(c, () -> pgm.applyAsInt(c));
    }

    public <P, C extends StepCount> StepStatus execPgm(P p, C c, BiConsumer<P, C> pgm) {
        log.info("Step {} start: {}", c.name(), p);
        return wrapper(c, () -> {
            pgm.accept(p, c);
            return factory.rcOk();
        });
    }

    public <C extends StepCount> StepStatus execPgm(C c, Consumer<C> pgm) {
        log.info("Step {} start", c.name());
        return wrapper(c, () -> {
            pgm.accept(c);
            return factory.rcOk();
        });
    }

    @Override
    public <P, C extends StepCount> StepStatus forkPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        futures.add(executorService.submit(() -> execPgm(p, c, pgm)));
        return StepStatus.OK;
    }

    @Override
    public <C extends StepCount> StepStatus forkPgm(C c, ToIntFunction<C> pgm) {
        futures.add(executorService.submit(() -> execPgm(c, pgm)));
        return StepStatus.OK;
    }

    @Override
    public <P, C extends StepCount> StepStatus forkPgm(P p, C c, BiConsumer<P, C> pgm) {
        futures.add(executorService.submit(() -> execPgm(p, c, pgm)));
        return StepStatus.OK;
    }

    @Override
    public <C extends StepCount> StepStatus forkPgm(C c, Consumer<C> pgm) {
        futures.add(executorService.submit(() -> execPgm(c, pgm)));
        return StepStatus.OK;
    }

    StepStatus join(StepStatus currentStatus) {
        StepStatus result = currentStatus;
        while (!futures.isEmpty()) {
            val it = futures.iterator();
            try {
                while (it.hasNext()) {
                    val ele = it.next();
                    if (ele.isDone()) {
                        result = maxResult(ele, result);
                        it.remove();
                    }
                }
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return result;
    }

    private StepStatus maxResult(Future<StepStatus> ele, StepStatus result) throws InterruptedException {
        try {
            StepStatus status = ele.get();
            if (status.returnCode() > result.returnCode()) {
                return status;
            }
        } catch (ExecutionException e) {
            log.error("Unhandled Error", e);
            return StepStatus.of(factory.rcErrorStep());
        }
        return result;
    }

    private static class Helper {
        private static final PmJCL INSTANCE = new PmJCL();
    }
}
