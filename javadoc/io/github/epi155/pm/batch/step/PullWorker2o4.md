Interface PullWorker2o4
=======================
interface to provide 2 readers and 4 writers

io.github.epi155.pm.batch.step.PullWorker2o4 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                       | Return type |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Consumer<? super O1> wr1, function.Consumer<? super O2> wr2, function.Consumer<? super O3> wr3, function.Consumer<? super O4> wr4)](#proceedfunctionsupplier?-extends-i1-rd1-functionsupplier?-extends-i2-rd2-functionconsumer?-super-o1-wr1-functionconsumer?-super-o2-wr2-functionconsumer?-super-o3-wr3-functionconsumer?-super-o4-wr4) | void        |

Methods
=======
proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Consumer<? super O1> wr1, function.Consumer<? super O2> wr2, function.Consumer<? super O3> wr3, function.Consumer<? super O4> wr4)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
provide 2 readers and 4 writers

### Parameters

| Name | Description |
| ---- | ----------- |
| rd1  | 1st reader  |
| rd2  | 2nd reader  |
| wr1  | 1st writer  |
| wr2  | 2nd writer  |
| wr3  | 3rd writer  |
| wr4  | 4th writer  |


