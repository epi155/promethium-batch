Interface AsyncWorker5
======================
interface to provide 1 input item and 5 writer for asynchronous process

io.github.epi155.pm.batch.step.AsyncWorker5 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                         | Return type          |
| ------------------- | ---------------------------------------------------------------------------------------- | -------------------- |
| **public abstract** | [apply(I i, W1 w1, W2 w2, W3 w3, W4 w4, W5 w5)](#applyi-i-w1-w1-w2-w2-w3-w3-w4-w4-w5-w5) | concurrent.Future<?> |

Methods
=======
apply(I i, W1 w1, W2 w2, W3 w3, W4 w4, W5 w5)
---------------------------------------------
provide 1 input item and 5 writer for asynchronous process

### Parameters

| Name | Description |
| ---- | ----------- |
| i    | input item  |
| w1   | 1st writer  |
| w2   | 2nd writer  |
| w3   | 3rd writer  |
| w4   | 4th writer  |
| w5   | 5th writer  |

### Returns

{@link Future} instance of asynchronous process


