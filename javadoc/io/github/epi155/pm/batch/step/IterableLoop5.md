Interface IterableLoop5 implements io.github.epi155.pm.batch.step.ParallelLoop5<I,O1,O2,O3,O4,O5>
=================================================================================================
interface to handle a repeated transformation from 1 input to 5 outputs

io.github.epi155.pm.batch.step.IterableLoop5 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                           | Return type                                                    |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------- |
| **public abstract** | [forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple5<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5>> transformer)](#foreachfunctionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple5?-extends-o1-?-extends-o2-?-extends-o3-?-extends-o4-?-extends-o5-transformer)                                                                                             | void                                                           |
| **public abstract** | [forEach(io.github.epi155.pm.batch.step.Worker5<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>> worker)](#foreachiogithubepi155pmbatchstepworker5?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-functionconsumer?-super-o3-functionconsumer?-super-o4-functionconsumer?-super-o5-worker) | void                                                           |
| **public abstract** | [shutdownTimeout(long time, TimeUnit unit)](#shutdowntimeoutlong-time-timeunit-unit)                                                                                                                                                                                                                                                                                                                                       | io.github.epi155.pm.batch.step.ParallelLoop5<I,O1,O2,O3,O4,O5> |

Methods
=======
forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple5<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5>> transformer)
-------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs sequentially

### Parameters

| Name        | Description                                                    |
| ----------- | -------------------------------------------------------------- |
| transformer | function that transforms input into a  {@link Tuple5}  outputs |


forEach(io.github.epi155.pm.batch.step.Worker5<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>> worker)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

instance of {@link ParallelLoop5} for run parallel processing


