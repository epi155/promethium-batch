Interface PullWorker2o0
=======================
interface to provide 2 readers and no writer

io.github.epi155.pm.batch.step.PullWorker2o0 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                               | Return type |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2)](#proceedfunctionsupplier?-extends-i1-rd1-functionsupplier?-extends-i2-rd2) | void        |

Methods
=======
proceed(function.Supplier<? extends I1> rd1, function.Supplier<? extends I2> rd2)
---------------------------------------------------------------------------------
provide 2 readers and 1 writer

### Parameters

| Name | Description |
| ---- | ----------- |
| rd1  | 1st reader  |
| rd2  | 2nd reader  |


