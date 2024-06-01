Interface ParallelLoop2
=======================
interface to handle a repeated transformation from 1 input to 2 outputs in parallel

io.github.epi155.pm.batch.step.ParallelLoop2 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                         | Return type |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [forEachParallel(int maxThread, io.github.epi155.pm.batch.step.Worker2<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>> worker)](#foreachparallelint-maxthread-iogithubepi155pmbatchstepworker2?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-worker)                                                             | void        |
| **public abstract** | [forEachParallelFair(int maxThread, function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple2<? extends O1,? extends O2>> transformer)](#foreachparallelfairint-maxthread-functionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple2?-extends-o1-?-extends-o2-transformer)                                                    | void        |
| **public abstract** | [forEachParallel(int maxThread, function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple2<? extends O1,? extends O2>> transformer)](#foreachparallelint-maxthread-functionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple2?-extends-o1-?-extends-o2-transformer)                                                            | void        |
| **public abstract** | [forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<? extends io.github.epi155.pm.batch.step.Tuple2<? extends O1,? extends O2>>> asyncTransformer)](#foreachasyncint-maxthread-functionfunction?-super-i-?-extends-concurrentfuture?-extends-iogithubepi155pmbatchsteptuple2?-extends-o1-?-extends-o2-asynctransformer) | void        |
| **public abstract** | [forEachAsync(int maxThread, io.github.epi155.pm.batch.step.AsyncWorker2<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>> asyncWorker)](#foreachasyncint-maxthread-iogithubepi155pmbatchstepasyncworker2?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-asyncworker)                                               | void        |

Methods
=======
forEachParallel(int maxThread, io.github.epi155.pm.batch.step.Worker2<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>> worker)
----------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated action from input to output in parallel
 <pre>Pgm.from(src).into(snk1,snk2).forEachParallel(n,(i,wr1,wr2) -> { ... });</pre>

### Parameters

| Name      | Description                                                                 |
| --------- | --------------------------------------------------------------------------- |
| maxThread | maximum number of parallel processing                                       |
| worker    | worker who takes the input value and writes the outputs using the consumers |


forEachParallelFair(int maxThread, function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple2<? extends O1,? extends O2>> transformer)
-------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs in parallel
 <p>first starts first writes
 <pre>Pgm.from(src).into(snk).forEachParallelFair(n,i -> { ... });</pre>

### Parameters

| Name        | Description                                                  |
| ----------- | ------------------------------------------------------------ |
| maxThread   | maximum number of parallel processing                        |
| transformer | function that transforms input into  {@link Tuple2}  outputs |


forEachParallel(int maxThread, function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple2<? extends O1,? extends O2>> transformer)
---------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs in parallel
 <p>first ends first writes
 <pre>Pgm.from(src).into(snk).forEachParallel(n,i -> { ... });</pre>

### Parameters

| Name        | Description                                                  |
| ----------- | ------------------------------------------------------------ |
| maxThread   | maximum number of parallel processing                        |
| transformer | function that transforms input into  {@link Tuple2}  outputs |


forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<? extends io.github.epi155.pm.batch.step.Tuple2<? extends O1,? extends O2>>> asyncTransformer)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to output using asynchronous task

### Parameters

| Name             | Description                                                               |
| ---------------- | ------------------------------------------------------------------------- |
| maxThread        | maximum number of parallel processing                                     |
| asyncTransformer | asynchronous function that transforms input into  {@link Tuple2}  outputs |


forEachAsync(int maxThread, io.github.epi155.pm.batch.step.AsyncWorker2<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>> asyncWorker)
-----------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated action from input to output using asynchronous worker

### Parameters

| Name        | Description                                                                    |
| ----------- | ------------------------------------------------------------------------------ |
| maxThread   | maximum number of parallel processing                                          |
| asyncWorker | asyncWorker who takes the input value and writes the output using the consumer |


