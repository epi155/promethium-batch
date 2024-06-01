Interface StepError
===================
interface to provide information about the execution of the step that ended in error

io.github.epi155.pm.batch.job.StepError Summary
-------
#### Methods
| Modifiers           | Method signature                  | Return type |
| ------------------- | --------------------------------- | ----------- |
| **public abstract** | [getName()](#getname)             | String      |
| **public abstract** | [getReturnCode()](#getreturncode) | int         |
| **public abstract** | [getError()](#geterror)           | Throwable   |

Methods
=======
getName()
---------
provides the name of the step

### Returns

step name


getReturnCode()
---------------
provides the return code of the step

### Returns

step return code


getError()
----------
returns the step error

### Returns

step error


