Interface PullWorker2o6
=======================
interface to provide 2 readers and 6 writers

io.github.epi155.pm.batch.step.PullWorker2o6 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | Return type |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Consumer<? super O1> wr1, function.Consumer<? super O2> wr2, function.Consumer<? super O3> wr3, function.Consumer<? super O4> wr4, function.Consumer<? super O5> wr5, function.Consumer<? super O6> wr6)](#proceedfunctionsupplier?-extends-i1-rd1-functionsupplier?-extends-i2-rd2-functionconsumer?-super-o1-wr1-functionconsumer?-super-o2-wr2-functionconsumer?-super-o3-wr3-functionconsumer?-super-o4-wr4-functionconsumer?-super-o5-wr5-functionconsumer?-super-o6-wr6) | void        |

Methods
=======
proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Consumer<? super O1> wr1, function.Consumer<? super O2> wr2, function.Consumer<? super O3> wr3, function.Consumer<? super O4> wr4, function.Consumer<? super O5> wr5, function.Consumer<? super O6> wr6)
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
provide 2 readers and 6 writers

### Parameters

| Name | Description |
| ---- | ----------- |
| rd1  | 1st reader  |
| rd2  | 2nd reader  |
| wr1  | 1st writer  |
| wr2  | 2nd writer  |
| wr3  | 3rd writer  |
| wr4  | 4th writer  |
| wr5  | 5th writer  |
| wr6  | 6th writer  |


