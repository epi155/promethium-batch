Interface ParallelLoop1
=======================
interface to handle a repeated transformation from 1 input to 1 output in parallel

io.github.epi155.pm.batch.step.ParallelLoop1 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                              | Return type |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [forEachParallel(int maxThread, io.github.epi155.pm.batch.step.Worker<? super I,function.Consumer<? super O>> worker)](#foreachparallelint-maxthread-iogithubepi155pmbatchstepworker?-super-i-functionconsumer?-super-o-worker)               | void        |
| **public abstract** | [forEachParallelFair(int maxThread, function.Function<? super I,? extends O> transformer)](#foreachparallelfairint-maxthread-functionfunction?-super-i-?-extends-o-transformer)                                                               | void        |
| **public abstract** | [forEachParallel(int maxThread, function.Function<? super I,? extends O> transformer)](#foreachparallelint-maxthread-functionfunction?-super-i-?-extends-o-transformer)                                                                       | void        |
| **public abstract** | [forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<? extends O>> asyncTransformer)](#foreachasyncint-maxthread-functionfunction?-super-i-?-extends-concurrentfuture?-extends-o-asynctransformer)            | void        |
| **public abstract** | [forEachAsync(int maxThread, io.github.epi155.pm.batch.step.AsyncWorker<? super I,function.Consumer<? super O>> asyncWorker)](#foreachasyncint-maxthread-iogithubepi155pmbatchstepasyncworker?-super-i-functionconsumer?-super-o-asyncworker) | void        |

Methods
=======
forEachParallel(int maxThread, io.github.epi155.pm.batch.step.Worker<? super I,function.Consumer<? super O>> worker)
--------------------------------------------------------------------------------------------------------------------
performs repeated action from input to output in parallel
 <pre>Pgm.from(src).into(snk).forEachParallel(n,(i,wr) -> { ... });</pre>

### Parameters

| Name      | Description                                                               |
| --------- | ------------------------------------------------------------------------- |
| maxThread | maximum number of parallel processing                                     |
| worker    | worker who takes the input value and writes the output using the consumer |


forEachParallelFair(int maxThread, function.Function<? super I,? extends O> transformer)
----------------------------------------------------------------------------------------
performs repeated transformation from input to output in parallel
 <p>first starts first writes
 <pre>Pgm.from(src).into(snk).forEachParallelFair(n,i -> { ... });</pre>

### Parameters

| Name        | Description                                |
| ----------- | ------------------------------------------ |
| maxThread   | maximum number of parallel processing      |
| transformer | function that transforms input into output |


forEachParallel(int maxThread, function.Function<? super I,? extends O> transformer)
------------------------------------------------------------------------------------
performs repeated transformation from input to output in parallel
 <p>first ends first writes
 <pre>Pgm.from(src).into(snk).forEachParallel(n,i -> { ... });</pre>

### Parameters

| Name        | Description                                |
| ----------- | ------------------------------------------ |
| maxThread   | maximum number of parallel processing      |
| transformer | function that transforms input into output |


forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<? extends O>> asyncTransformer)
-------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to output using asynchronous task

### Parameters

| Name             | Description                                             |
| ---------------- | ------------------------------------------------------- |
| maxThread        | maximum number of parallel processing                   |
| asyncTransformer | asynchronous function that transforms input into output |


forEachAsync(int maxThread, io.github.epi155.pm.batch.step.AsyncWorker<? super I,function.Consumer<? super O>> asyncWorker)
---------------------------------------------------------------------------------------------------------------------------
performs repeated action from input to output using asynchronous worker

### Parameters

| Name        | Description                                                                    |
| ----------- | ------------------------------------------------------------------------------ |
| maxThread   | maximum number of parallel processing                                          |
| asyncWorker | asyncWorker who takes the input value and writes the output using the consumer |


