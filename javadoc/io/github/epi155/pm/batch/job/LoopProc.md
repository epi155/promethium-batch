Interface LoopProc
==================
class to iterate a procedure

io.github.epi155.pm.batch.job.LoopProc Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                                   | Return type |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [forEachProc(P p, function.Function<Q,String> name, io.github.epi155.pm.batch.job.Proc<Q> proc)](#foreachprocp-p-functionfunctionq-string-name-iogithubepi155pmbatchjobprocq-proc) | S           |

Methods
=======
forEachProc(P p, function.Function<Q,String> name, io.github.epi155.pm.batch.job.Proc<Q> proc)
----------------------------------------------------------------------------------------------
iterate the execution of the procedure

### Parameters

| Name | Description                                                            |
| ---- | ---------------------------------------------------------------------- |
| p    | job parameter (or parent procedure)                                    |
| name | function that returns the procedure name from the procedure parameters |
| proc | procedure to be performed                                              |
| P    | type of job parameter                                                  |
| Q    | type of procedure parameter                                            |

### Returns

job class


