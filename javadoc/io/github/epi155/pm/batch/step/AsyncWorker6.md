Interface AsyncWorker6
======================
interface to provide 1 input item and 6 writer for asynchronous process

io.github.epi155.pm.batch.step.AsyncWorker6 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                      | Return type          |
| ------------------- | ----------------------------------------------------------------------------------------------------- | -------------------- |
| **public abstract** | [apply(I i, W1 w1, W2 w2, W3 w3, W4 w4, W5 w5, W6 w6)](#applyi-i-w1-w1-w2-w2-w3-w3-w4-w4-w5-w5-w6-w6) | concurrent.Future<?> |

Methods
=======
apply(I i, W1 w1, W2 w2, W3 w3, W4 w4, W5 w5, W6 w6)
----------------------------------------------------
provide 1 input item and 6 writer for asynchronous process

### Parameters

| Name | Description |
| ---- | ----------- |
| i    | input item  |
| w1   | 1st writer  |
| w2   | 2nd writer  |
| w3   | 3rd writer  |
| w4   | 4th writer  |
| w5   | 5th writer  |
| w6   | 6th writer  |

### Returns

{@link Future} instance of asynchronous process

