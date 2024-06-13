Interface PullProcess2o7
========================
interface to manage 2 input to 7 output processing

io.github.epi155.pm.batch.step.PullProcess2o7 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                            | Return type |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(io.github.epi155.pm.batch.step.PullWorker2o7<I1,I2,O1,O2,O3,O4,O5,O6,O7> worker)](#proceediogithubepi155pmbatchsteppullworker2o7i1-i2-o1-o2-o3-o4-o5-o6-o7-worker) | void        |

Methods
=======
proceed(io.github.epi155.pm.batch.step.PullWorker2o7<I1,I2,O1,O2,O3,O4,O5,O6,O7> worker)
----------------------------------------------------------------------------------------
processes the data
 <pre>
 Pgm.from(src1, src2).into(dst).proceed((rd1, rd2, wr1, wr2, wr3, wr4, wr5, wr6, wr7) -> {
     val i1 = rd1.get();
     val i2 = rd2.get();
     ...
     wr1.accept(o1);
     wr2.accept(o2);
     wr3.accept(o3);
     wr4.accept(o4);
     wr5.accept(o5);
     wr6.accept(o6);
     wr7.accept(o7);
 });
 </pre>

### Parameters

| Name   | Description                                                  |
| ------ | ------------------------------------------------------------ |
| worker | worker who read the input values and writes the output value |

