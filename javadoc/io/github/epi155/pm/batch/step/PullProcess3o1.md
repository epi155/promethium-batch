Interface PullProcess3o1
========================
interface to manage 3 input to 1 output processing

io.github.epi155.pm.batch.step.PullProcess3o1 Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                            | Return type |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [proceed(io.github.epi155.pm.batch.step.PullWorker3o1<I1,I2,I3,O> worker)](#proceediogithubepi155pmbatchsteppullworker3o1i1-i2-i3-o-worker) | void        |

Methods
=======
proceed(io.github.epi155.pm.batch.step.PullWorker3o1<I1,I2,I3,O> worker)
------------------------------------------------------------------------
processes the data
 <pre>
 Pgm.from(src1,src2,src3).into(dst).proceed((rd1,rd2,rd3,wr) -> {
     val i1 = rd1.get();
     val i2 = rd2.get();
     val i3 = rd3.get();
     ...
     wr.accept(o);
 });
 </pre>

### Parameters

| Name   | Description                                                  |
| ------ | ------------------------------------------------------------ |
| worker | worker who read the input values and writes the output value |


