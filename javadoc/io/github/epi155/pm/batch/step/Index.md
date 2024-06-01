Package _io.github.epi155.pm.batch.step_
========================================
Package with classes to facilitate the development of job steps
---




Step example
 

<pre>


     public void run() {
         val src = SourceResource.bufferedReader(inFile);
         val snk = SinkResource.bufferedWriter(outFile);
         Pgm.from(src).into(snk).forEach(it -> it);
     }
 

</pre>

Enumerations
============
| Name            |
| --------------- |
| [Cond](Cond.md) |

Interfaces
==========
| Name                                  |
| ------------------------------------- |
| [AsyncWorker](AsyncWorker.md)         |
| [AsyncWorker2](AsyncWorker2.md)       |
| [AsyncWorker3](AsyncWorker3.md)       |
| [AsyncWorker4](AsyncWorker4.md)       |
| [AsyncWorker5](AsyncWorker5.md)       |
| [AsyncWorker6](AsyncWorker6.md)       |
| [AsyncWorker7](AsyncWorker7.md)       |
| [AsyncWorker8](AsyncWorker8.md)       |
| [IterableLoop](IterableLoop.md)       |
| [IterableLoop2](IterableLoop2.md)     |
| [IterableLoop3](IterableLoop3.md)     |
| [IterableLoop4](IterableLoop4.md)     |
| [IterableLoop5](IterableLoop5.md)     |
| [IterableLoop6](IterableLoop6.md)     |
| [IterableLoop7](IterableLoop7.md)     |
| [IterableLoop8](IterableLoop8.md)     |
| [LoopSource](LoopSource.md)           |
| [LoopSourceLayer](LoopSourceLayer.md) |
| [LoopSourceStd](LoopSourceStd.md)     |
| [ParallelLoop0](ParallelLoop0.md)     |
| [ParallelLoop1](ParallelLoop1.md)     |
| [ParallelLoop2](ParallelLoop2.md)     |
| [ParallelLoop3](ParallelLoop3.md)     |
| [ParallelLoop4](ParallelLoop4.md)     |
| [ParallelLoop5](ParallelLoop5.md)     |
| [ParallelLoop6](ParallelLoop6.md)     |
| [ParallelLoop7](ParallelLoop7.md)     |
| [ParallelLoop8](ParallelLoop8.md)     |
| [PullProcess2o1](PullProcess2o1.md)   |
| [PullProcess2o2](PullProcess2o2.md)   |
| [PullProcess2o3](PullProcess2o3.md)   |
| [PullProcess2o4](PullProcess2o4.md)   |
| [PullProcess2o5](PullProcess2o5.md)   |
| [PullProcess2o6](PullProcess2o6.md)   |
| [PullProcess2o7](PullProcess2o7.md)   |
| [PullProcess2o8](PullProcess2o8.md)   |
| [PullProcess3o1](PullProcess3o1.md)   |
| [PullProcess3o2](PullProcess3o2.md)   |
| [PullProcess3o3](PullProcess3o3.md)   |
| [PullProcess3o4](PullProcess3o4.md)   |
| [PullProcess3o5](PullProcess3o5.md)   |
| [PullProcess3o6](PullProcess3o6.md)   |
| [PullProcess3o7](PullProcess3o7.md)   |
| [PullProcess3o8](PullProcess3o8.md)   |
| [PullSource2](PullSource2.md)         |
| [PullSource3](PullSource3.md)         |
| [PullWorker2o0](PullWorker2o0.md)     |
| [PullWorker2o1](PullWorker2o1.md)     |
| [PullWorker2o2](PullWorker2o2.md)     |
| [PullWorker2o3](PullWorker2o3.md)     |
| [PullWorker2o4](PullWorker2o4.md)     |
| [PullWorker2o5](PullWorker2o5.md)     |
| [PullWorker2o6](PullWorker2o6.md)     |
| [PullWorker2o7](PullWorker2o7.md)     |
| [PullWorker2o8](PullWorker2o8.md)     |
| [PullWorker3o0](PullWorker3o0.md)     |
| [PullWorker3o1](PullWorker3o1.md)     |
| [PullWorker3o2](PullWorker3o2.md)     |
| [Pgm](Pgm.md)                         |
| [PullWorker3o3](PullWorker3o3.md)     |
| [PullWorker3o4](PullWorker3o4.md)     |
| [PullWorker3o5](PullWorker3o5.md)     |
| [PullWorker3o6](PullWorker3o6.md)     |
| [PullWorker3o7](PullWorker3o7.md)     |
| [PullWorker3o8](PullWorker3o8.md)     |
| [SinkResource](SinkResource.md)       |
| [SourceResource](SourceResource.md)   |
| [Worker](Worker.md)                   |
| [Worker2](Worker2.md)                 |
| [Worker3](Worker3.md)                 |
| [Worker4](Worker4.md)                 |
| [Worker5](Worker5.md)                 |
| [Worker6](Worker6.md)                 |
| [Worker7](Worker7.md)                 |
| [Worker8](Worker8.md)                 |

Classes
=======
| Name                                                      |
| --------------------------------------------------------- |
| [PmCloseableConsumer](PmCloseableConsumer.md)             |
| [PmCloseableIterable](PmCloseableIterable.md)             |
| [PmLoopSource](PmLoopSource.md)                           |
| [PmPullSource2](PmPullSource2.md)                         |
| [PmPullSource3](PmPullSource3.md)                         |
| [PmPushSource](PmPushSource.md)                           |
| [PmQueueWriter](PmQueueWriter.md)                         |
| [PmSinkResource](PmSinkResource.md)                       |
| [PmSourceResourceIterator](PmSourceResourceIterator.md)   |
| [PmSourceResourceStream](PmSourceResourceStream.md)       |
| [PmSourceResourceSupplier](PmSourceResourceSupplier.md)   |
| [Tuple1](Tuple1.md)                                       |
| [Tuple2](Tuple2.md)                                       |
| [Tuple3](Tuple3.md)                                       |
| [Tuple4](Tuple4.md)                                       |
| [Tuple5](Tuple5.md)                                       |
| [Tuple6](Tuple6.md)                                       |
| [Tuple7](Tuple7.md)                                       |
| [Tuple8](Tuple8.md)                                       |
| [PmSinkResourceTriggerable](PmSinkResourceTriggerable.md) |

