Class PmJob implements io.github.epi155.pm.batch.job.JobStatus
==============================================================


io.github.epi155.pm.batch.job.PmJob Summary
-------
#### Constructors
| Visibility | Signature                                                      |
| ---------- | -------------------------------------------------------------- |
| private    | PmJob(int,io.github.epi155.pm.batch.job.JCL,java.lang.String)  |
| protected  | PmJob(java.lang.String,io.github.epi155.pm.batch.job.JobTrace) |
#### Fields
| Modifiers                | Field name                                                                            | Type                                   |
| ------------------------ | ------------------------------------------------------------------------------------- | -------------------------------------- |
| **private static final** | [JOB_NAME](#javalangstring-job_name)                                                  | String                                 |
| **private static final** | [STEP_NAME](#javalangstring-step_name)                                                | String                                 |
| **private static final** | [BG](#javalangstring-bg)                                                              | String                                 |
| **private static final** | [isBackground](#javalangthreadlocaljavalangboolean-isbackground)                      | ThreadLocal<Boolean>                   |
| **private static final** | [UNHANDLED_ERROR](#javalangstring-unhandled_error)                                    | String                                 |
| **private final**        | [jcl](#iogithubepi155pmbatchjobjcl-jcl)                                               | io.github.epi155.pm.batch.job.JCL      |
| **private final**        | [jobName](#javalangstring-jobname)                                                    | String                                 |
| **private final**        | [stack](#javautildequejavalanginteger-stack)                                          | Deque<Integer>                         |
| **private final**        | [jobCount](#iogithubepi155pmbatchjobjobcount-jobcount)                                | io.github.epi155.pm.batch.job.JobCount |
| **private final**        | [jobTrace](#iogithubepi155pmbatchjobjobtrace-jobtrace)                                | io.github.epi155.pm.batch.job.JobTrace |
| **private final**        | [executorService](#javautilconcurrentexecutorservice-executorservice)                 | concurrent.ExecutorService             |
| **private final**        | [futures](#javautilmapjavalangstring-javautilconcurrentfuturejavalanginteger-futures) | Map<String,concurrent.Future<Integer>> |
| **private**              | [maxcc](#int-maxcc)                                                                   | int                                    |
| **private**              | [lastcc](#javalanginteger-lastcc)                                                     | Integer                                |
#### Methods
| Modifiers     | Method signature                                                                                                                                                                     | Return type                                                                      |
| ------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------- |
| **static**    | [of(int rc, JCL jcl, String jobName)](#ofint-rc-jcl-jcl-string-jobname)                                                                                                              | io.github.epi155.pm.batch.job.PmJob                                              |
| **public**    | [push()](#push)                                                                                                                                                                      | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [pop()](#pop)                                                                                                                                                                        | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [peek()](#peek)                                                                                                                                                                      | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [returnCode(String stepName)](#returncodestring-stepname)                                                                                                                            | Optional<Integer>                                                                |
| **public**    | [forEachProc(P qs, function.Function<Q,String> name, io.github.epi155.pm.batch.job.Proc<Q> proc)](#foreachprocp-qs-functionfunctionq-string-name-iogithubepi155pmbatchjobprocq-proc) | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [execProc(P p, String procName, io.github.epi155.pm.batch.job.Proc<P> proc)](#execprocp-p-string-procname-iogithubepi155pmbatchjobprocp-proc)                                        | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [execProc(String procName, io.github.epi155.pm.batch.job.Proc<Void> proc)](#execprocstring-procname-iogithubepi155pmbatchjobprocvoid-proc)                                           | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [cond(int cc, Cond cond)](#condint-cc-cond-cond)                                                                                                                                     | io.github.epi155.pm.batch.job.JobAction<io.github.epi155.pm.batch.job.JobStatus> |
| **public**    | [cond(int cc, Cond cond, String stepName)](#condint-cc-cond-cond-string-stepname)                                                                                                    | io.github.epi155.pm.batch.job.JobAction<io.github.epi155.pm.batch.job.JobStatus> |
| **public**    | [exec(function.Consumer<io.github.epi155.pm.batch.job.JobStatus> action)](#execfunctionconsumeriogithubepi155pmbatchjobjobstatus-action)                                             | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [maxcc()](#maxcc)                                                                                                                                                                    | int                                                                              |
| **public**    | [lastcc()](#lastcc)                                                                                                                                                                  | Integer                                                                          |
| **public**    | [complete()](#complete)                                                                                                                                                              | int                                                                              |
| **public**    | [done()](#done)                                                                                                                                                                      | io.github.epi155.pm.batch.job.JobInfo                                            |
| **private**   | [joinResult(concurrent.Future<Integer> ele)](#joinresultconcurrentfutureinteger-ele)                                                                                                 | int                                                                              |
| **private**   | [sendMessage(int j, int r)](#sendmessageint-j-int-r)                                                                                                                                 | void                                                                             |
| **protected** | [runStep(StatsCount c, IntSupplier step)](#runstepstatscount-c-intsupplier-step)                                                                                                     | int                                                                              |
| **protected** | [maxcc(int returnCode)](#maxccint-returncode)                                                                                                                                        | void                                                                             |
| **private**   | [fullName(String name)](#fullnamestring-name)                                                                                                                                        | String                                                                           |
| **public**    | [join()](#join)                                                                                                                                                                      | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [join(String name)](#joinstring-name)                                                                                                                                                | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [quit(String name)](#quitstring-name)                                                                                                                                                | io.github.epi155.pm.batch.job.JobStatus                                          |
| **private**   | [loopOnFutures(Iterator<Map.Entry<String,concurrent.Future<Integer>>> it, int k)](#looponfuturesiteratormapentrystring-concurrentfutureinteger-it-int-k)                             | Integer                                                                          |
| **private**   | [add(String name, int rc, Instant tiStart, Instant tiEnd, Throwable error)](#addstring-name-int-rc-instant-tistart-instant-tiend-throwable-error)                                    | void                                                                             |
| **private**   | [add(String name, int rc, Instant tiStart, Instant tiEnd)](#addstring-name-int-rc-instant-tistart-instant-tiend)                                                                     | void                                                                             |
| **private**   | [add(String cmd, int rc)](#addstring-cmd-int-rc)                                                                                                                                     | void                                                                             |
| **private**   | [add(String cmd)](#addstring-cmd)                                                                                                                                                    | void                                                                             |
| **protected** | [wrapper(StatsCount c, IntSupplier step)](#wrapperstatscount-c-intsupplier-step)                                                                                                     | void                                                                             |
| **public**    | [execPgm(P p, C c, function.BiFunction<P,C,Integer> pgm)](#execpgmp-p-c-c-functionbifunctionp-c-integer-pgm)                                                                         | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [execPgm(P p, String stepName, function.ToIntFunction<P> pgm)](#execpgmp-p-string-stepname-functiontointfunctionp-pgm)                                                               | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [execPgm(C c, function.ToIntFunction<C> pgm)](#execpgmc-c-functiontointfunctionc-pgm)                                                                                                | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [execPgm(String stepName, IntSupplier pgm)](#execpgmstring-stepname-intsupplier-pgm)                                                                                                 | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [execPgm(P p, C c, function.BiConsumer<P,C> pgm)](#execpgmp-p-c-c-functionbiconsumerp-c-pgm)                                                                                         | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [execPgm(P p, String stepName, function.Consumer<P> pgm)](#execpgmp-p-string-stepname-functionconsumerp-pgm)                                                                         | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [execPgm(C c, function.Consumer<C> pgm)](#execpgmc-c-functionconsumerc-pgm)                                                                                                          | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [execPgm(String stepName, Runnable pgm)](#execpgmstring-stepname-runnable-pgm)                                                                                                       | io.github.epi155.pm.batch.job.JobStatus                                          |
| **private**   | [submit(String stepName, concurrent.Callable<Integer> step)](#submitstring-stepname-concurrentcallableinteger-step)                                                                  | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forkPgm(P p, C c, function.BiFunction<P,C,Integer> pgm)](#forkpgmp-p-c-c-functionbifunctionp-c-integer-pgm)                                                                         | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forkPgm(P p, String stepName, function.ToIntFunction<P> pgm)](#forkpgmp-p-string-stepname-functiontointfunctionp-pgm)                                                               | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forkPgm(C c, function.ToIntFunction<C> pgm)](#forkpgmc-c-functiontointfunctionc-pgm)                                                                                                | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forkPgm(String stepName, IntSupplier pgm)](#forkpgmstring-stepname-intsupplier-pgm)                                                                                                 | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forkPgm(P p, C c, function.BiConsumer<P,C> pgm)](#forkpgmp-p-c-c-functionbiconsumerp-c-pgm)                                                                                         | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forkPgm(P p, String stepName, function.Consumer<P> pgm)](#forkpgmp-p-string-stepname-functionconsumerp-pgm)                                                                         | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forkPgm(C c, function.Consumer<C> pgm)](#forkpgmc-c-functionconsumerc-pgm)                                                                                                          | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forkPgm(String stepName, Runnable pgm)](#forkpgmstring-stepname-runnable-pgm)                                                                                                       | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forEachPgm(P p, function.Function<Q,C> c, function.BiFunction<Q,C,Integer> pgm)](#foreachpgmp-p-functionfunctionq-c-c-functionbifunctionq-c-integer-pgm)                            | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forEachPgm(P p, function.Function<Q,String> name, function.ToIntFunction<Q> pgm)](#foreachpgmp-p-functionfunctionq-string-name-functiontointfunctionq-pgm)                          | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forEachPgm(P p, function.Function<Q,C> c, function.BiConsumer<Q,C> pgm)](#foreachpgmp-p-functionfunctionq-c-c-functionbiconsumerq-c-pgm)                                            | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forEachPgm(P p, function.Function<Q,String> name, function.Consumer<Q> pgm)](#foreachpgmp-p-functionfunctionq-string-name-functionconsumerq-pgm)                                    | io.github.epi155.pm.batch.job.JobStatus                                          |
| **public**    | [forkProc(P p, String procName, io.github.epi155.pm.batch.job.Proc<P> proc)](#forkprocp-p-string-procname-iogithubepi155pmbatchjobprocp-proc)                                        | io.github.epi155.pm.batch.job.JobStatus                                          |
| **private**   | [runProc(String procName, function.ToIntFunction<io.github.epi155.pm.batch.job.JobStatus> fcn)](#runprocstring-procname-functiontointfunctioniogithubepi155pmbatchjobjobstatus-fcn)  | Integer                                                                          |
| **public**    | [forkProc(String procName, io.github.epi155.pm.batch.job.Proc<Void> proc)](#forkprocstring-procname-iogithubepi155pmbatchjobprocvoid-proc)                                           | io.github.epi155.pm.batch.job.JobStatus                                          |

Fields
======
String JOB_NAME
-------------------------
*No description provided*


String STEP_NAME
--------------------------
*No description provided*


String BG
-------------------
*No description provided*


ThreadLocal<Boolean> isBackground
-----------------------------------------------------
*No description provided*


String UNHANDLED_ERROR
--------------------------------
*No description provided*


io.github.epi155.pm.batch.job.JCL jcl
-------------------------------------
*No description provided*


String jobName
------------------------
*No description provided*


Deque<Integer> stack
----------------------------------------
*No description provided*


io.github.epi155.pm.batch.job.JobCount jobCount
-----------------------------------------------
*No description provided*


io.github.epi155.pm.batch.job.JobTrace jobTrace
-----------------------------------------------
*No description provided*


concurrent.ExecutorService executorService
----------------------------------------------------
*No description provided*


Map<String,concurrent.Future<Integer>> futures
--------------------------------------------------------------------------------------
*No description provided*


int maxcc
---------
*No description provided*


Integer lastcc
------------------------
*No description provided*


Methods
=======
of(int rc, JCL jcl, String jobName)
-----------------------------------
*No method description provided*

### Parameters

| Name    | Description               |
| ------- | ------------------------- |
| rc      | *No description provided* |
| jcl     | *No description provided* |
| jobName | *No description provided* |

push()
------
### Overrides/Implements:
push() from io.github.epi155.pm.batch.job.JobStatus

*No method description provided*


pop()
-----
### Overrides/Implements:
pop() from io.github.epi155.pm.batch.job.JobStatus

*No method description provided*


peek()
------
### Overrides/Implements:
peek() from io.github.epi155.pm.batch.job.JobStatus

*No method description provided*


returnCode(String stepName)
---------------------------
### Overrides/Implements:
returnCode(String stepName) from io.github.epi155.pm.batch.job.JobStatus

*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | *No description provided* |

forEachProc(P qs, function.Function<Q,String> name, io.github.epi155.pm.batch.job.Proc<Q> proc)
-----------------------------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| qs   | *No description provided* |
| name | *No description provided* |
| proc | *No description provided* |

execProc(P p, String procName, io.github.epi155.pm.batch.job.Proc<P> proc)
--------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| p        | *No description provided* |
| procName | *No description provided* |
| proc     | *No description provided* |

execProc(String procName, io.github.epi155.pm.batch.job.Proc<Void> proc)
------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| procName | *No description provided* |
| proc     | *No description provided* |

cond(int cc, Cond cond)
-----------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| cc   | *No description provided* |
| cond | *No description provided* |

cond(int cc, Cond cond, String stepName)
----------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| cc       | *No description provided* |
| cond     | *No description provided* |
| stepName | *No description provided* |

exec(function.Consumer<io.github.epi155.pm.batch.job.JobStatus> action)
-----------------------------------------------------------------------
### Overrides/Implements:
exec(function.Consumer<io.github.epi155.pm.batch.job.JobStatus> action) from io.github.epi155.pm.batch.job.JobStatus

*No method description provided*

### Parameters

| Name   | Description               |
| ------ | ------------------------- |
| action | *No description provided* |

maxcc()
-------
### Overrides/Implements:
maxcc() from io.github.epi155.pm.batch.job.JobStatus

*No method description provided*


lastcc()
--------
### Overrides/Implements:
lastcc() from io.github.epi155.pm.batch.job.JobStatus

*No method description provided*


complete()
----------
### Overrides/Implements:
complete() from io.github.epi155.pm.batch.job.JobStatus

*No method description provided*


done()
------
### Overrides/Implements:
done() from io.github.epi155.pm.batch.job.JobStatus

*No method description provided*


joinResult(concurrent.Future<Integer> ele)
------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| ele  | *No description provided* |

sendMessage(int j, int r)
-------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| j    | *No description provided* |
| r    | *No description provided* |

runStep(StatsCount c, IntSupplier step)
---------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| c    | *No description provided* |
| step | *No description provided* |

maxcc(int returnCode)
---------------------
*No method description provided*

### Parameters

| Name       | Description               |
| ---------- | ------------------------- |
| returnCode | *No description provided* |

fullName(String name)
---------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| name | *No description provided* |

join()
------
*No method description provided*


join(String name)
-----------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| name | *No description provided* |

quit(String name)
-----------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| name | *No description provided* |

loopOnFutures(Iterator<Map.Entry<String,concurrent.Future<Integer>>> it, int k)
-------------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| it   | *No description provided* |
| k    | *No description provided* |

add(String name, int rc, Instant tiStart, Instant tiEnd, Throwable error)
-------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name    | Description               |
| ------- | ------------------------- |
| name    | *No description provided* |
| rc      | *No description provided* |
| tiStart | *No description provided* |
| tiEnd   | *No description provided* |
| error   | *No description provided* |

add(String name, int rc, Instant tiStart, Instant tiEnd)
--------------------------------------------------------
*No method description provided*

### Parameters

| Name    | Description               |
| ------- | ------------------------- |
| name    | *No description provided* |
| rc      | *No description provided* |
| tiStart | *No description provided* |
| tiEnd   | *No description provided* |

add(String cmd, int rc)
-----------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| cmd  | *No description provided* |
| rc   | *No description provided* |

add(String cmd)
---------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| cmd  | *No description provided* |

wrapper(StatsCount c, IntSupplier step)
---------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| c    | *No description provided* |
| step | *No description provided* |

execPgm(P p, C c, function.BiFunction<P,C,Integer> pgm)
-------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| p    | *No description provided* |
| c    | *No description provided* |
| pgm  | *No description provided* |

execPgm(P p, String stepName, function.ToIntFunction<P> pgm)
------------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| p        | *No description provided* |
| stepName | *No description provided* |
| pgm      | *No description provided* |

execPgm(C c, function.ToIntFunction<C> pgm)
-------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| c    | *No description provided* |
| pgm  | *No description provided* |

execPgm(String stepName, IntSupplier pgm)
-----------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | *No description provided* |
| pgm      | *No description provided* |

execPgm(P p, C c, function.BiConsumer<P,C> pgm)
-----------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| p    | *No description provided* |
| c    | *No description provided* |
| pgm  | *No description provided* |

execPgm(P p, String stepName, function.Consumer<P> pgm)
-------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| p        | *No description provided* |
| stepName | *No description provided* |
| pgm      | *No description provided* |

execPgm(C c, function.Consumer<C> pgm)
--------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| c    | *No description provided* |
| pgm  | *No description provided* |

execPgm(String stepName, Runnable pgm)
--------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | *No description provided* |
| pgm      | *No description provided* |

submit(String stepName, concurrent.Callable<Integer> step)
----------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | *No description provided* |
| step     | *No description provided* |

forkPgm(P p, C c, function.BiFunction<P,C,Integer> pgm)
-------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| p    | *No description provided* |
| c    | *No description provided* |
| pgm  | *No description provided* |

forkPgm(P p, String stepName, function.ToIntFunction<P> pgm)
------------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| p        | *No description provided* |
| stepName | *No description provided* |
| pgm      | *No description provided* |

forkPgm(C c, function.ToIntFunction<C> pgm)
-------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| c    | *No description provided* |
| pgm  | *No description provided* |

forkPgm(String stepName, IntSupplier pgm)
-----------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | *No description provided* |
| pgm      | *No description provided* |

forkPgm(P p, C c, function.BiConsumer<P,C> pgm)
-----------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| p    | *No description provided* |
| c    | *No description provided* |
| pgm  | *No description provided* |

forkPgm(P p, String stepName, function.Consumer<P> pgm)
-------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| p        | *No description provided* |
| stepName | *No description provided* |
| pgm      | *No description provided* |

forkPgm(C c, function.Consumer<C> pgm)
--------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| c    | *No description provided* |
| pgm  | *No description provided* |

forkPgm(String stepName, Runnable pgm)
--------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | *No description provided* |
| pgm      | *No description provided* |

forEachPgm(P p, function.Function<Q,C> c, function.BiFunction<Q,C,Integer> pgm)
-------------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| p    | *No description provided* |
| c    | *No description provided* |
| pgm  | *No description provided* |

forEachPgm(P p, function.Function<Q,String> name, function.ToIntFunction<Q> pgm)
--------------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| p    | *No description provided* |
| name | *No description provided* |
| pgm  | *No description provided* |

forEachPgm(P p, function.Function<Q,C> c, function.BiConsumer<Q,C> pgm)
-----------------------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| p    | *No description provided* |
| c    | *No description provided* |
| pgm  | *No description provided* |

forEachPgm(P p, function.Function<Q,String> name, function.Consumer<Q> pgm)
---------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| p    | *No description provided* |
| name | *No description provided* |
| pgm  | *No description provided* |

forkProc(P p, String procName, io.github.epi155.pm.batch.job.Proc<P> proc)
--------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| p        | *No description provided* |
| procName | *No description provided* |
| proc     | *No description provided* |

runProc(String procName, function.ToIntFunction<io.github.epi155.pm.batch.job.JobStatus> fcn)
---------------------------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| procName | *No description provided* |
| fcn      | *No description provided* |

forkProc(String procName, io.github.epi155.pm.batch.job.Proc<Void> proc)
------------------------------------------------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| procName | *No description provided* |
| proc     | *No description provided* |

