Interface ValueFactory
======================
ErrorCode Factory Interface

io.github.epi155.pm.batch.job.ValueFactory Summary
-------
#### Methods
| Modifiers           | Method signature                         | Return type |
| ------------------- | ---------------------------------------- | ----------- |
| **public abstract** | [rcOk()](#rcok)                          | int         |
| **public abstract** | [rcWarning()](#rcwarning)                | int         |
| **public abstract** | [rcErrorIO()](#rcerrorio)                | int         |
| **public abstract** | [rcErrorSQL()](#rcerrorsql)              | int         |
| **public abstract** | [rcErrorStep()](#rcerrorstep)            | int         |
| **public abstract** | [rcErrorJob()](#rcerrorjob)              | int         |
| **public abstract** | [rcMax(int a, int b)](#rcmaxint-a-int-b) | int         |
| **public abstract** | [jobName()](#jobname)                    | String      |
| **public abstract** | [stepName()](#stepname)                  | String      |

Methods
=======
rcOk()
------
Success error code

### Returns

success error code


rcWarning()
-----------
Warning error code

### Returns

warning error code


rcErrorIO()
-----------
Error code on IO operation

### Returns

IO error code


rcErrorSQL()
------------
Error code on SQL operation

### Returns

SQL errror code


rcErrorStep()
-------------
Error code at Step level

### Returns

step error code


rcErrorJob()
------------
Error code at Job level

### Returns

job error code


rcMax(int a, int b)
-------------------
Max returnCode operator

### Parameters

| Name | Description     |
| ---- | --------------- |
| a    | 1st return code |
| b    | 2nd return code |

### Returns

maximum return code


jobName()
---------
MDC key for jobName

### Returns

MDC key for jobName


stepName()
----------
MDC key for stepName

### Returns

MDC key for stepName


