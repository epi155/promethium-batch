Package _io.github.epi155.pm.batch.job_
=======================================
Package with classes to create jobs and coordinate job steps
---




Job example
 

<pre>


 public Integer call() {
     return JCL.getInstance().job("job01")
         .execPgm("step01", step01::run)
         .complete();
 }
 

</pre>

Enumerations
============
| Name            |
| --------------- |
| [Cond](Cond.md) |

Interfaces
==========
| Name                              |
| --------------------------------- |
| [LoopPgm](LoopPgm.md)             |
| [JCL](JCL.md)                     |
| [CondAction](CondAction.md)       |
| [ForkPgm](ForkPgm.md)             |
| [JobStatus](JobStatus.md)         |
| [JobTrace](JobTrace.md)           |
| [ForkProc](ForkProc.md)           |
| [ExecProc](ExecProc.md)           |
| [JobAction](JobAction.md)         |
| [LoopProc](LoopProc.md)           |
| [ValueFactory](ValueFactory.md)   |
| [JobInfo](JobInfo.md)             |
| [StepError](StepError.md)         |
| [ExecPgm](ExecPgm.md)             |
| [ValueProvider](ValueProvider.md) |
| [Joinable](Joinable.md)           |

Classes
=======
| Name                                        |
| ------------------------------------------- |
| [BatchIOException](BatchIOException.md)     |
| [BatchSQLException](BatchSQLException.md)   |
| [PmProc](PmProc.md)                         |
| [BareCount](BareCount.md)                   |
| [PmJCL](PmJCL.md)                           |
| [JobContext](JobContext.md)                 |
| [BatchStepException](BatchStepException.md) |
| [JobCount](JobCount.md)                     |
| [PmJob](PmJob.md)                           |
| [PmValue](PmValue.md)                       |
| [StatsCount](StatsCount.md)                 |
| [BatchJobException](BatchJobException.md)   |
| [Proc](Proc.md)                             |
| [BatchException](BatchException.md)         |

