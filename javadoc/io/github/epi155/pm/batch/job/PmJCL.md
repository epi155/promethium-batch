Class PmJCL implements io.github.epi155.pm.batch.job.JCL
========================================================


io.github.epi155.pm.batch.job.PmJCL Summary
-------
#### Constructors
| Visibility | Signature |
| ---------- | --------- |
| private    | PmJCL()   |
#### Fields
| Modifiers         | Field name                                               | Type                                       |
| ----------------- | -------------------------------------------------------- | ------------------------------------------ |
| **private final** | [factory](#iogithubepi155pmbatchjobvaluefactory-factory) | io.github.epi155.pm.batch.job.ValueFactory |
#### Methods
| Modifiers         | Method signature                                 | Return type                             |
| ----------------- | ------------------------------------------------ | --------------------------------------- |
| **public static** | [getInstance()](#getinstance)                    | io.github.epi155.pm.batch.job.PmJCL     |
| **public**        | [job(String name)](#jobstring-name)              | io.github.epi155.pm.batch.job.JobStatus |
| **public**        | [job(String name, int w)](#jobstring-name-int-w) | io.github.epi155.pm.batch.job.JobStatus |
| **public**        | [rcOk()](#rcok)                                  | int                                     |
| **public**        | [rcWarning()](#rcwarning)                        | int                                     |
| **public**        | [rcErrorStep()](#rcerrorstep)                    | int                                     |
| **public**        | [rcErrorJob()](#rcerrorjob)                      | int                                     |
| **public**        | [rcMax(int a, int b)](#rcmaxint-a-int-b)         | int                                     |
| **public**        | [jobName()](#jobname)                            | String                                  |
| **public**        | [stepName()](#stepname)                          | String                                  |
| **public**        | [rcErrorIO()](#rcerrorio)                        | int                                     |
| **public**        | [rcErrorSQL()](#rcerrorsql)                      | int                                     |

Fields
======
io.github.epi155.pm.batch.job.ValueFactory factory
--------------------------------------------------
*No description provided*


Methods
=======
getInstance()
-------------
### Overrides/Implements:
getInstance() from io.github.epi155.pm.batch.job.JCL

*No method description provided*


job(String name)
----------------
### Overrides/Implements:
job(String name) from io.github.epi155.pm.batch.job.JCL

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| name | *No description provided* |

job(String name, int w)
-----------------------
### Overrides/Implements:
job(String name, int w) from io.github.epi155.pm.batch.job.JCL

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| name | *No description provided* |
| w    | *No description provided* |

rcOk()
------
*No method description provided*


rcWarning()
-----------
*No method description provided*


rcErrorStep()
-------------
*No method description provided*


rcErrorJob()
------------
*No method description provided*


rcMax(int a, int b)
-------------------
*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| a    | *No description provided* |
| b    | *No description provided* |

jobName()
---------
*No method description provided*


stepName()
----------
*No method description provided*


rcErrorIO()
-----------
*No method description provided*


rcErrorSQL()
------------
*No method description provided*


