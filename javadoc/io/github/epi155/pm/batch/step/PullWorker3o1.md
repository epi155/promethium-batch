Interface PullWorker3o1
=======================
interface to provide 3 readers and 1 writer

io.github.epi155.pm.batch.step.PullWorker3o1 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                   | Return type |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Supplier<? extends I3> rd3, function.Consumer<? super O> wr)](#proceedfunctionsupplier?-extends-i1-rd1-functionsupplier?-extends-i2-rd2-functionsupplier?-extends-i3-rd3-functionconsumer?-super-o-wr) | void        |

Methods
=======
proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Supplier<? extends I3> rd3, function.Consumer<? super O> wr)
-------------------------------------------------------------------------------------------------------------------------------------------------------
provide 3 readers and 1 writer

### Parameters

| Name | Description |
| ---- | ----------- |
| rd1  | 1st reader  |
| rd2  | 2nd reader  |
| rd3  | 3rd reader  |
| wr   | writer      |


