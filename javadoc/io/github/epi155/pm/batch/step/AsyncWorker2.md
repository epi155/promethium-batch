Interface AsyncWorker2
======================
interface to provide 1 input item and 2 writer for asynchronous process

io.github.epi155.pm.batch.step.AsyncWorker2 Summary
-------
#### Methods
| Modifiers           | Method signature                                  | Return type          |
| ------------------- | ------------------------------------------------- | -------------------- |
| **public abstract** | [apply(I i, W1 w1, W2 w2)](#applyi-i-w1-w1-w2-w2) | concurrent.Future<?> |

Methods
=======
apply(I i, W1 w1, W2 w2)
------------------------
provide 1 input item and 2 writer for asynchronous process

### Parameters

| Name | Description |
| ---- | ----------- |
| i    | input item  |
| w1   | 1st writer  |
| w2   | 2nd writer  |

### Returns

{@link Future} instance of asynchronous process


