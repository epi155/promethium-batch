Interface JCL implements io.github.epi155.pm.batch.job.ValueFactory
===================================================================
root interface to launch the job control language

io.github.epi155.pm.batch.job.JCL Summary
-------
#### Methods
| Modifiers           | Method signature                                 | Return type                             |
| ------------------- | ------------------------------------------------ | --------------------------------------- |
| **public static**   | [getInstance()](#getinstance)                    | io.github.epi155.pm.batch.job.JCL       |
| **public abstract** | [job(String name)](#jobstring-name)              | io.github.epi155.pm.batch.job.JobStatus |
| **public abstract** | [job(String name, int w)](#jobstring-name-int-w) | io.github.epi155.pm.batch.job.JobStatus |

Methods
=======
getInstance()
-------------
JCL singleton

### Returns

instance of {@link JCL}


job(String name)
----------------
Initialize job environment

### Parameters

| Name | Description |
| ---- | ----------- |
| name | jobName     |

### Returns

instance of {@link JobStatus}


job(String name, int w)
-----------------------
Initialize job environment

### Parameters

| Name | Description                                               |
| ---- | --------------------------------------------------------- |
| name | jobName                                                   |
| w    | number of namespace nodes to use to select the stacktrace |

### Returns

instance of {@link JobStatus}


