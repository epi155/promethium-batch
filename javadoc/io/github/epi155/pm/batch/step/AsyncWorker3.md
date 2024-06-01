Interface AsyncWorker3
======================
interface to provide 1 input item and 3 writer for asynchronous process

io.github.epi155.pm.batch.step.AsyncWorker3 Summary
-------
#### Methods
| Modifiers           | Method signature                                               | Return type          |
| ------------------- | -------------------------------------------------------------- | -------------------- |
| **public abstract** | [apply(I i, W1 w1, W2 w2, W3 w3)](#applyi-i-w1-w1-w2-w2-w3-w3) | concurrent.Future<?> |

Methods
=======
apply(I i, W1 w1, W2 w2, W3 w3)
-------------------------------
provide 1 input item and 3 writer for asynchronous process

### Parameters

| Name | Description |
| ---- | ----------- |
| i    | input item  |
| w1   | 1st writer  |
| w2   | 2nd writer  |
| w3   | 3rd writer  |

### Returns

{@link Future} instance of asynchronous process


