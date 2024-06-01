Class Proc
==========
class to define a procedure (sequence of programs)

io.github.epi155.pm.batch.job.Proc Summary
-------
#### Constructors
| Visibility | Signature |
| ---------- | --------- |
| public     | Proc()    |
#### Methods
| Modifiers              | Method signature                                                                                                                                                                                                             | Return type                              |
| ---------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------- |
| **public static**      | [create(function.BiFunction<Q,io.github.epi155.pm.batch.job.JobStatus,io.github.epi155.pm.batch.job.JobStatus> route)](#createfunctionbifunctionq-iogithubepi155pmbatchjobjobstatus-iogithubepi155pmbatchjobjobstatus-route) | io.github.epi155.pm.batch.job.Proc<Q>    |
| **public static**      | [create(function.UnaryOperator<io.github.epi155.pm.batch.job.JobStatus> route)](#createfunctionunaryoperatoriogithubepi155pmbatchjobjobstatus-route)                                                                         | io.github.epi155.pm.batch.job.Proc<Void> |
| **protected abstract** | [call(P p, JobStatus s)](#callp-p-jobstatus-s)                                                                                                                                                                               | int                                      |

Methods
=======
create(function.BiFunction<Q,io.github.epi155.pm.batch.job.JobStatus,io.github.epi155.pm.batch.job.JobStatus> route)
--------------------------------------------------------------------------------------------------------------------
static constructor of a procedure with a parameter class
 <p>example
 <pre>
 Proc&lt;Parm01&gt; proc01 = Proc.create((p, s) -> s
     .execPgm(p, "Step01", this::step01)
     .cond(0,NE)
         .execPgm(p, "Step02", this::step02)
 );
 </pre>

### Parameters

| Name  | Description                                 |
| ----- | ------------------------------------------- |
| route | sequence of programs to call (internal job) |
| Q     | parameter type                              |

### Returns

instance of {@link Proc} (the procedure)


create(function.UnaryOperator<io.github.epi155.pm.batch.job.JobStatus> route)
-----------------------------------------------------------------------------
static constructor of a procedure without a parameter class
 <p>example
 <pre>
 Proc&lt;Void&gt; proc01 = Proc.create(s -> s
     .execPgm("Step01", this::step01)
     .cond(0,NE)
         .execPgm("Step02", this::step02)
 );
 </pre>

### Parameters

| Name  | Description                                 |
| ----- | ------------------------------------------- |
| route | sequence of programs to call (internal job) |

### Returns

instance of {@link Proc} (the procedure)


call(P p, JobStatus s)
----------------------
calls the procedure

### Parameters

| Name | Description                             |
| ---- | --------------------------------------- |
| p    | procedure parameter (or  {@code null} ) |
| s    | job in which to launch the procedure    |

### Returns

procedure max return code


