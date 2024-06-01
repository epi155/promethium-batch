Interface PullWorker3o0
=======================
interface to provide 3 readers and no writer

io.github.epi155.pm.batch.step.PullWorker3o0 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                     | Return type |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----------- |
| **public abstract** | [proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Supplier<? extends I3> rd3)](#proceedfunctionsupplier?-extends-i1-rd1-functionsupplier?-extends-i2-rd2-functionsupplier?-extends-i3-rd3) | void        |

Methods
=======
proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2, function.Supplier<? extends I3> rd3)
----------------------------------------------------------------------------------------------------------------------
provide 3 readers and no writer

### Parameters

| Name | Description |
| ---- | ----------- |
| rd1  | 1st reader  |
| rd2  | 2nd reader  |
| rd3  | 3rd reader  |


