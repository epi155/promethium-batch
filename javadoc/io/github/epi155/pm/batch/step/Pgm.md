Interface Pgm
=============
root interface for the batch process

io.github.epi155.pm.batch.step.Pgm Summary
-------
#### Methods
| Modifiers         | Method signature                                                                                                                                                                                                                                                                                                                                                      | Return type                                          |
| ----------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------- |
| **public static** | [from(io.github.epi155.pm.batch.step.SourceResource<S,I> source)](#fromiogithubepi155pmbatchstepsourceresources-i-source)                                                                                                                                                                                                                                             | io.github.epi155.pm.batch.step.LoopSource<I>         |
| **public static** | [from(io.github.epi155.pm.batch.step.SourceResource<S1,I1> source1, io.github.epi155.pm.batch.step.SourceResource<S2,I2> source2)](#fromiogithubepi155pmbatchstepsourceresources1-i1-source1-iogithubepi155pmbatchstepsourceresources2-i2-source2)                                                                                                                    | io.github.epi155.pm.batch.step.PullSource2<I1,I2>    |
| **public static** | [from(io.github.epi155.pm.batch.step.SourceResource<S1,I1> source1, io.github.epi155.pm.batch.step.SourceResource<S2,I2> source2, io.github.epi155.pm.batch.step.SourceResource<S3,I3> source3)](#fromiogithubepi155pmbatchstepsourceresources1-i1-source1-iogithubepi155pmbatchstepsourceresources2-i2-source2-iogithubepi155pmbatchstepsourceresources3-i3-source3) | io.github.epi155.pm.batch.step.PullSource3<I1,I2,I3> |

Methods
=======
from(io.github.epi155.pm.batch.step.SourceResource<S,I> source)
---------------------------------------------------------------
Sets a source resource<br>
 <pre>
 Pgm<b>.from(source)</b>.into(sink).forEach(src -&gt; ...)
 </pre>

### Parameters

| Name   | Description          |
| ------ | -------------------- |
| source | the source resource  |
| S      | source resource type |
| I      | source element type  |

### Returns

instance of {@link LoopSource}


from(io.github.epi155.pm.batch.step.SourceResource<S1,I1> source1, io.github.epi155.pm.batch.step.SourceResource<S2,I2> source2)
--------------------------------------------------------------------------------------------------------------------------------
Sets a source resource pair<br>
 <pre>
 Pgm.from(source1, source2).into(sink).proceed((it1, it2, wr) -&gt; ...)
 </pre>

### Parameters

| Name    | Description              |
| ------- | ------------------------ |
| source1 | the 1st source resource  |
| source2 | the 2nd source resource  |
| S1      | 1st source resource type |
| I1      | 1st source element type  |
| S2      | 2nd source resource type |
| I2      | 2nd source element type  |

### Returns

instance of {@link PullSource2}


from(io.github.epi155.pm.batch.step.SourceResource<S1,I1> source1, io.github.epi155.pm.batch.step.SourceResource<S2,I2> source2, io.github.epi155.pm.batch.step.SourceResource<S3,I3> source3)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Set three source resources<br>
 <pre>
 Pgm.from(src1, src2, src3).into(sink).proceed((it1, it2, it3, wr) -&gt; ...)
 </pre>

### Parameters

| Name    | Description              |
| ------- | ------------------------ |
| source1 | the 1st source resource  |
| source2 | the 2nd source resource  |
| source3 | the 3rd source resource  |
| S1      | 1st source resource type |
| I1      | 1st source element type  |
| S2      | 2nd source resource type |
| I2      | 2nd source element type  |
| S3      | 3rd source resource type |
| I3      | 3rd source element type  |

### Returns

instance of {@link PullSource3}


