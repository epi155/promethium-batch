Interface ParallelLoop0
=======================
interface to handle a repeated transformation from 1 input, no output in parallel

io.github.epi155.pm.batch.step.ParallelLoop0 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                     | Return type |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [forEachParallel(int maxThread, function.Consumer<? super I> action)](#foreachparallelint-maxthread-functionconsumer?-super-i-action)                                                                                | void        |
| **public abstract** | [forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<Void>> asyncTransformer)](#foreachasyncint-maxthread-functionfunction?-super-i-?-extends-concurrentfuturevoid-asynctransformer) | void        |

Methods
=======
forEachParallel(int maxThread, function.Consumer<? super I> action)
-------------------------------------------------------------------
Performs the specified action for each item taken from the source until all items have been processed or the
 action throws an exception. Up to {@code maxThread} elements are processed in parallel

### Parameters

| Name      | Description                                 |
| --------- | ------------------------------------------- |
| maxThread | maximum number of parallel processing       |
| action    | The action to be performed for each element |


forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<Void>> asyncTransformer)
------------------------------------------------------------------------------------------------------------
performs repeated transformation from input using asynchronous task

### Parameters

| Name             | Description                                             |
| ---------------- | ------------------------------------------------------- |
| maxThread        | maximum number of parallel processing                   |
| asyncTransformer | asynchronous function that transforms input into output |


