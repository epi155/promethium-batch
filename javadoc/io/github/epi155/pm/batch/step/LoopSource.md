Interface LoopSource implements io.github.epi155.pm.batch.step.LoopSourceLayer<I>
=================================================================================
terminable interface to manage the elements of a single input resource.
 


 it is possible to process the input immediately or to define one or more
 output resources where the input processing can be sent

io.github.epi155.pm.batch.step.LoopSource Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                   | Return type                                       |
| ------------------- | -------------------------------------------------------------------------------------------------- | ------------------------------------------------- |
| **public abstract** | [terminate(function.Predicate<? super I> test)](#terminatefunctionpredicate?-super-i-test)         | io.github.epi155.pm.batch.step.LoopSourceLayer<I> |
| **public abstract** | [map(function.Function<? super I,? extends J> map)](#mapfunctionfunction?-super-i-?-extends-j-map) | io.github.epi155.pm.batch.step.LoopSource<J>      |

Methods
=======
terminate(function.Predicate<? super I> test)
---------------------------------------------
Sets a condition to interrupt the processing cycle
 <p>even if the value being processed is passed as an argument,
 the condition may depend on an external parameter,
 for example the reaching of a maximum execution time

### Parameters

| Name | Description    |
| ---- | -------------- |
| test | test condition |

### Returns

{@code true} to stop the processing loop, {@code false} to continue as usual


map(function.Function<? super I,? extends J> map)
-------------------------------------------------
transforms the item read from the source before offering it to the main processing loop.

### Parameters

| Name | Description             |
| ---- | ----------------------- |
| map  | transformation function |
| J    | new item type           |

### Returns

new instance of {@link LoopSource}


