Interface PullProcess2o2
========================
interface to manage 2 input to 2 output processing

io.github.epi155.pm.batch.step.PullProcess2o2 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                              | Return type |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(io.github.epi155.pm.batch.step.PullWorker2o2<I1,I2,O1,O2> worker)](#proceediogithubepi155pmbatchsteppullworker2o2i1-i2-o1-o2-worker) | void        |

Methods
=======
proceed(io.github.epi155.pm.batch.step.PullWorker2o2<I1,I2,O1,O2> worker)
-------------------------------------------------------------------------
processes the data
 <pre>
 Pgm.from(src1, src2).into(dst).proceed((rd1, rd2, wr1, wr2) -> {
     val i1 = rd1.get();
     val i2 = rd2.get();
     ...
     wr1.accept(o1);
     wr2.accept(o2);
 });
 </pre>

### Parameters

| Name   | Description                                                  |
| ------ | ------------------------------------------------------------ |
| worker | worker who read the input values and writes the output value |


