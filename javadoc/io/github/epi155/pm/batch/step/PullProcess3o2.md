Interface PullProcess3o2
========================
interface to manage 3 input to 2 output processing

io.github.epi155.pm.batch.step.PullProcess3o2 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                    | Return type |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(io.github.epi155.pm.batch.step.PullWorker3o2<I1,I2,I3,O1,O2> worker)](#proceediogithubepi155pmbatchsteppullworker3o2i1-i2-i3-o1-o2-worker) | void        |

Methods
=======
proceed(io.github.epi155.pm.batch.step.PullWorker3o2<I1,I2,I3,O1,O2> worker)
----------------------------------------------------------------------------
processes the data
 <pre>
 Pgm.from(src1,src2,src3).into(dst).proceed((rd1,rd2,rd3,wr1,wr2) -> {
     val i1 = rd1.get();
     val i2 = rd2.get();
     val i3 = rd3.get();
     ...
     wr1.accept(o1);
     wr2.accept(o2);
 });
 </pre>

### Parameters

| Name   | Description                                                  |
| ------ | ------------------------------------------------------------ |
| worker | worker who read the input values and writes the output value |


