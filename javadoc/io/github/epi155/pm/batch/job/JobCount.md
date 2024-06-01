Class JobCount implements io.github.epi155.pm.batch.job.JobTrace extends io.github.epi155.pm.batch.job.StatsCount
=================================================================================================================


io.github.epi155.pm.batch.job.JobCount Summary
-------
#### Constructors
| Visibility | Signature                  |
| ---------- | -------------------------- |
| public     | JobCount(java.lang.String) |
#### Fields
| Modifiers                | Field name                                                                                              | Type                                                                              |
| ------------------------ | ------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- |
| **private static final** | [JAVA_BASE](#javalangstring-java_base)                                                                  | String                                                                            |
| **private static final** | [L_STEP](#javalangstring-l_step)                                                                        | String                                                                            |
| **private final**        | [stepInfos](#javautilconcurrentconcurrentlinkedqueueiogithubepi155pmbatchjobjobcountstepinfo-stepinfos) | concurrent.ConcurrentLinkedQueue<io.github.epi155.pm.batch.job.JobCount.StepInfo> |
| **private final**        | [tiStart](#javatimeinstant-tistart)                                                                     | java.time.Instant                                                                 |
| **private**              | [maxcc](#int-maxcc)                                                                                     | int                                                                               |
#### Methods
| Modifiers     | Method signature                                                                                                                                                  | Return type                                   |
| ------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------- |
| **protected** | [recap(PrintWriter pw)](#recapprintwriter-pw)                                                                                                                     | void                                          |
| **private**   | [cause(StepFail fail)](#causestepfail-fail)                                                                                                                       | String                                        |
| **public**    | [add(String name, int returnCode, Instant tiStart, Instant tiEnd, Throwable error)](#addstring-name-int-returncode-instant-tistart-instant-tiend-throwable-error) | void                                          |
| **public**    | [add(String name, int returnCode, Instant tiStart, Instant tiEnd)](#addstring-name-int-returncode-instant-tistart-instant-tiend)                                  | void                                          |
| **public**    | [add(String name, int returnCode)](#addstring-name-int-returncode)                                                                                                | void                                          |
| **public**    | [add(String name)](#addstring-name)                                                                                                                               | void                                          |
| **public**    | [fullName(String name)](#fullnamestring-name)                                                                                                                     | String                                        |
| **public**    | [getPrefix()](#getprefix)                                                                                                                                         | String                                        |
|               | [getReturnCode(String stepName)](#getreturncodestring-stepname)                                                                                                   | Optional<Integer>                             |
| **public**    | [maxcc(int maxcc)](#maxccint-maxcc)                                                                                                                               | void                                          |
| **protected** | [stepErrors()](#steperrors)                                                                                                                                       | List<io.github.epi155.pm.batch.job.StepError> |

Fields
======
String JAVA_BASE
--------------------------
*No description provided*


String L_STEP
-----------------------
*No description provided*


concurrent.ConcurrentLinkedQueue<io.github.epi155.pm.batch.job.JobCount.StepInfo> stepInfos
-----------------------------------------------------------------------------------------------------
*No description provided*


java.time.Instant tiStart
-------------------------
*No description provided*


int maxcc
---------
*No description provided*


Methods
=======
recap(PrintWriter pw)
---------------------
### Overrides/Implements:
recap(PrintWriter pw) from io.github.epi155.pm.batch.job.StatsCount

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| pw   | *No description provided* |

cause(StepFail fail)
--------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| fail | *No description provided* |

add(String name, int returnCode, Instant tiStart, Instant tiEnd, Throwable error)
---------------------------------------------------------------------------------
### Overrides/Implements:
add(String name, int returnCode, Instant tiStart, Instant tiEnd, Throwable error) from io.github.epi155.pm.batch.job.JobTrace

*No method description provided*

### Parameters

| Name       | Description               |
| ---------- | ------------------------- |
| name       | *No description provided* |
| returnCode | *No description provided* |
| tiStart    | *No description provided* |
| tiEnd      | *No description provided* |
| error      | *No description provided* |

add(String name, int returnCode, Instant tiStart, Instant tiEnd)
----------------------------------------------------------------
### Overrides/Implements:
add(String name, int returnCode, Instant tiStart, Instant tiEnd) from io.github.epi155.pm.batch.job.JobTrace

*No method description provided*

### Parameters

| Name       | Description               |
| ---------- | ------------------------- |
| name       | *No description provided* |
| returnCode | *No description provided* |
| tiStart    | *No description provided* |
| tiEnd      | *No description provided* |

add(String name, int returnCode)
--------------------------------
### Overrides/Implements:
add(String name, int returnCode) from io.github.epi155.pm.batch.job.JobTrace

*No method description provided*

### Parameters

| Name       | Description               |
| ---------- | ------------------------- |
| name       | *No description provided* |
| returnCode | *No description provided* |

add(String name)
----------------
### Overrides/Implements:
add(String name) from io.github.epi155.pm.batch.job.JobTrace

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| name | *No description provided* |

fullName(String name)
---------------------
### Overrides/Implements:
fullName(String name) from io.github.epi155.pm.batch.job.JobTrace

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| name | *No description provided* |

getPrefix()
-----------
### Overrides/Implements:
getPrefix() from io.github.epi155.pm.batch.job.JobTrace

*No method description provided*


getReturnCode(String stepName)
------------------------------
*No method description provided*

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | *No description provided* |

maxcc(int maxcc)
----------------
*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| maxcc | *No description provided* |

stepErrors()
------------
*No method description provided*


