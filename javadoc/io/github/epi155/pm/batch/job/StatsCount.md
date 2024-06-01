Class StatsCount
================
super class to manage the execution statistics

io.github.epi155.pm.batch.job.StatsCount Summary
-------
#### Constructors
| Visibility | Signature                    |
| ---------- | ---------------------------- |
| protected  | StatsCount(java.lang.String) |
#### Fields
| Modifiers                | Field name                                   | Type             |
| ------------------------ | -------------------------------------------- | ---------------- |
| **private static final** | [RECAP_MARKER](#orgslf4jmarker-recap_marker) | org.slf4j.Marker |
| **private final**        | [name](#javalangstring-name)                 | String           |
| **private**              | [error](#javalangthrowable-error)            | Throwable        |
#### Methods
| Modifiers              | Method signature                                | Return type |
| ---------------------- | ----------------------------------------------- | ----------- |
|                        | [recap(int returnCode)](#recapint-returncode)   | void        |
|                        | [recap()](#recap)                               | void        |
| **protected abstract** | [recap(PrintWriter pw)](#recapprintwriter-pw)   | void        |
|                        | [name()](#name)                                 | String      |
|                        | [error(Throwable error)](#errorthrowable-error) | void        |

Fields
======
org.slf4j.Marker RECAP_MARKER
-----------------------------
*No description provided*


String name
---------------------
*No description provided*


Throwable error
-------------------------
*No description provided*


Methods
=======
recap(int returnCode)
---------------------
*No method description provided*

### Parameters

| Name       | Description               |
| ---------- | ------------------------- |
| returnCode | *No description provided* |

recap()
-------
*No method description provided*


recap(PrintWriter pw)
---------------------
customized statistical report

### Parameters

| Name | Description |
| ---- | ----------- |
| pw   | writer      |


name()
------
*No method description provided*


error(Throwable error)
----------------------
*No method description provided*

### Parameters

| Name  | Description               |
| ----- | ------------------------- |
| error | *No description provided* |

