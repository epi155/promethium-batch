Interface AsyncWorker
=====================
interface to provide 1 input item and 1 writer for asynchronous process

io.github.epi155.pm.batch.step.AsyncWorker Summary
-------
#### Methods
| Modifiers           | Method signature                 | Return type          |
| ------------------- | -------------------------------- | -------------------- |
| **public abstract** | [apply(I i, W w)](#applyi-i-w-w) | concurrent.Future<?> |

Methods
=======
apply(I i, W w)
---------------
provide 1 input item and 1 writer for asynchronous process

### Parameters

| Name | Description |
| ---- | ----------- |
| i    | input item  |
| w    | writer      |

### Returns

{@link Future} instance of asynchronous process


