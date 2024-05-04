package io.github.epi155.pm.batch.job;

import io.github.epi155.pm.batch.step.BatchException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

@Slf4j
class PmJCL implements JCL {
    private final ErrorCodeFactory factory;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final List<Future<JobStatus>> futures = new LinkedList<>();
    private final Deque<Integer> stack = new LinkedList<>();

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

    private JobStatus wrapper(StepCount c, IntSupplier submit) {
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
            log.info("Step {} end: {}", c.name(), DateTimeFormatter.ISO_LOCAL_TIME.format(lapse.addTo(LocalTime.of(0, 0))));
        }
        return PmJobStatus.of(returnCode,factory);
    }

    public <P, C extends StepCount> JobStatus execPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        log.info("C.Step {} start: {}", c.name(), p);
        return wrapper(c, () -> pgm.apply(p, c));
    }

    public <C extends StepCount> JobStatus execPgm(C c, ToIntFunction<C> pgm) {
        log.info("C.Step {} start", c.name());
        return wrapper(c, () -> pgm.applyAsInt(c));
    }

    public <P, C extends StepCount> JobStatus execPgm(P p, C c, BiConsumer<P, C> pgm) {
        log.info("A.Step {} start: {}", c.name(), p);
        return wrapper(c, () -> {
            pgm.accept(p, c);
            return factory.rcOk();
        });
    }

    public <C extends StepCount> JobStatus execPgm(C c, Consumer<C> pgm) {
        log.info("A:Step {} start", c.name());
        return wrapper(c, () -> {
            pgm.accept(c);
            return factory.rcOk();
        });
    }

    @Override
    public <P, C extends StepCount> JobStatus forkPgm(P p, C c, BiFunction<P, C, Integer> pgm) {
        return submit(() -> execPgm(p, c, pgm));
    }

    private JobStatus submit(Callable<JobStatus> step) {
        futures.add(executorService.submit(step));
        return PmJobStatus.of(factory.rcOk(), factory);
    }

    @Override
    public <C extends StepCount> JobStatus forkPgm(C c, ToIntFunction<C> pgm) {
        return submit(() -> execPgm(c, pgm));
    }

    @Override
    public <P, C extends StepCount> JobStatus forkPgm(P p, C c, BiConsumer<P, C> pgm) {
        return submit(() -> execPgm(p, c, pgm));
    }

    @Override
    public <C extends StepCount> JobStatus forkPgm(C c, Consumer<C> pgm) {
        return submit(() -> execPgm(c, pgm));
    }

    JobStatus join(JobStatus currentStatus) {
        JobStatus result = currentStatus;
        int k=0;
        while (!futures.isEmpty()) {
            val it = futures.iterator();
            int nmAlive = 0;
            try {
                while (it.hasNext()) {
                    val ele = it.next();
                    if (ele.isDone()) {
                        result = maxResult(ele, result);
                        it.remove();
                    } else {
                        nmAlive++;
                    }
                }
                if (nmAlive > 0) {
                    sendMessage(++k, nmAlive);
                    TimeUnit.MILLISECONDS.sleep(40);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return result;
    }
    private void sendMessage(int j, int r) {
        if ((j & 0x03ff) != 0) {
            int n = 0;
            while (j != 0) {
                if ((j & 0x01) == 1) n++;
                j >>= 1;
            }
            if (n != 1) return;
        }
        log.info("Waiting {} running step", r);
    }


    private JobStatus maxResult(Future<JobStatus> ele, JobStatus result) throws InterruptedException {
        try {
            JobStatus status = ele.get();
            if (status.returnCode() > result.returnCode()) {
                return status;
            }
        } catch (ExecutionException e) {
            log.error("Unhandled Error", e);
            return PmJobStatus.of(factory.rcErrorStep(),factory);
        }
        return result;
    }

    public void push(int returnCode) {
        stack.push(returnCode);
    }

    public Optional<Integer> pop() {
        if (stack.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(stack.pop());
        }
    }

    private static class Helper {
        private static final PmJCL INSTANCE = new PmJCL();
    }
}
