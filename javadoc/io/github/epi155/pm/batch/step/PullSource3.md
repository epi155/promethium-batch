Interface PullSource3
=====================
interface to manage the elements of three input resources.
 


 unlike the single source, where it is possible to push the data towards the processing process, in the case of
 multiple sources it is the processing process that decides from which source, and when, to pull the data to be
 processed

io.github.epi155.pm.batch.step.PullSource3 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Return type                                                                     |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T,O> sink)](#intoiogithubepi155pmbatchstepsinkresourcet-o-sink)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | io.github.epi155.pm.batch.step.PullProcess3o1<I1,I2,I3,O>                       |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | io.github.epi155.pm.batch.step.PullProcess3o2<I1,I2,I3,O1,O2>                   |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | io.github.epi155.pm.batch.step.PullProcess3o3<I1,I2,I3,O1,O2,O3>                |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4)                                                                                                                                                                                                                                                                                                                                                                                                                                             | io.github.epi155.pm.batch.step.PullProcess3o4<I1,I2,I3,O1,O2,O3,O4>             |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5)                                                                                                                                                                                                                                                                                                                                  | io.github.epi155.pm.batch.step.PullProcess3o5<I1,I2,I3,O1,O2,O3,O4,O5>          |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6)                                                                                                                                                                                                                       | io.github.epi155.pm.batch.step.PullProcess3o6<I1,I2,I3,O1,O2,O3,O4,O5,O6>       |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6-iogithubepi155pmbatchstepsinkresourcet7-o7-sink7)                                                                                                            | io.github.epi155.pm.batch.step.PullProcess3o7<I1,I2,I3,O1,O2,O3,O4,O5,O6,O7>    |
| **public abstract** | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7, io.github.epi155.pm.batch.step.SinkResource<T8,O8> sink8)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6-iogithubepi155pmbatchstepsinkresourcet7-o7-sink7-iogithubepi155pmbatchstepsinkresourcet8-o8-sink8) | io.github.epi155.pm.batch.step.PullProcess3o8<I1,I2,I3,O1,O2,O3,O4,O5,O6,O7,O8> |
| **public abstract** | [proceed(io.github.epi155.pm.batch.step.PullWorker3o0<I1,I2,I3> worker)](#proceediogithubepi155pmbatchsteppullworker3o0i1-i2-i3-worker)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | void                                                                            |

Methods
=======
into(io.github.epi155.pm.batch.step.SinkResource<T,O> sink)
-----------------------------------------------------------
sets the sink resource to process the data

### Parameters

| Name | Description        |
| ---- | ------------------ |
| sink | the sink resource  |
| T    | sink resource type |
| O    | sink element type  |

### Returns

instance of {@link PullProcess3o1}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2)
------------------------------------------------------------------------------------------------------------------------
sets the sinks resource to process the data

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

instance of {@link PullProcess3o2}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
sets the sinks resource to process the data

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

instance of {@link PullProcess3o3}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
sets the sinks resource to process the data

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

instance of {@link PullProcess3o4}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5)
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
sets the sinks resource to process the data

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

instance of {@link PullProcess3o5}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
sets the sinks resource to process the data

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

instance of {@link PullProcess3o6}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
sets the sinks resource to process the data

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

instance of {@link PullProcess3o7}


into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7, io.github.epi155.pm.batch.step.SinkResource<T8,O8> sink8)
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
sets the sinks resource to process the data

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

instance of {@link PullProcess3o8}


proceed(io.github.epi155.pm.batch.step.PullWorker3o0<I1,I2,I3> worker)
----------------------------------------------------------------------
processes the data
 <pre>
 Pgm.from(src1, src2, src3).proceed((rd1, rd2, rd3) -> {
     val i1 = rd1.get();
     val i2 = rd2.get();
     val i3 = rd3.get();
     ...
 });
 </pre>

### Parameters

| Name   | Description                      |
| ------ | -------------------------------- |
| worker | worker who read the input values |


