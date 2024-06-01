Interface ForkProc
==================
class to execute a procedure

io.github.epi155.pm.batch.job.ForkProc Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                              | Return type |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [forkProc(P p, String procName, io.github.epi155.pm.batch.job.Proc<P> proc)](#forkprocp-p-string-procname-iogithubepi155pmbatchjobprocp-proc) | S           |
| **public abstract** | [forkProc(String procName, io.github.epi155.pm.batch.job.Proc<Void> proc)](#forkprocstring-procname-iogithubepi155pmbatchjobprocvoid-proc)    | S           |

Methods
=======
forkProc(P p, String procName, io.github.epi155.pm.batch.job.Proc<P> proc)
--------------------------------------------------------------------------
runs the procedure in background (with a parameter class)

### Parameters

| Name     | Description                  |
| -------- | ---------------------------- |
| p        | procedure parameters         |
| procName | procedure name               |
| proc     | procedure to be run          |
| P        | type of procedure parameters |

### Returns

job class


forkProc(String procName, io.github.epi155.pm.batch.job.Proc<Void> proc)
------------------------------------------------------------------------
runs the procedure in background (without a parameter class)

### Parameters

| Name     | Description         |
| -------- | ------------------- |
| procName | procedure name      |
| proc     | procedure to be run |

### Returns

job class


