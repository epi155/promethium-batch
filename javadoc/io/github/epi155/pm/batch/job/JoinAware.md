Interface JoinAware
===================
interface to manage programs (procedures) that are running in the background

io.github.epi155.pm.batch.job.Joinable Summary
-------
#### Methods
| Modifiers           | Method signature                      | Return type                             |
| ------------------- | ------------------------------------- | --------------------------------------- |
| **public abstract** | [join()](#join)                       | io.github.epi155.pm.batch.job.JobStatus |
| **public abstract** | [join(String name)](#joinstring-name) | io.github.epi155.pm.batch.job.JobStatus |
| **public abstract** | [quit(String name)](#quitstring-name) | io.github.epi155.pm.batch.job.JobStatus |

Methods
=======
join()
------
waits for the completion of all the programs launched in the background.
 {@code lastcc} will be the maximum returnCode returned by programs running in the background

### Returns

instance of {@link JobStatus}


join(String name)
-----------------
waits for the completion of the program (procedure) launched in the background.

### Parameters

| Name | Description                                                     |
| ---- | --------------------------------------------------------------- |
| name | name of the step (proc) associated with the program (procedure) |

### Returns

instance of {@link JobStatus} with step(proc) return code


quit(String name)
-----------------
sends a stop signal to the program (procedure) that is running in the background

### Parameters

| Name | Description                                                     |
| ---- | --------------------------------------------------------------- |
| name | name of the step (proc) associated with the program (procedure) |

### Returns

unchanged instance of {@link JobStatus}


