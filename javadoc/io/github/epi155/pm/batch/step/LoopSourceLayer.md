Interface LoopSourceLayer implements io.github.epi155.pm.batch.step.LoopSourceStd<I>
====================================================================================
interface to manage the elements of a single input resource.
 


 it is possible to process the input immediately or to define one or more
 output resources where the input processing can be sent

io.github.epi155.pm.batch.step.LoopSourceLayer Summary
-------
#### Methods
| Modifiers           | Method signature                                  | Return type                                     |
| ------------------- | ------------------------------------------------- | ----------------------------------------------- |
| **public abstract** | [before(Runnable action)](#beforerunnable-action) | io.github.epi155.pm.batch.step.LoopSourceStd<I> |

Methods
=======
before(Runnable action)
-----------------------
performs the indicated action after reading each element read before main processing

### Parameters

| Name   | Description       |
| ------ | ----------------- |
| action | action to perform |

### Returns

new instance of {@link LoopSourceStd}


