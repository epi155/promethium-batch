Interface IterableLoop6 implements io.github.epi155.pm.batch.step.ParallelLoop6<I,O1,O2,O3,O4,O5,O6>
====================================================================================================
interface to handle a repeated transformation from 1 input to 6 outputs

io.github.epi155.pm.batch.step.IterableLoop6 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | Return type                                                       |
| ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------- |
| **public abstract** | [forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple6<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6>> transformer)](#foreachfunctionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple6?-extends-o1-?-extends-o2-?-extends-o3-?-extends-o4-?-extends-o5-?-extends-o6-transformer)                                                                                                                            | void                                                              |
| **public abstract** | [forEach(io.github.epi155.pm.batch.step.Worker6<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>> worker)](#foreachiogithubepi155pmbatchstepworker6?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-functionconsumer?-super-o3-functionconsumer?-super-o4-functionconsumer?-super-o5-functionconsumer?-super-o6-worker) | void                                                              |
| **public abstract** | [shutdownTimeout(long time, TimeUnit unit)](#shutdowntimeoutlong-time-timeunit-unit)                                                                                                                                                                                                                                                                                                                                                                                                | io.github.epi155.pm.batch.step.ParallelLoop6<I,O1,O2,O3,O4,O5,O6> |

Methods
=======
forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple6<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6>> transformer)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs sequentially

### Parameters

| Name        | Description                                                    |
| ----------- | -------------------------------------------------------------- |
| transformer | function that transforms input into a  {@link Tuple6}  outputs |


forEach(io.github.epi155.pm.batch.step.Worker6<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>> worker)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

instance of {@link ParallelLoop6} for run parallel processing


