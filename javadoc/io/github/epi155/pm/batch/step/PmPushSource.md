Class PmPushSource implements io.github.epi155.pm.batch.step.LoopSource<I>
==========================================================================


io.github.epi155.pm.batch.step.PmPushSource Summary
-------
#### Constructors
| Visibility | Signature                                                        |
| ---------- | ---------------------------------------------------------------- |
|            | PmPushSource(io.github.epi155.pm.batch.step.SourceResource<S,I>) |
#### Fields
| Modifiers                | Field name                                                         | Type                                               |
| ------------------------ | ------------------------------------------------------------------ | -------------------------------------------------- |
| **private static final** | [JOB_NAME](#javalangstring-job_name)                               | String                                             |
| **private static final** | [STEP_NAME](#javalangstring-step_name)                             | String                                             |
| **protected final**      | [source](#iogithubepi155pmbatchstepsourceresources-i-source)       | io.github.epi155.pm.batch.step.SourceResource<S,I> |
| **private**              | [beforeAction](#javalangrunnable-beforeaction)                     | Runnable                                           |
| **private**              | [time](#long-time)                                                 | long                                               |
| **private**              | [unit](#javautilconcurrenttimeunit-unit)                           | concurrent.TimeUnit                                |
| **private**              | [terminateTest](#javautilfunctionpredicate?-super-i-terminatetest) | function.Predicate<? super I>                      |
#### Methods
| Modifiers   | Method signature                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Return type                                                             |
| ----------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| **private** | [consumerOf(io.github.epi155.pm.batch.step.SinkResource<T,O> sink, T t)](#consumerofiogithubepi155pmbatchstepsinkresourcet-o-sink-t-t)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | function.Consumer<? super O>                                            |
| **public**  | [terminate(function.Predicate<? super I> test)](#terminatefunctionpredicate?-super-i-test)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | io.github.epi155.pm.batch.step.LoopSourceLayer<I>                       |
| **public**  | [before(Runnable action)](#beforerunnable-action)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | io.github.epi155.pm.batch.step.LoopSourceStd<I>                         |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T,O> sink)](#intoiogithubepi155pmbatchstepsinkresourcet-o-sink)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | io.github.epi155.pm.batch.step.IterableLoop<I,O>                        |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | io.github.epi155.pm.batch.step.IterableLoop2<I,O1,O2>                   |
| **private** | [setShutdownTimeout(long time, TimeUnit unit)](#setshutdowntimeoutlong-time-timeunit-unit)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | void                                                                    |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | io.github.epi155.pm.batch.step.IterableLoop3<I,O1,O2,O3>                |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4)                                                                                                                                                                                                                                                                                                                                                                                                                                             | io.github.epi155.pm.batch.step.IterableLoop4<I,O1,O2,O3,O4>             |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5)                                                                                                                                                                                                                                                                                                                                  | io.github.epi155.pm.batch.step.IterableLoop5<I,O1,O2,O3,O4,O5>          |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6)                                                                                                                                                                                                                       | io.github.epi155.pm.batch.step.IterableLoop6<I,O1,O2,O3,O4,O5,O6>       |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6-iogithubepi155pmbatchstepsinkresourcet7-o7-sink7)                                                                                                            | io.github.epi155.pm.batch.step.IterableLoop7<I,O1,O2,O3,O4,O5,O6,O7>    |
| **public**  | [into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4, io.github.epi155.pm.batch.step.SinkResource<T5,O5> sink5, io.github.epi155.pm.batch.step.SinkResource<T6,O6> sink6, io.github.epi155.pm.batch.step.SinkResource<T7,O7> sink7, io.github.epi155.pm.batch.step.SinkResource<T8,O8> sink8)](#intoiogithubepi155pmbatchstepsinkresourcet1-o1-sink1-iogithubepi155pmbatchstepsinkresourcet2-o2-sink2-iogithubepi155pmbatchstepsinkresourcet3-o3-sink3-iogithubepi155pmbatchstepsinkresourcet4-o4-sink4-iogithubepi155pmbatchstepsinkresourcet5-o5-sink5-iogithubepi155pmbatchstepsinkresourcet6-o6-sink6-iogithubepi155pmbatchstepsinkresourcet7-o7-sink7-iogithubepi155pmbatchstepsinkresourcet8-o8-sink8) | io.github.epi155.pm.batch.step.IterableLoop8<I,O1,O2,O3,O4,O5,O6,O7,O8> |
| **public**  | [shutdownTimeout(long time, TimeUnit unit)](#shutdowntimeoutlong-time-timeunit-unit)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | io.github.epi155.pm.batch.step.ParallelLoop0<I>                         |
| **public**  | [forEach(function.Consumer<? super I> action)](#foreachfunctionconsumer?-super-i-action)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | void                                                                    |
| **public**  | [forEachParallel(int maxThread, function.Consumer<? super I> action)](#foreachparallelint-maxthread-functionconsumer?-super-i-action)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | void                                                                    |
| **public**  | [forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<Void>> asyncTransformer)](#foreachasyncint-maxthread-functionfunction?-super-i-?-extends-concurrentfuturevoid-asynctransformer)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | void                                                                    |
| **private** | [shutdown(ExecutorService pool)](#shutdownexecutorservice-pool)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | void                                                                    |
| **private** | [monitor(Thread main, List<concurrent.Future<?>> statuses)](#monitorthread-main-listconcurrentfuture?-statuses)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | void                                                                    |
| **private** | [probeStatuses(List<concurrent.Future<?>> statuses, boolean hot)](#probestatuseslistconcurrentfuture?-statuses-boolean-hot)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | void                                                                    |
| **private** | [sendMessage(int j, int r)](#sendmessageint-j-int-r)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | void                                                                    |
| **private** | [iterateStatus(Iterator<concurrent.Future<?>> it)](#iteratestatusiteratorconcurrentfuture?-it)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | int                                                                     |

Fields
======
String JOB_NAME
-------------------------
*No description provided*


String STEP_NAME
--------------------------
*No description provided*


io.github.epi155.pm.batch.step.SourceResource<S,I> source
---------------------------------------------------------
*No description provided*


Runnable beforeAction
-------------------------------
*No description provided*


long time
---------
*No description provided*


concurrent.TimeUnit unit
----------------------------------
*No description provided*


function.Predicate<? super I> terminateTest
-----------------------------------------------------
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

terminate(function.Predicate<? super I> test)
---------------------------------------------
### Overrides/Implements:
terminate(function.Predicate<? super I> test) from io.github.epi155.pm.batch.step.LoopSource

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| test | *No description provided* |

before(Runnable action)
-----------------------
*No method description provided*

### Parameters

| Name   | Description               |
| ------ | ------------------------- |
| action | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T,O> sink)
-----------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| sink | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2)
------------------------------------------------------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| sink1 | *No description provided* |
| sink2 | *No description provided* |

setShutdownTimeout(long time, TimeUnit unit)
--------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| time | *No description provided* |
| unit | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| sink1 | *No description provided* |
| sink2 | *No description provided* |
| sink3 | *No description provided* |

into(io.github.epi155.pm.batch.step.SinkResource<T1,O1> sink1, io.github.epi155.pm.batch.step.SinkResource<T2,O2> sink2, io.github.epi155.pm.batch.step.SinkResource<T3,O3> sink3, io.github.epi155.pm.batch.step.SinkResource<T4,O4> sink4)
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

shutdownTimeout(long time, TimeUnit unit)
-----------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| time | *No description provided* |
| unit | *No description provided* |

forEach(function.Consumer<? super I> action)
--------------------------------------------
*No method description provided*

### Parameters

| Name   | Description               |
| ------ | ------------------------- |
| action | *No description provided* |

forEachParallel(int maxThread, function.Consumer<? super I> action)
-------------------------------------------------------------------
*No method description provided*

### Parameters

| Name      | Description               |
| --------- | ------------------------- |
| maxThread | *No description provided* |
| action    | *No description provided* |

forEachAsync(int maxThread, function.Function<? super I,? extends concurrent.Future<Void>> asyncTransformer)
------------------------------------------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name             | Description               |
| ---------------- | ------------------------- |
| maxThread        | *No description provided* |
| asyncTransformer | *No description provided* |

shutdown(ExecutorService pool)
------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| pool | *No description provided* |

monitor(Thread main, List<concurrent.Future<?>> statuses)
---------------------------------------------------------
monitors list of future's writer listener (invoked from task scheduler)

### Parameters

| Name     | Description                                           |
| -------- | ----------------------------------------------------- |
| main     | main thread (to be interrupted on listener exception) |
| statuses | list of futures                                       |


probeStatuses(List<concurrent.Future<?>> statuses, boolean hot)
---------------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| statuses | *No description provided* |
| hot      | *No description provided* |

sendMessage(int j, int r)
-------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| j    | *No description provided* |
| r    | *No description provided* |

iterateStatus(Iterator<concurrent.Future<?>> it)
------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| it   | *No description provided* |

