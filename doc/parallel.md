## Parallel Processing

Three parallel processing implementations were developed

### 1. First End First Write

~~~java
void forEachParallel(int maxThread, Function<? super I,? extends Tuple2<? extends O1,? extends O2>> transformer)
~~~

### 2. First Start First Write

~~~java
void forEachParallelFair(int maxThread, Function<? super I,? extends Tuple2<? extends O1,? extends O2>> transformer)
~~~

### 3. Parallel Write

~~~java
void forEachParallel(int maxThread, Worker2<? super I,Consumer<? super O1>,Consumer<? super O2>> worker)
~~~

### General notes

`maxThread` is the maximum number of processing tasks executed in parallel.
The thread that submits processing tasks stops when the number of active tasks reaches the maximum value, and waits for a task to become free before submitting another.

When all the processing tasks have been submitted, a time limit is waited to allow the active tasks to finish.
If this time limit is exceeded, the processing tasks are interrupted, this could generate an abnormal end of the processing loop. If the processing task does not handle the *interrupt flag* the task may not terminate.

The default value for this timeout is 30 seconds, you can change it by calling the `shutdownTimeout` method before `forEachParallel`.

~~~java
Batch.from(src).into(snk1,snk2).shutdownTimeout(5, TimeUnit.SECONDS).forEachParallel(maxThread, ...);
~~~