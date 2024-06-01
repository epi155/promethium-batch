Interface PullProcess2o1
========================
interface to manage 2 input to 1 output processing

io.github.epi155.pm.batch.step.PullProcess2o1 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                      | Return type |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(io.github.epi155.pm.batch.step.PullWorker2o1<I1,I2,O> worker)](#proceediogithubepi155pmbatchsteppullworker2o1i1-i2-o-worker) | void        |

Methods
=======
proceed(io.github.epi155.pm.batch.step.PullWorker2o1<I1,I2,O> worker)
---------------------------------------------------------------------
processes the data
 <pre>
 Pgm.from(src1, src2).into(dst).proceed((rd1, rd2, wr) -> {
     val i1 = rd1.get();
     val i2 = rd2.get();
     ...
     wr.accept(o);
 });
 </pre>

### Parameters

| Name   | Description                                                  |
| ------ | ------------------------------------------------------------ |
| worker | worker who read the input values and writes the output value |


