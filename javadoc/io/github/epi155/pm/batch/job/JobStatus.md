Interface JobStatus implements io.github.epi155.pm.batch.job.JobAction<io.github.epi155.pm.batch.job.JobStatus>, io.github.epi155.pm.batch.job.CondAction<io.github.epi155.pm.batch.job.JobStatus>
==================================================================================================================================================================================================
Job environment

io.github.epi155.pm.batch.job.JobStatus Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                         | Return type                             |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------- |
| **public abstract** | [exec(function.Consumer<io.github.epi155.pm.batch.job.JobStatus> action)](#execfunctionconsumeriogithubepi155pmbatchjobjobstatus-action) | io.github.epi155.pm.batch.job.JobStatus |
| **public abstract** | [maxcc()](#maxcc)                                                                                                                        | int                                     |
| **public abstract** | [lastcc()](#lastcc)                                                                                                                      | Integer                                 |
| **public abstract** | [complete()](#complete)                                                                                                                  | int                                     |
| **public abstract** | [done()](#done)                                                                                                                          | io.github.epi155.pm.batch.job.JobInfo   |
| **public abstract** | [push()](#push)                                                                                                                          | io.github.epi155.pm.batch.job.JobStatus |
| **public abstract** | [pop()](#pop)                                                                                                                            | io.github.epi155.pm.batch.job.JobStatus |
| **public abstract** | [peek()](#peek)                                                                                                                          | io.github.epi155.pm.batch.job.JobStatus |
| **public abstract** | [returnCode(String stepName)](#returncodestring-stepname)                                                                                | Optional<Integer>                       |

Methods
=======
exec(function.Consumer<io.github.epi155.pm.batch.job.JobStatus> action)
-----------------------------------------------------------------------
Action on JobStatus
 <p>used for conditional step
 <pre>
 int xc = JCL.getInstance(),job("job")
             .execPgm(count1, this::step01)
             .exec(s -> s.isSuccess() ? s.execPgm(count2, this::step02)
                                     : s.execPgm(count3, this::step03))
             .complete();
 </pre>

### Parameters

| Name   | Description    |
| ------ | -------------- |
| action | action ontatus |

### Returns

instance of {@link JobStatus}


maxcc()
-------
Max step return code

### Returns

max step return code


lastcc()
--------
Last step return code

### Returns

last step return code


complete()
----------
Complete job, and get job return code

### Returns

job return code


done()
------
Completes the job and returns information about its execution

### Returns

information about the execution of the job


push()
------
Pushes jobReturnCode onto the internal stack

### Returns

original jobStatus


pop()
-----
Pops jobReturnCode from the stack

### Returns

jobStatus with restored jobReturnCode


peek()
------
Retrieves, but does not remove, the head of the stack

### Returns

jobStatus with restored jobReturnCode


returnCode(String stepName)
---------------------------
Retrieves the returnCode of the step with the indicated name
 <p>
 if a step with the indicated name does not exist or has not been executed, an Optional.empty() is returned

### Parameters

| Name     | Description |
| -------- | ----------- |
| stepName | step name   |

### Returns

optional step return code


