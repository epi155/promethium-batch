package io.github.epi155.batch.plugin;

import java.io.File;

public class PmIterableLoopGenerator extends ClassSourceGenerator {
    public PmIterableLoopGenerator(File baseDir, String packageName) {
        super(baseDir, packageName);
    }

    @Override
    protected void createClass(PrintModel ipw, int k) {
        writeImport(ipw);
        ipw.println();
        writeDeclareClass(ipw, k);
        ipw.more();
        writeFields(ipw, k);
        ipw.println();
        writeConstructor(ipw, k);
        ipw.println();
        writeSeqTuple(ipw, k);
        ipw.println();
        writeSeqWorker(ipw, k);
        ipw.println();
        writeShutdownTmo(ipw, k);
        ipw.println();
        writeParallelTupleFair(ipw, k);
        ipw.println();
        writeParallelTupleRaw(ipw, k);
        ipw.println();
        writeParallelWorker(ipw, k);
        ipw.println();
        writeAsyncTuple(ipw, k);
        writeAsyncWorker(ipw, k);
        ipw.ends();

    }

    private void writeAsyncWorker(PrintModel ipw, int k) {
        ipw.override();
        ipw.printf("public void forEachAsync(int maxThread, AsyncWorker%d<? super I,%n", k);
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("Consumer<? super O%d>", i);
            ipw.putf(i < k ? ",%n" : "> asyncWorker) {%n");
        }
        ipw.less();
        ipw.printf("par.new DoAsyncWrite(maxThread) {%n");
        ipw.more();
        ipw.override();
        ipw.printf("protected void openResources() throws Exception {%n");
        ipw.more();
        ipw.printf("try (%n");
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("T%1$d t%d = sink%1$d.get(); PmQueueWriter<O%1$d> w%1$d = openQueue(sink%1$d, t%1$d);%n", i);
        }
        ipw.printf("S s = par.source.get()) {%n");
        ipw.less();
        ipw.printf("doWork(List.of(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("w%d.getFuture()", i);
            ipw.putf(i < k ? ", " : "),%n");
        }
        ipw.more();
        ipw.more();
        ipw.printf("par.source.iterator(s),%n");
        ipw.printf("i -> asyncWorker.apply(i, ");
        for (int i = 1; i <= k; i++) {
            ipw.putf("w%d::write", i);
            ipw.putf(i < k ? "," : "));%n");
        }
        ipw.less();
        ipw.less();
        ipw.ends();
        ipw.ends();
        ipw.less();
        ipw.printf("}.start();%n");
        ipw.ends();
    }

    private void writeAsyncTuple(PrintModel ipw, int k) {
        ipw.override();
        ipw.printf("public void forEachAsync(int maxThread, Function<%n", k);
        ipw.more();
        ipw.more();
        ipw.printf("? super I,%n");
        if (k == 1) {
            ipw.printf("? extends Future<? extends O%d>> asyncTransformer) {%n", k);
        } else {
            ipw.printf("? extends Future<? extends Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">>> asyncTransformer) {%n");
            }
            ipw.less();
            ipw.less();
        }
        ipw.less();
        if (k == 1) {
            ipw.printf("par.new DoAsyncParallelFair<O%d>(maxThread, asyncTransformer) {%n", k);
            ipw.more();
        } else {
            ipw.printf("par.new DoAsyncParallelFair<Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">>(maxThread, asyncTransformer) {%n");
            }
            ipw.less();
        }
        ipw.override();
        ipw.printf("protected void openResources() throws Exception {%n");
        ipw.more();
        ipw.printf("try (%n");
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("T%1$d t%1$d = sink%1$d.get();%n", i);
        }
        ipw.printf("S s = par.source.get()) {%n");
        ipw.less();
        if (k == 1) {
            ipw.printf("doWork(par.source.iterator(s), oo -> sink%1$d.accept(t%1$d, oo));%n", k);
        } else {
            ipw.printf("doWork(par.source.iterator(s), oo -> {%n");
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("oo.onT%1$d(v -> sink%1$d.accept(t%1$d, v));%n", i);
            }
            ipw.less();
            ipw.printf("});%n");
        }
        ipw.ends();
        ipw.ends();
        ipw.less();
        ipw.printf("}.start();%n");
        ipw.ends();
    }

    private void writeParallelWorker(PrintModel ipw, int k) {
        ipw.override();
        ipw.printf("public void forEachParallel(int maxThread, Worker%d<? super I,%n", k);
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("Consumer<? super O%d>", i);
            ipw.putf(i < k ? ",%n" : "> worker) {%n");
        }
        ipw.less();
        ipw.printf("par.new DoParallelWrite(maxThread) {%n");
        ipw.more();
        ipw.override();
        ipw.printf("protected void openResources() throws Exception {%n");
        ipw.more();
        ipw.printf("try (%n");
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("T%1$d t%d = sink%1$d.get(); PmQueueWriter<O%1$d> w%1$d = openQueue(sink%1$d, t%1$d);%n", i);
        }
        ipw.printf("S s = par.source.get()) {%n");
        ipw.less();
        ipw.printf("doWork(List.of(");
        for (int i = 1; i <= k; i++) {
            ipw.putf("w%d.getFuture()", i);
            ipw.putf(i < k ? ", " : "),%n");
        }
        ipw.more();
        ipw.more();
        ipw.printf("par.source.iterator(s),%n");
        ipw.printf("i -> worker.process(i, ");
        for (int i = 1; i <= k; i++) {
            ipw.putf("w%d::write", i);
            ipw.putf(i < k ? "," : "));%n");
        }
        ipw.less();
        ipw.less();
        ipw.ends();
        ipw.ends();
        ipw.less();
        ipw.printf("}.start();%n");
        ipw.ends();
    }

    private void writeParallelTupleRaw(PrintModel ipw, int k) {
        ipw.override();
        ipw.printf("public void forEachParallel(int maxThread, Function<%n", k);
        ipw.more();
        ipw.more();
        ipw.printf("? super I,%n");
        if (k == 1) {
            ipw.printf("? extends O%d> transformer) {%n", k);
        } else {
            ipw.printf("? extends Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">> transformer) {%n");
            }
            ipw.less();
            ipw.less();
        }
        ipw.less();
        if (k == 1) {
            ipw.printf("par.new DoParallelRaw<O%d>(maxThread, transformer) {%n", k);
            ipw.more();

        } else {
            ipw.printf("par.new DoParallelRaw<Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">>(maxThread, transformer) {%n");
            }
            ipw.less();
        }
        ipw.override();
        ipw.printf("protected void openResources() throws Exception {%n");
        ipw.more();
        ipw.printf("try (%n");
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("T%1$d t%1$d = sink%1$d.get();%n", i);
        }
        ipw.printf("S s = par.source.get()) {%n");
        ipw.less();
        if (k == 1) {
            ipw.printf("doWork(par.source.iterator(s), oo -> sink%1$d.accept(t%1$d, oo));", k);
        } else {
            ipw.printf("doWork(par.source.iterator(s), oo -> {%n");
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("oo.onT%1$d(v -> sink%1$d.accept(t%1$d, v));%n", i);
            }
            ipw.less();
            ipw.printf("});%n");
        }
        ipw.ends();
        ipw.ends();
        ipw.less();
        ipw.printf("}.start();%n");
        ipw.ends();
    }

    private void writeParallelTupleFair(PrintModel ipw, int k) {
        ipw.override();
        ipw.printf("public void forEachParallelFair(int maxThread, Function<%n", k);
        ipw.more();
        ipw.more();
        ipw.printf("? super I,%n");
        if (k == 1) {
            ipw.printf("? extends O%d> transformer) {%n", k);
        } else {
            ipw.printf("? extends Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">> transformer) {%n");
            }
            ipw.less();
            ipw.less();
        }
        ipw.less();
        if (k == 1) {
            ipw.printf("par.new DoParallelFair<O%d>(maxThread, transformer) {%n", k);
            ipw.more();
        } else {
            ipw.printf("par.new DoParallelFair<Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">>(maxThread, transformer) {%n");
            }
            ipw.less();
        }
        ipw.override();
        ipw.printf("protected void openResources() throws Exception {%n");
        ipw.more();
        ipw.printf("try (%n");
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("T%1$d t%1$d = sink%1$d.get();%n", i);
        }
        ipw.printf("S s = par.source.get()) {%n");
        ipw.less();
        if (k == 1) {
            ipw.printf("doWork(par.source.iterator(s), oo -> sink%1$d.accept(t%1$d, oo));%n", k);
        } else {
            ipw.printf("doWork(par.source.iterator(s), oo -> {%n");
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("oo.onT%1$d(v -> sink%1$d.accept(t%1$d, v));%n", i);
            }
            ipw.less();
            ipw.printf("});%n");
        }
        ipw.ends();
        ipw.ends();
        ipw.less();
        ipw.printf("}.start();%n");
        ipw.ends();
    }

    private void writeShutdownTmo(PrintModel ipw, int k) {
        ipw.override();
        ipw.printf("public ParallelLoop%d<I,", k);
        for (int i = 1; i <= k; i++) {
            ipw.putf(" O%d", i);
            ipw.putf(i < k ? "," : "> shutdownTimeout(long time, TimeUnit unit) {%n");
        }
        ipw.more();
        ipw.printf("par.setShutdownTimeout(time, unit);%n");
        ipw.printf("return this;%n");
        ipw.ends();
    }

    private void writeSeqWorker(PrintModel ipw, int k) {
        ipw.override();
        ipw.printf("public void forEach(Worker%d<? super I,%n", k);
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("Consumer<? super O%d>", i);
            ipw.putf(i < k ? ",%n" : "> worker) {%n");
        }
        ipw.less();
        ipw.printf("try (%n");
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("T%1$d t%1$d = sink%1$d.get();%n", i);
        }
        ipw.printf("S s = par.source.get()) {%n");
        ipw.less();
        for (int i = 1; i <= k; i++) {
            ipw.printf("Consumer<? super O%1$d> w%1$d = consumerOf(sink%1$d, t%1$d);%n", i);
        }
        ipw.printf("Iterator<I> iterator = par.source.iterator(s);%n");
        ipw.printf("while (iterator.hasNext()) {%n");
        ipw.more();
        ipw.printf("I i = iterator.next();%n");
        ipw.printf("if (par.terminateTest != null && par.terminateTest.test(i)) {%n");
        ipw.more();
        ipw.printf("log.warn(\"QW%d.>>> Loop terminated on condition\");%n", k);
        ipw.printf("break;%n");
        ipw.ends();
        ipw.printf("if (par.beforeAction != null) par.beforeAction.run();%n");
        ipw.printf("worker.process(i,");
        for (int i = 1; i <= k; i++) {
            ipw.putf(" w%d", i);
            ipw.putf(i < k ? "," : ");%n");
        }
        ipw.ends();
        ipw.less();
        ipw.printf("} catch (BatchException e) {%n");
        ipw.more();
        ipw.printf("throw e;%n");
        ipw.less();
        ipw.printf("} catch (Exception e) {%n");
        ipw.more();
        ipw.printf("throw new BatchException(e);%n");
        ipw.ends();
        ipw.ends();
    }

    private void writeSeqTuple(PrintModel ipw, int k) {
        ipw.override();
        ipw.printf("public void forEach(Function<%n");
        ipw.more();
        ipw.more();
        ipw.printf("? super I,%n");
        if (k == 1) {
            ipw.printf("? extends O%d> transformer) {%n", k);
        } else {
            ipw.printf("? extends Tuple%d<%n", k);
            ipw.more();
            ipw.more();
            for (int i = 1; i <= k; i++) {
                ipw.printf("? extends O%d", i);
                ipw.putf(i < k ? ",%n" : ">> transformer) {%n");
            }
            ipw.less();
            ipw.less();
        }
        ipw.less();
        ipw.printf("try (%n");
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("T%1$d t%1$d = sink%1$d.get();%n", i);
        }
        ipw.printf("S s = par.source.get()) {%n");
        ipw.less();
        ipw.printf("Iterator<I> iterator = par.source.iterator(s);%n");
        ipw.printf("while (iterator.hasNext()) {%n");
        ipw.more();
        ipw.printf("I i = iterator.next();%n");
        ipw.printf("if (par.terminateTest != null && par.terminateTest.test(i)) {%n");
        ipw.more();
        ipw.printf("log.warn(\"QT%d.>>> Loop terminated on condition\");%n", k);
        ipw.printf("break;%n");
        ipw.ends();
        ipw.printf("if (par.beforeAction != null) par.beforeAction.run();%n");
        if (k == 1) {
            ipw.printf("sink%1$d.accept(t%1$d, transformer.apply(i));%n", k);
        } else {
            ipw.printf("val oo = transformer.apply(i);%n");
            for (int i = 1; i <= k; i++) {
                ipw.printf("oo.onT%1$d(v -> sink%1$d.accept(t%1$d, v));%n", i);
            }
        }
        ipw.ends();
        ipw.less();
        ipw.printf("} catch (BatchException e) {%n");
        ipw.more();
        ipw.printf("throw e;%n");
        ipw.less();
        ipw.printf("} catch (Exception e) {%n");
        ipw.more();
        ipw.printf("throw new BatchException(e);%n");
        ipw.ends();
        ipw.ends();
    }

    private void writeConstructor(PrintModel ipw, int k) {
        ipw.printf("PmIterableLoop%d(PmPushSource<S, I> parent,%n", k);
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("SinkResource<T%1$d, O%1$d> sink%1$d", i);
            ipw.putf(i < k ? ",%n" : ") {%n");
        }
        ipw.less();
        ipw.printf("this.par = parent;%n");
        for (int i = 1; i <= k; i++) {
            ipw.printf("this.sink%1$d = sink%1$d;%n", i);
        }
        ipw.ends();
    }

    private void writeFields(PrintModel ipw, int k) {
        ipw.printf("private final PmPushSource<S, I> par;%n");
        for (int i = 1; i <= k; i++) {
            ipw.printf("private final SinkResource<T%1$d, O%1$d> sink%1$d;%n", i);
        }
    }

    private void writeDeclareClass(PrintModel ipw, int k) {
        ipw.printf("@Slf4j%n");
        ipw.printf("class PmIterableLoop%d<S extends AutoCloseable, I,%n", k);
        ipw.more();
        ipw.more();
        for (int i = 1; i <= k; i++) {
            ipw.printf("O%1$d, T%1$d extends AutoCloseable", i);
            if (i < k) {
                ipw.putf(",%n");
            } else {
                ipw.putf("> implements IterableLoop%d<I,", k);
                for (int j = 1; j <= k; j++) {
                    ipw.putf(" O%d", j);
                    ipw.putf(j < k ? "," : "> {%n");
                }
            }
        }
        ipw.less();
        ipw.less();
    }

    private void writeImport(PrintModel ipw) {
        ipw.printf("import lombok.extern.slf4j.Slf4j;%n");
        ipw.printf("import lombok.val;%n");
        ipw.println();
        ipw.printf("import java.util.Iterator;%n");
        ipw.printf("import java.util.List;%n");
        ipw.printf("import java.util.concurrent.Future;%n");
        ipw.printf("import java.util.concurrent.TimeUnit;%n");
        ipw.printf("import java.util.function.Consumer;%n");
        ipw.printf("import java.util.function.Function;%n");
        ipw.println();
        ipw.printf("import static %s.PmPushCore.consumerOf;%n", packageName);
    }
}
