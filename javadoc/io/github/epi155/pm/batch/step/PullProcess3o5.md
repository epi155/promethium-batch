Interface PullProcess3o5
========================
interface to manage 3 input to 5 output processing

io.github.epi155.pm.batch.step.PullProcess3o5 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                      | Return type |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(io.github.epi155.pm.batch.step.PullWorker3o5<I1,I2,I3,O1,O2,O3,O4,O5> worker)](#proceediogithubepi155pmbatchsteppullworker3o5i1-i2-i3-o1-o2-o3-o4-o5-worker) | void        |

Methods
=======
proceed(io.github.epi155.pm.batch.step.PullWorker3o5<I1,I2,I3,O1,O2,O3,O4,O5> worker)
-------------------------------------------------------------------------------------
processes the data
 <pre>
 Pgm.from(src1,src2,src3).into(dst).proceed((rd1,rd2,rd3,wr1,wr2,wr3,wr4,wr5) -> {
     val i1 = rd1.get();
     val i2 = rd2.get();
     val i3 = rd3.get();
     ...
     wr1.accept(o1);
     wr2.accept(o2);
     wr3.accept(o3);
     wr4.accept(o4);
     wr5.accept(o5);
 });
 </pre>

### Parameters

| Name   | Description                                                  |
| ------ | ------------------------------------------------------------ |
| worker | worker who read the input values and writes the output value |


