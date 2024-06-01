Interface IterableLoop8 implements io.github.epi155.pm.batch.step.ParallelLoop8<I,O1,O2,O3,O4,O5,O6,O7,O8>
==========================================================================================================
interface to handle a repeated transformation from 1 input to 8 outputs

io.github.epi155.pm.batch.step.IterableLoop8 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | Return type                                                             |
| ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| **public abstract** | [forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple8<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7,? extends O8>> transformer)](#foreachfunctionfunction?-super-i-?-extends-iogithubepi155pmbatchsteptuple8?-extends-o1-?-extends-o2-?-extends-o3-?-extends-o4-?-extends-o5-?-extends-o6-?-extends-o7-?-extends-o8-transformer)                                                                                                                                                                                          | void                                                                    |
| **public abstract** | [forEach(io.github.epi155.pm.batch.step.Worker8<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>,function.Consumer<? super O7>,function.Consumer<? super O8>> worker)](#foreachiogithubepi155pmbatchstepworker8?-super-i-functionconsumer?-super-o1-functionconsumer?-super-o2-functionconsumer?-super-o3-functionconsumer?-super-o4-functionconsumer?-super-o5-functionconsumer?-super-o6-functionconsumer?-super-o7-functionconsumer?-super-o8-worker) | void                                                                    |
| **public abstract** | [shutdownTimeout(long time, TimeUnit unit)](#shutdowntimeoutlong-time-timeunit-unit)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  | io.github.epi155.pm.batch.step.ParallelLoop8<I,O1,O2,O3,O4,O5,O6,O7,O8> |

Methods
=======
forEach(function.Function<? super I,? extends io.github.epi155.pm.batch.step.Tuple8<? extends O1,? extends O2,? extends O3,? extends O4,? extends O5,? extends O6,? extends O7,? extends O8>> transformer)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
performs repeated transformation from input to outputs sequentially

### Parameters

| Name        | Description                                                    |
| ----------- | -------------------------------------------------------------- |
| transformer | function that transforms input into a  {@link Tuple8}  outputs |


forEach(io.github.epi155.pm.batch.step.Worker8<? super I,function.Consumer<? super O1>,function.Consumer<? super O2>,function.Consumer<? super O3>,function.Consumer<? super O4>,function.Consumer<? super O5>,function.Consumer<? super O6>,function.Consumer<? super O7>,function.Consumer<? super O8>> worker)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

instance of {@link ParallelLoop8} for run parallel processing


