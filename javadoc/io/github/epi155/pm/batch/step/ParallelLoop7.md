Interface ParallelLoop7
=======================
interface to handle a repeated transformation from 1 input to 7 outputs in parallel

io.github.epi155.pm.batch.step.ParallelLoop7 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | Return type |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [forEachParallel(int maxThread, io.github.epi155.pm.batch.step.Worker7<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>,function.Consumer<? super O7>> worker)](#foreachparallelint-maxthread-iogithubepi155pmbatchstepworker7?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-functionconsumer?-super-o3-functionconsumer?-super-o4-functionconsumer?-super-o5-functionconsumer?-super-o6-functionconsumer?-super-o7-worker)               | void        |
| **public abstract** | [forEachParallelFair(int maxThread, function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple7<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7>> transformer)](#foreachparallelfairint-maxthread-functionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple7?-extends-o1-?-extends-o2-?-extends-o3-?-extends-o4-?-extends-o5-?-extends-o6-?-extends-o7-transformer)                                                                                                                                                                 | void        |
| **public abstract** | [forEachParallel(int maxThread, function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple7<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7>> transformer)](#foreachparallelint-maxthread-functionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple7?-extends-o1-?-extends-o2-?-extends-o3-?-extends-o4-?-extends-o5-?-extends-o6-?-extends-o7-transformer)                                                                                                                                                                         | void        |
| **public abstract** | [forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<? extends io.github.epi155.pm.batch.step.Tuple7<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7>>> asyncTransformer)](#foreachasyncint-maxthread-functionfunction?-super-i-?-extends-concurrentfuture?-extends-iogithubepi155pmbatchsteptuple7?-extends-o1-?-extends-o2-?-extends-o3-?-extends-o4-?-extends-o5-?-extends-o6-?-extends-o7-asynctransformer)                                                                                                              | void        |
| **public abstract** | [forEachAsync(int maxThread, io.github.epi155.pm.batch.step.AsyncWorker7<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>,function.Consumer<? super O7>> asyncWorker)](#foreachasyncint-maxthread-iogithubepi155pmbatchstepasyncworker7?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-functionconsumer?-super-o3-functionconsumer?-super-o4-functionconsumer?-super-o5-functionconsumer?-super-o6-functionconsumer?-super-o7-asyncworker) | void        |

Methods
=======
forEachParallel(int maxThread, io.github.epi155.pm.batch.step.Worker7<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>,function.Consumer<? super O7>> worker)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated action from input to output in parallel
 <pre>Pgm.from(src)
      .into(snk1,snk2,snk3,snk4,snk5,snk6,snk7)
      .forEachParallel(n,(i,wr1,wr2,wr3,wr4,wr5,wr6,wr7) -> { ... });</pre>

### Parameters

| Name      | Description                                                                 |
| --------- | --------------------------------------------------------------------------- |
| maxThread | maximum number of parallel processing                                       |
| worker    | worker who takes the input value and writes the outputs using the consumers |


forEachParallelFair(int maxThread, function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple7<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7>> transformer)
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs in parallel
 <p>first starts first writes
 <pre>Pgm.from(src).into(snk).forEachParallelFair(n,i -> { ... });</pre>

### Parameters

| Name        | Description                                                  |
| ----------- | ------------------------------------------------------------ |
| maxThread   | maximum number of parallel processing                        |
| transformer | function that transforms input into  {@link Tuple7}  outputs |


forEachParallel(int maxThread, function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple7<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7>> transformer)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs in parallel
 <p>first ends first writes
 <pre>Pgm.from(src).into(snk).forEachParallel(n,i -> { ... });</pre>

### Parameters

| Name        | Description                                                  |
| ----------- | ------------------------------------------------------------ |
| maxThread   | maximum number of parallel processing                        |
| transformer | function that transforms input into  {@link Tuple7}  outputs |


forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<? extends io.github.epi155.pm.batch.step.Tuple7<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7>>> asyncTransformer)
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to output using asynchronous task

### Parameters

| Name             | Description                                                               |
| ---------------- | ------------------------------------------------------------------------- |
| maxThread        | maximum number of parallel processing                                     |
| asyncTransformer | asynchronous function that transforms input into  {@link Tuple7}  outputs |


forEachAsync(int maxThread, io.github.epi155.pm.batch.step.AsyncWorker7<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>,function.Consumer<? super O7>> asyncWorker)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated action from input to output using asynchronous worker

### Parameters

| Name        | Description                                                                    |
| ----------- | ------------------------------------------------------------------------------ |
| maxThread   | maximum number of parallel processing                                          |
| asyncWorker | asyncWorker who takes the input value and writes the output using the consumer |

