Interface PullWorker3o8
=======================
interface to provide 3 readers and 8 writers

io.github.epi155.pm.batch.step.PullWorker3o8 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Return type |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----------- |
| **public abstract** | [proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Supplier<? extends I3> rd3, function.Consumer<? super O1> wr1, function.Consumer<? super O2> wr2, function.Consumer<? super O3> wr3, function.Consumer<? super O4> wr4, function.Consumer<? super O5> wr5, function.Consumer<? super O6> wr6, function.Consumer<? super O7> wr7, function.Consumer<? super O8> wr8)](#proceedfunctionsupplier?-extends-i1-rd1-functionsupplier?-extends-i2-rd2-functionsupplier?-extends-i3-rd3-functionconsumer?-super-o1-wr1-functionconsumer?-super-o2-wr2-functionconsumer?-super-o3-wr3-functionconsumer?-super-o4-wr4-functionconsumer?-super-o5-wr5-functionconsumer?-super-o6-wr6-functionconsumer?-super-o7-wr7-functionconsumer?-super-o8-wr8) | void        |

Methods
=======
proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Supplier<? extends I3> rd3, function.Consumer<? super O1> wr1, function.Consumer<? super O2> wr2, function.Consumer<? super O3> wr3, function.Consumer<? super O4> wr4, function.Consumer<? super O5> wr5, function.Consumer<? super O6> wr6, function.Consumer<? super O7> wr7, function.Consumer<? super O8> wr8)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
provide 3 readers and 8 writers

### Parameters

| Name | Description |
| ---- | ----------- |
| rd1  | 1st reader  |
| rd2  | 2nd reader  |
| rd3  | 3rd reader  |
| wr1  | 1st writer  |
| wr2  | 2nd writer  |
| wr3  | 3rd writer  |
| wr4  | 4th writer  |
| wr5  | 5th writer  |
| wr6  | 6th writer  |
| wr7  | 7th writer  |
| wr8  | 8th writer  |


