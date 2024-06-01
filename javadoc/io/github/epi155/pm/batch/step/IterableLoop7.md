Interface IterableLoop7 implements io.github.epi155.pm.batch.step.ParallelLoop7<I,O1,O2,O3,O4,O5,O6,O7>
=======================================================================================================
interface to handle a repeated transformation from 1 input to 7 outputs

io.github.epi155.pm.batch.step.IterableLoop7 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Return type                                                          |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------- |
| **public abstract** | [forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple7<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7>> transformer)](#foreachfunctionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple7?-extends-o1-?-extends-o2-?-extends-o3-?-extends-o4-?-extends-o5-?-extends-o6-?-extends-o7-transformer)                                                                                                                                                           | void                                                                 |
| **public abstract** | [forEach(io.github.epi155.pm.batch.step.Worker7<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>,function.Consumer<? super O7>> worker)](#foreachiogithubepi155pmbatchstepworker7?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-functionconsumer?-super-o3-functionconsumer?-super-o4-functionconsumer?-super-o5-functionconsumer?-super-o6-functionconsumer?-super-o7-worker) | void                                                                 |
| **public abstract** | [shutdownTimeout(long time, TimeUnit unit)](#shutdowntimeoutlong-time-timeunit-unit)                                                                                                                                                                                                                                                                                                                                                                                                                                                         | io.github.epi155.pm.batch.step.ParallelLoop7<I,O1,O2,O3,O4,O5,O6,O7> |

Methods
=======
forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple7<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7>> transformer)
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs sequentially

### Parameters

| Name        | Description                                                    |
| ----------- | -------------------------------------------------------------- |
| transformer | function that transforms input into a  {@link Tuple7}  outputs |


forEach(io.github.epi155.pm.batch.step.Worker7<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>,function.Consumer<? super O7>> worker)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

instance of {@link ParallelLoop7} for run parallel processing


