Interface IterableLoop implements io.github.epi155.pm.batch.step.ParallelLoop1<I,O>
===================================================================================
interface to handle a repeated transformation from 1 input to 1 output

io.github.epi155.pm.batch.step.IterableLoop Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                   | Return type                                       |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------- |
| **public abstract** | [forEach(function.Function<? super I,? extends O> transformer)](#foreachfunctionfunction?-super-i-?-extends-o-transformer)                                                         | void                                              |
| **public abstract** | [forEach(io.github.epi155.pm.batch.step.Worker<? super I,function.Consumer<? super O>> worker)](#foreachiogithubepi155pmbatchstepworker?-super-i-functionconsumer?-super-o-worker) | void                                              |
| **public abstract** | [shutdownTimeout(long time, TimeUnit unit)](#shutdowntimeoutlong-time-timeunit-unit)                                                                                               | io.github.epi155.pm.batch.step.ParallelLoop1<I,O> |

Methods
=======
forEach(function.Function<? super I,? extends O> transformer)
-------------------------------------------------------------
performs repeated transformation from input to output sequentially

### Parameters

| Name        | Description                                |
| ----------- | ------------------------------------------ |
| transformer | function that transforms input into output |


forEach(io.github.epi155.pm.batch.step.Worker<? super I,function.Consumer<? super O>> worker)
---------------------------------------------------------------------------------------------
performs repeated action from input to output sequentially

### Parameters

| Name   | Description                                                               |
| ------ | ------------------------------------------------------------------------- |
| worker | worker who takes the input value and writes the output using the consumer |


shutdownTimeout(long time, TimeUnit unit)
-----------------------------------------
sets the shutdown timeout for parallel processing

### Parameters

| Name | Description |
| ---- | ----------- |
| time | time amount |
| unit | time unit   |

### Returns

instance of {@link ParallelLoop1} for run parallel processing


