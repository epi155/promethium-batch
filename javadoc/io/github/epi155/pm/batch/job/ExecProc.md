Interface ExecProc
==================
class to execute a procedure

io.github.epi155.pm.batch.job.ExecProc Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                              | Return type |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [execProc(P p, String procName, io.github.epi155.pm.batch.job.Proc<P> proc)](#execprocp-p-string-procname-iogithubepi155pmbatchjobprocp-proc) | S           |
| **public abstract** | [execProc(String procName, io.github.epi155.pm.batch.job.Proc<Void> proc)](#execprocstring-procname-iogithubepi155pmbatchjobprocvoid-proc)    | S           |

Methods
=======
execProc(P p, String procName, io.github.epi155.pm.batch.job.Proc<P> proc)
--------------------------------------------------------------------------
runs the procedure (with a parameter class)

### Parameters

| Name     | Description                  |
| -------- | ---------------------------- |
| p        | procedure parameters         |
| procName | procedure name               |
| proc     | procedure to be run          |
| P        | type of procedure parameters |

### Returns

job class


execProc(String procName, io.github.epi155.pm.batch.job.Proc<Void> proc)
------------------------------------------------------------------------
runs the procedure (without a parameter class)

### Parameters

| Name     | Description         |
| -------- | ------------------- |
| procName | procedure name      |
| proc     | procedure to be run |

### Returns

job class


