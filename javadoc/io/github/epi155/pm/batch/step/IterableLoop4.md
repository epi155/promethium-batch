Interface IterableLoop4 implements io.github.epi155.pm.batch.step.ParallelLoop4<I,O1,O2,O3,O4>
==============================================================================================
interface to handle a repeated transformation from 1 input to 4 outputs

io.github.epi155.pm.batch.step.IterableLoop4 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                  | Return type                                                 |
| ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------- |
| **public abstract** | [forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple4<? extends O1,? extends O2,? extends O3,? extends O4>> transformer)](#foreachfunctionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple4?-extends-o1-?-extends-o2-?-extends-o3-?-extends-o4-transformer)                                                              | void                                                        |
| **public abstract** | [forEach(io.github.epi155.pm.batch.step.Worker4<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>> worker)](#foreachiogithubepi155pmbatchstepworker4?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-functionconsumer?-super-o3-functionconsumer?-super-o4-worker) | void                                                        |
| **public abstract** | [shutdownTimeout(long time, TimeUnit unit)](#shutdowntimeoutlong-time-timeunit-unit)                                                                                                                                                                                                                                                                              | io.github.epi155.pm.batch.step.ParallelLoop4<I,O1,O2,O3,O4> |

Methods
=======
forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple4<? extends O1,? extends O2,? extends O3,? extends O4>> transformer)
------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs sequentially

### Parameters

| Name        | Description                                                    |
| ----------- | -------------------------------------------------------------- |
| transformer | function that transforms input into a  {@link Tuple4}  outputs |


forEach(io.github.epi155.pm.batch.step.Worker4<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>> worker)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

instance of {@link ParallelLoop4} for run parallel processing


