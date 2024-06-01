Class PmQueueWriter implements java.io.Closeable
================================================


io.github.epi155.pm.batch.step.PmQueueWriter Summary
-------
#### Constructors
| Visibility | Signature                                                                                                                  |
| ---------- | -------------------------------------------------------------------------------------------------------------------------- |
| private    | PmQueueWriter(java.util.concurrent.BlockingQueue<io.github.epi155.pm.batch.step.Tuple1<T>>,java.util.concurrent.Future<?>) |
#### Fields
| Modifiers                | Field name                                                                      | Type                                                               |
| ------------------------ | ------------------------------------------------------------------------------- | ------------------------------------------------------------------ |
| **private static final** | [JOB_NAME](#javalangstring-job_name)                                            | String                                                             |
| **private static final** | [STEP_NAME](#javalangstring-step_name)                                          | String                                                             |
| **private final**        | [queue](#javautilconcurrentblockingqueueiogithubepi155pmbatchsteptuple1t-queue) | concurrent.BlockingQueue<io.github.epi155.pm.batch.step.Tuple1<T>> |
| **private final**        | [future](#javautilconcurrentfuture?-future)                                     | concurrent.Future<?>                                               |
#### Methods
| Modifiers   | Method signature                                                                                                                                                                               | Return type                                     |
| ----------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------- |
| **static**  | [of(int maxThread, ExecutorService pool, io.github.epi155.pm.batch.step.SinkResource<T,O> sink, T t)](#ofint-maxthread-executorservice-pool-iogithubepi155pmbatchstepsinkresourcet-o-sink-t-t) | io.github.epi155.pm.batch.step.PmQueueWriter<O> |
| **public**  | [write(T t)](#writet-t)                                                                                                                                                                        | void                                            |
| **private** | [put(io.github.epi155.pm.batch.step.Tuple1<T> t1)](#putiogithubepi155pmbatchsteptuple1t-t1)                                                                                                    | void                                            |
| **public**  | [close()](#close)                                                                                                                                                                              | void                                            |

Fields
======
String JOB_NAME
-------------------------
*No description provided*


String STEP_NAME
--------------------------
*No description provided*


concurrent.BlockingQueue<io.github.epi155.pm.batch.step.Tuple1<T>> queue
----------------------------------------------------------------------------------
*No description provided*


concurrent.Future<?> future
-------------------------------------
*No description provided*


Methods
=======
of(int maxThread, ExecutorService pool, io.github.epi155.pm.batch.step.SinkResource<T,O> sink, T t)
---------------------------------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name      | Description               |
| --------- | ------------------------- |
| maxThread | *No description provided* |
| pool      | *No description provided* |
| sink      | *No description provided* |
| t         | *No description provided* |

write(T t)
----------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| t    | *No description provided* |

put(io.github.epi155.pm.batch.step.Tuple1<T> t1)
------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| t1   | *No description provided* |

close()
-------
### Overrides/Implements:
close() from java.io.Closeable

*No method description provided*


