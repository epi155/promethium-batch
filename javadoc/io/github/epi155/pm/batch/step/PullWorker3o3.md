Interface PullWorker3o3
=======================
interface to provide 3 readers and 3 writers

io.github.epi155.pm.batch.step.PullWorker3o3 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                           | Return type |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----------- |
| **public abstract** | [proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Supplier<? extends I3> rd3, function.Consumer<? super O1> wr1, function.Consumer<? super O2> wr2, function.Consumer<? super O3> wr3)](#proceedfunctionsupplier?-extends-i1-rd1-functionsupplier?-extends-i2-rd2-functionsupplier?-extends-i3-rd3-functionconsumer?-super-o1-wr1-functionconsumer?-super-o2-wr2-functionconsumer?-super-o3-wr3) | void        |

Methods
=======
proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Supplier<? extends I3> rd3, function.Consumer<? super O1> wr1, function.Consumer<? super O2> wr2, function.Consumer<? super O3> wr3)
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
provide 3 readers and 3 writers

### Parameters

| Name | Description |
| ---- | ----------- |
| rd1  | 1st reader  |
| rd2  | 2nd reader  |
| rd3  | 3rd reader  |
| wr1  | 1st writer  |
| wr2  | 2nd writer  |
| wr3  | 3rd writer  |


