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