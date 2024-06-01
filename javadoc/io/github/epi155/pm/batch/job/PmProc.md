Class PmProc extends io.github.epi155.pm.batch.job.Proc<P>
==========================================================


io.github.epi155.pm.batch.job.PmProc Summary
-------
#### Constructors
| Visibility | Signature                                                                                                                |
| ---------- | ------------------------------------------------------------------------------------------------------------------------ |
|            | PmProc(java.util.function.BiFunction<P,io.github.epi155.pm.batch.job.JobStatus,io.github.epi155.pm.batch.job.JobStatus>) |
|            | PmProc(java.util.function.UnaryOperator<io.github.epi155.pm.batch.job.JobStatus>)                                        |
#### Fields
| Modifiers         | Field name                                                                                                      | Type                                                                                                   |
| ----------------- | --------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------ |
| **private final** | [route](#javautilfunctionbifunctionp-iogithubepi155pmbatchjobjobstatus-iogithubepi155pmbatchjobjobstatus-route) | function.BiFunction<P,io.github.epi155.pm.batch.job.JobStatus,io.github.epi155.pm.batch.job.JobStatus> |
#### Methods
| Modifiers     | Method signature                               | Return type |
| ------------- | ---------------------------------------------- | ----------- |
| **protected** | [call(P p, JobStatus s)](#callp-p-jobstatus-s) | int         |

Fields
======
function.BiFunction<P,io.github.epi155.pm.batch.job.JobStatus,io.github.epi155.pm.batch.job.JobStatus> route
----------------------------------------------------------------------------------------------------------------------
*No description provided*


Methods
=======
call(P p, JobStatus s)
----------------------
### Overrides/Implements:
call(P p, JobStatus s) from io.github.epi155.pm.batch.job.Proc

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| p    | *No description provided* |
| s    | *No description provided* |

