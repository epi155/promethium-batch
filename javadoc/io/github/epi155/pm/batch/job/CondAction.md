Interface CondAction
====================
interface that contains conditional operations to drive the execution of programs or procedures

io.github.epi155.pm.batch.job.CondAction Summary
-------
#### Methods
| Modifiers           | Method signature                                                                  | Return type                                |
| ------------------- | --------------------------------------------------------------------------------- | ------------------------------------------ |
| **public abstract** | [cond(int cc, Cond cond)](#condint-cc-cond-cond)                                  | io.github.epi155.pm.batch.job.JobAction<T> |
| **public default**  | [when(int cc, Cond cond)](#whenint-cc-cond-cond)                                  | io.github.epi155.pm.batch.job.JobAction<T> |
| **public abstract** | [cond(int cc, Cond cond, String stepName)](#condint-cc-cond-cond-string-stepname) | io.github.epi155.pm.batch.job.JobAction<T> |
| **public default**  | [when(int cc, Cond cond, String stepName)](#whenint-cc-cond-cond-string-stepname) | io.github.epi155.pm.batch.job.JobAction<T> |

Methods
=======
cond(int cc, Cond cond)
-----------------------
condition for not performing the next operation
 <p>
 example
 <pre>
 JCL.getInstance().job("job01")
     .execPgm("step01", this::step01)
     <b>.cond(0,NE)</b>.execPgm("step02", this::step02)
     .complete();
 </pre>
 if the previous program (step01) ends with a return code other than zero (NE),
 it does not execute the program that follows the condition (step02)

### Parameters

| Name | Description            |
| ---- | ---------------------- |
| cc   | value to test          |
| cond | condition to be tested |

### Returns

state to which to apply the operation


when(int cc, Cond cond)
-----------------------
condition for performing the next operation
 <p>
 example
 <pre>
 JCL.getInstance().job("job01")
     .execPgm("step01", this::step01)
     <b>.when(0,EQ)</b>.execPgm("step02", this::step02)
     .complete();
 </pre>
 if the previous program (step01) ends with a return code equal (EQ) to zero,
 executes the program that follows the condition (step02)

### Parameters

| Name | Description            |
| ---- | ---------------------- |
| cc   | value to test          |
| cond | condition to be tested |

### Returns

state to which to apply the operation


cond(int cc, Cond cond, String stepName)
----------------------------------------
condition for not performing the next operation
 <p>
 if the given name does not exist or has not been executed, the next program is executed
 <p>
 example
 <pre>
 JCL.getInstance().job("job01")
     .execPgm("step01", this::step01)
     <b>.cond(0,NE,"step01")</b>
         .execPgm("step02", this::step02)
     .complete();
 </pre>
 if the indicated program (step01) ends with a non-zero return code (NE),
 it does not execute the program that follows the condition (step02)

### Parameters

| Name     | Description              |
| -------- | ------------------------ |
| cc       | value to test            |
| cond     | condition to be tested   |
| stepName | name of the step to test |

### Returns

state to which to apply the operation


when(int cc, Cond cond, String stepName)
----------------------------------------
condition for performing the next operation
 <p>
 if the given name does not exist or has not been executed, the next program is executed
 <p>
 example
 <pre>
 JCL.getInstance().job("job01")
     .execPgm("step01", this::step01)
     <b>.when(0,EQ,"step01")</b>
         .execPgm("step02", this::step02)
     .complete();
 </pre>
 if the indicated program (step01) ends with a return code equal (EQ) to zero,
 executes the program that follows the condition (step02)

### Parameters

| Name     | Description              |
| -------- | ------------------------ |
| cc       | value to test            |
| cond     | condition to be tested   |
| stepName | name of the step to test |

### Returns

state to which to apply the operation


