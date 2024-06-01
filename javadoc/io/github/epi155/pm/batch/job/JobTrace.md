Interface JobTrace
==================
interface to notify the execution of programs to the job

io.github.epi155.pm.batch.job.JobTrace Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                  | Return type |
| ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [add(String name, int returnCode, Instant tiStart, Instant tiEnd, Throwable error)](#addstring-name-int-returncode-instant-tistart-instant-tiend-throwable-error) | void        |
| **public abstract** | [add(String name, int returnCode, Instant tiStart, Instant tiEnd)](#addstring-name-int-returncode-instant-tistart-instant-tiend)                                  | void        |
| **public abstract** | [add(String name, int returnCode)](#addstring-name-int-returncode)                                                                                                | void        |
| **public abstract** | [add(String name)](#addstring-name)                                                                                                                               | void        |
| **public abstract** | [fullName(String name)](#fullnamestring-name)                                                                                                                     | String      |
| **public abstract** | [getPrefix()](#getprefix)                                                                                                                                         | String      |

Methods
=======
add(String name, int returnCode, Instant tiStart, Instant tiEnd, Throwable error)
---------------------------------------------------------------------------------
provides information on program execution

### Parameters

| Name       | Description          |
| ---------- | -------------------- |
| name       | step name            |
| returnCode | step return code     |
| tiStart    | step start timestamp |
| tiEnd      | step end timestamp   |
| error      | step exception       |


add(String name, int returnCode, Instant tiStart, Instant tiEnd)
----------------------------------------------------------------
provides information on program execution

### Parameters

| Name       | Description          |
| ---------- | -------------------- |
| name       | step name            |
| returnCode | step return code     |
| tiStart    | step start timestamp |
| tiEnd      | step end timestamp   |


add(String name, int returnCode)
--------------------------------
provides information on command execution

### Parameters

| Name       | Description                      |
| ---------- | -------------------------------- |
| name       | command name                     |
| returnCode | job code after command execution |


add(String name)
----------------
provides information on skipped program

### Parameters

| Name | Description |
| ---- | ----------- |
| name | step name   |


fullName(String name)
---------------------
complete stepName with all parent procedures

### Parameters

| Name | Description |
| ---- | ----------- |
| name | step name   |

### Returns

full step name


getPrefix()
-----------
Step prefix (nested proc)

### Returns

step prefix


