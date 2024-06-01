Interface LoopSourceStd implements io.github.epi155.pm.batch.step.ParallelLoop0<I>
==================================================================================
interface to manage the elements of a single input resource.
 


 it is possible to process the input immediately or to define one or more
 output resources where the input processing can be sent

io.github.epi155.pm.batch.step.LoopSourceStd Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Return type                                                             |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| **public abstract** | [forEach(function.Consumer<? super I> action)](#foreachfunctionconsumer?-super-i-action)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | void                                                                    |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T,O> sink)](#intoiogithubepi155pmbatchstepsinkresourcet-o-sink)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | io.github.epi155.pm.batch.step.IterableLoop<I,O>                        |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | io.github.epi155.pm.batch.step.IterableLoop2<I,O1,O2>                   |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | io.github.epi155.pm.batch.step.IterableLoop3<I,O1,O2,O3>                |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4)                                                                                                                                                                                                                                                                                                                                                                                                                                             | io.github.epi155.pm.batch.step.IterableLoop4<I,O1,O2,O3,O4>             |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5)                                                                                                                                                                                                                                                                                                                                  | io.github.epi155.pm.batch.step.IterableLoop5<I,O1,O2,O3,O4,O5>          |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6)                                                                                                                                                                                                                       | io.github.epi155.pm.batch.step.IterableLoop6<I,O1,O2,O3,O4,O5,O6>       |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6-iogithubepi155pmbatchstepsinkresourcet7-o7-sink7)                                                                                                            | io.github.epi155.pm.batch.step.IterableLoop7<I,O1,O2,O3,O4,O5,O6,O7>    |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7, io.github.epi155.pm.batch.step.SinkResource<T8,O8> sink8)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6-iogithubepi155pmbatchstepsinkresourcet7-o7-sink7-iogithubepi155pmbatchstepsinkresourcet8-o8-sink8) | io.github.epi155.pm.batch.step.IterableLoop8<I,O1,O2,O3,O4,O5,O6,O7,O8> |
| **public abstract** | [shutdownTimeout(long time, TimeUnit unit)](#shutdowntimeoutlong-time-timeunit-unit)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | io.github.epi155.pm.batch.step.ParallelLoop0<I>                         |

Methods
=======
forEach(function.Consumer<? super I> action)
--------------------------------------------
Performs the specified action for each item taken from the source until all items have been processed or the
 action throws an exception.

### Parameters

| Name   | Description                                 |
| ------ | ------------------------------------------- |
| action | The action to be performed for each element |


into(io.github.epi155.pm.batch.step.SinkResource<T,O> sink)
-----------------------------------------------------------
sets the sink resource
 <pre>
 Pgm.from(source)<b>.into(sink)</b>.forEach(src -&gt; ...)
 </pre>

### Parameters

| Name | Description        |
| ---- | ------------------ |
| sink | the sink resource  |
| T    | sink resource type |
| O    | sink element type  |

### Returns

instance of {@link IterableLoop}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2)
------------------------------------------------------------------------------------------------------------------------
set 2 output resources
 <pre>
 Pgm.from(source)<b>.into(sink1,sink2)</b>.forEach(src -&gt; ...)
 </pre>

### Parameters

| Name  | Description            |
| ----- | ---------------------- |
| sink1 | 1st sink resource      |
| sink2 | 2nd sink resource      |
| T1    | 1st sink resource type |
| O1    | 1st sink element type  |
| T2    | 2nd sink resource type |
| O2    | 2nd sink element type  |

### Returns

instance of {@link IterableLoop2}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set 3 output resources
 <pre>
 Pgm.from(source)<b>.into(sink1,sink2,sink3)</b>.forEach(src -&gt; ...)
 </pre>

### Parameters

| Name  | Description            |
| ----- | ---------------------- |
| sink1 | 1st sink resource      |
| sink2 | 2nd sink resource      |
| sink3 | 3rd sink resource      |
| T1    | 1st sink resource type |
| O1    | 1st sink element type  |
| T2    | 2nd sink resource type |
| O2    | 2nd sink element type  |
| T3    | 3rd sink resource type |
| O3    | 3rd sink element type  |

### Returns

instance of {@link IterableLoop3}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set 4 output resources
 <pre>
 Pgm.from(source)<b>.into(sink1,sink2,sink3,sink4)</b>.forEach(src -&gt; ...)
 </pre>

### Parameters

| Name  | Description            |
| ----- | ---------------------- |
| sink1 | 1st sink resource      |
| sink2 | 2nd sink resource      |
| sink3 | 3rd sink resource      |
| sink4 | 4th sink resource      |
| T1    | 1st sink resource type |
| O1    | 1st sink element type  |
| T2    | 2nd sink resource type |
| O2    | 2nd sink element type  |
| T3    | 3rd sink resource type |
| O3    | 3rd sink element type  |
| T4    | 4th sink resource type |
| O4    | 4th sink element type  |

### Returns

instance of {@link IterableLoop4}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5)
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set 5 output resources
 <pre>
 Pgm.from(source)<b>.into(sink1,sink2,sink3,sink4,sink5)</b>.forEach(src -&gt; ...)
 </pre>

### Parameters

| Name  | Description            |
| ----- | ---------------------- |
| sink1 | 1st sink resource      |
| sink2 | 2nd sink resource      |
| sink3 | 3rd sink resource      |
| sink4 | 4th sink resource      |
| sink5 | 5th sink resource      |
| T1    | 1st sink resource type |
| O1    | 1st sink element type  |
| T2    | 2nd sink resource type |
| O2    | 2nd sink element type  |
| T3    | 3rd sink resource type |
| O3    | 3rd sink element type  |
| T4    | 4th sink resource type |
| O4    | 4th sink element type  |
| T5    | 5th sink resource type |
| O5    | 5th sink element type  |

### Returns

instance of {@link IterableLoop5}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set 6 output resources
 <pre>
 Pgm.from(source)<b>.into(sink1,sink2,sink3,sink4,sink5,sink6)</b>.forEach(src -&gt; ...)
 </pre>

### Parameters

| Name  | Description            |
| ----- | ---------------------- |
| sink1 | 1st sink resource      |
| sink2 | 2nd sink resource      |
| sink3 | 3rd sink resource      |
| sink4 | 4th sink resource      |
| sink5 | 5th sink resource      |
| sink6 | 6th sink resource      |
| T1    | 1st sink resource type |
| O1    | 1st sink element type  |
| T2    | 2nd sink resource type |
| O2    | 2nd sink element type  |
| T3    | 3rd sink resource type |
| O3    | 3rd sink element type  |
| T4    | 4th sink resource type |
| O4    | 4th sink element type  |
| T5    | 5th sink resource type |
| O5    | 5th sink element type  |
| T6    | 6th sink resource type |
| O6    | 6th sink element type  |

### Returns

instance of {@link IterableLoop6}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set 7 output resources
 <pre>
 Pgm.from(source)<b>.into(sink1,sink2,sink3,sink4,sink5,sink6,sink7)</b>.forEach(src -&gt; ...)
 </pre>

### Parameters

| Name  | Description            |
| ----- | ---------------------- |
| sink1 | 1st sink resource      |
| sink2 | 2nd sink resource      |
| sink3 | 3rd sink resource      |
| sink4 | 4th sink resource      |
| sink5 | 5th sink resource      |
| sink6 | 6th sink resource      |
| sink7 | 7th sink resource      |
| T1    | 1st sink resource type |
| O1    | 1st sink element type  |
| T2    | 2nd sink resource type |
| O2    | 2nd sink element type  |
| T3    | 3rd sink resource type |
| O3    | 3rd sink element type  |
| T4    | 4th sink resource type |
| O4    | 4th sink element type  |
| T5    | 5th sink resource type |
| O5    | 5th sink element type  |
| T6    | 6th sink resource type |
| O6    | 6th sink element type  |
| T7    | 7th sink resource type |
| O7    | 7th sink element type  |

### Returns

instance of {@link IterableLoop7}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7, io.github.epi155.pm.batch.step.SinkResource<T8,O8> sink8)
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
set 8 output resources
 <pre>
 Pgm.from(source)<b>.into(sink1,sink2,sink3,sink4,sink5,sink6,sink7,sink8)</b>.forEach(src -&gt; ...)
 </pre>

### Parameters

| Name  | Description            |
| ----- | ---------------------- |
| sink1 | 1st sink resource      |
| sink2 | 2nd sink resource      |
| sink3 | 3rd sink resource      |
| sink4 | 4th sink resource      |
| sink5 | 5th sink resource      |
| sink6 | 6th sink resource      |
| sink7 | 7th sink resource      |
| sink8 | 8th sink resource      |
| T1    | 1st sink resource type |
| O1    | 1st sink element type  |
| T2    | 2nd sink resource type |
| O2    | 2nd sink element type  |
| T3    | 3rd sink resource type |
| O3    | 3rd sink element type  |
| T4    | 4th sink resource type |
| O4    | 4th sink element type  |
| T5    | 5th sink resource type |
| O5    | 5th sink element type  |
| T6    | 6th sink resource type |
| O6    | 6th sink element type  |
| T7    | 7th sink resource type |
| O7    | 7th sink element type  |
| T8    | 8th sink resource type |
| O8    | 8th sink element type  |

### Returns

instance of {@link IterableLoop8}


shutdownTimeout(long time, TimeUnit unit)
-----------------------------------------
sets the shutdown timeout for parallel processing

### Parameters

| Name | Description |
| ---- | ----------- |
| time | time amount |
| unit | time unit   |

### Returns

instance of {@link ParallelLoop0} for run parallel processing


