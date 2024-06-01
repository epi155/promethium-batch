Interface IterableLoop2 implements io.github.epi155.pm.batch.step.ParallelLoop2<I,O1,O2>
========================================================================================
interface to handle a repeated transformation from 1 input to 2 outputs

io.github.epi155.pm.batch.step.IterableLoop2 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                 | Return type                                           |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------- |
| **public abstract** | [forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple2<? extends O1,? extends O2>> transformer)](#foreachfunctionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple2?-extends-o1-?-extends-o2-transformer) | void                                                  |
| **public abstract** | [forEach(io.github.epi155.pm.batch.step.Worker2<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>> worker)](#foreachiogithubepi155pmbatchstepworker2?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-worker)  | void                                                  |
| **public abstract** | [shutdownTimeout(long time, TimeUnit unit)](#shutdowntimeoutlong-time-timeunit-unit)                                                                                                                                                             | io.github.epi155.pm.batch.step.ParallelLoop2<I,O1,O2> |

Methods
=======
forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple2<? extends O1,? extends O2>> transformer)
----------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs sequentially

### Parameters

| Name        | Description                                                    |
| ----------- | -------------------------------------------------------------- |
| transformer | function that transforms input into a  {@link Tuple2}  outputs |


forEach(io.github.epi155.pm.batch.step.Worker2<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>> worker)
-----------------------------------------------------------------------------------------------------------------------------
performs repeated action from input to outputs sequentially

### Parameters

| Name   | Description                                                                 |
| ------ | --------------------------------------------------------------------------- |
| worker | worker who takes the input value and writes the outputs using the consumers |


shutdownTimeout(long time, TimeUnit unit)
-----------------------------------------
sets the shutdown timeout for parallel processing

### Parameters

| Name | Description |
| ---- | ----------- |
| time | time amount |
| unit | time unit   |

### Returns

instance of {@link ParallelLoop2} for run parallel processing


