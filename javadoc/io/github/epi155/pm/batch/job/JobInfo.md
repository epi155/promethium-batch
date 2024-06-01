Interface JobInfo
=================
interface to provide information about job execution

io.github.epi155.pm.batch.job.JobInfo Summary
-------
#### Methods
| Modifiers           | Method signature              | Return type                                   |
| ------------------- | ----------------------------- | --------------------------------------------- |
| **public abstract** | [getName()](#getname)         | String                                        |
| **public abstract** | [getExitCode()](#getexitcode) | int                                           |
| **public abstract** | [getErrors()](#geterrors)     | List<io.github.epi155.pm.batch.job.StepError> |

Methods
=======
getName()
---------
provides the name of the job

### Returns

job name


getExitCode()
-------------
provides the exit code of the job

### Returns

job exit code


getErrors()
-----------
provides the list of information on the steps that ended in error

### Returns

list of information on the steps that ended in error


