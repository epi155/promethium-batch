Class PmPullSource3 implements io.github.epi155.pm.batch.step.PullSource3<I1,I2,I3>
===================================================================================


io.github.epi155.pm.batch.step.PmPullSource3 Summary
-------
#### Constructors
| Visibility | Signature                                                                                                                                                                     |
| ---------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| public     | PmPullSource3(io.github.epi155.pm.batch.step.SourceResource<S1,I1>,io.github.epi155.pm.batch.step.SourceResource<S2,I2>,io.github.epi155.pm.batch.step.SourceResource<S3,I3>) |
#### Fields
| Modifiers         | Field name                                                       | Type                                                 |
| ----------------- | ---------------------------------------------------------------- | ---------------------------------------------------- |
| **private final** | [source1](#iogithubepi155pmbatchstepsourceresources1-i1-source1) | io.github.epi155.pm.batch.step.SourceResource<S1,I1> |
| **private final** | [source2](#iogithubepi155pmbatchstepsourceresources2-i2-source2) | io.github.epi155.pm.batch.step.SourceResource<S2,I2> |
| **private final** | [source3](#iogithubepi155pmbatchstepsourceresources3-i3-source3) | io.github.epi155.pm.batch.step.SourceResource<S3,I3> |
#### Methods
| Modifiers   | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Return type                                                                     |
| ----------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- |
| **private** | [consumerOf(io.github.epi155.pm.batch.step.SinkResource<T,O> sink, T t)](#consumerofiogithubepi155pmbatchstepsinkresourcet-o-sink-t-t)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | function.Consumer<? super O>                                                    |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T,O> sink)](#intoiogithubepi155pmbatchstepsinkresourcet-o-sink)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | io.github.epi155.pm.batch.step.PullProcess3o1<I1,I2,I3,O>                       |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | io.github.epi155.pm.batch.step.PullProcess3o2<I1,I2,I3,O1,O2>                   |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | io.github.epi155.pm.batch.step.PullProcess3o3<I1,I2,I3,O1,O2,O3>                |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4)                                                                                                                                                                                                                                                                                                                                                                                                                                             | io.github.epi155.pm.batch.step.PullProcess3o4<I1,I2,I3,O1,O2,O3,O4>             |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5)                                                                                                                                                                                                                                                                                                                                  | io.github.epi155.pm.batch.step.PullProcess3o5<I1,I2,I3,O1,O2,O3,O4,O5>          |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6)                                                                                                                                                                                                                       | io.github.epi155.pm.batch.step.PullProcess3o6<I1,I2,I3,O1,O2,O3,O4,O5,O6>       |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6-iogithubepi155pmbatchstepsinkresourcet7-o7-sink7)                                                                                                            | io.github.epi155.pm.batch.step.PullProcess3o7<I1,I2,I3,O1,O2,O3,O4,O5,O6,O7>    |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7, io.github.epi155.pm.batch.step.SinkResource<T8,O8> sink8)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6-iogithubepi155pmbatchstepsinkresourcet7-o7-sink7-iogithubepi155pmbatchstepsinkresourcet8-o8-sink8) | io.github.epi155.pm.batch.step.PullProcess3o8<I1,I2,I3,O1,O2,O3,O4,O5,O6,O7,O8> |
| **public**  | [proceed(io.github.epi155.pm.batch.step.PullWorker3o0<I1,I2,I3> worker)](#proceediogithubepi155pmbatchsteppullworker3o0i1-i2-i3-worker)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | void                                                                            |

Fields
======
io.github.epi155.pm.batch.step.SourceResource<S1,I1> source1
------------------------------------------------------------
*No description provided*


io.github.epi155.pm.batch.step.SourceResource<S2,I2> source2
------------------------------------------------------------
*No description provided*


io.github.epi155.pm.batch.step.SourceResource<S3,I3> source3
------------------------------------------------------------
*No description provided*


Methods
=======
consumerOf(io.github.epi155.pm.batch.step.SinkResource<T,O> sink, T t)
----------------------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| sink | *No description provided* |
| t    | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T,O> sink)
-----------------------------------------------------------
### Overrides/Implements:
into(io.github.epi155.pm.batch.step.SinkResource<T,O> sink) from io.github.epi155.pm.batch.step.PullSource3

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| sink | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2)
------------------------------------------------------------------------------------------------------------------------
### Overrides/Implements:
into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2) from io.github.epi155.pm.batch.step.PullSource3

*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| sink1 | *No description provided* |
| sink2 | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
### Overrides/Implements:
into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3) from io.github.epi155.pm.batch.step.PullSource3

*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| sink1 | *No description provided* |
| sink2 | *No description provided* |
| sink3 | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
### Overrides/Implements:
into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4) from io.github.epi155.pm.batch.step.PullSource3

*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| sink1 | *No description provided* |
| sink2 | *No description provided* |
| sink3 | *No description provided* |
| sink4 | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5)
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
### Overrides/Implements:
into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5) from io.github.epi155.pm.batch.step.PullSource3

*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| sink1 | *No description provided* |
| sink2 | *No description provided* |
| sink3 | *No description provided* |
| sink4 | *No description provided* |
| sink5 | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
### Overrides/Implements:
into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6) from io.github.epi155.pm.batch.step.PullSource3

*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| sink1 | *No description provided* |
| sink2 | *No description provided* |
| sink3 | *No description provided* |
| sink4 | *No description provided* |
| sink5 | *No description provided* |
| sink6 | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
### Overrides/Implements:
into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7) from io.github.epi155.pm.batch.step.PullSource3

*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| sink1 | *No description provided* |
| sink2 | *No description provided* |
| sink3 | *No description provided* |
| sink4 | *No description provided* |
| sink5 | *No description provided* |
| sink6 | *No description provided* |
| sink7 | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7, io.github.epi155.pm.batch.step.SinkResource<T8,O8> sink8)
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
### Overrides/Implements:
into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7, io.github.epi155.pm.batch.step.SinkResource<T8,O8> sink8) from io.github.epi155.pm.batch.step.PullSource3

*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| sink1 | *No description provided* |
| sink2 | *No description provided* |
| sink3 | *No description provided* |
| sink4 | *No description provided* |
| sink5 | *No description provided* |
| sink6 | *No description provided* |
| sink7 | *No description provided* |
| sink8 | *No description provided* |

proceed(io.github.epi155.pm.batch.step.PullWorker3o0<I1,I2,I3> worker)
----------------------------------------------------------------------
### Overrides/Implements:
proceed(io.github.epi155.pm.batch.step.PullWorker3o0<I1,I2,I3> worker) from io.github.epi155.pm.batch.step.PullSource3

*No method description provided*

### Parameters

| Name   | Description               |
| ------ | ------------------------- |
| worker | *No description provided* |
