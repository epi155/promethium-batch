# promethium-batch

In a batch processing we have one (or more) data source (typically a file, but not only), one (or more) data destination (typically a file, but not only) and a processing process, which takes the data from the source, transforms them so you can write them to the destination.

If there are multiple data sources, it is the processing that must decide which source to read from, and it is not possible to parallelize the processing.

In the case of a single data source, the processing process can receive as input the data read from the data source, it does not require reading logic. Reading from the data source can be done by the batch infrastructure, and the processing process can be parallelized. In this case, the sequence of the data source may or may not be maintained in the data destinations (depending on the implementation of parallelization).

The following classes are used to generically manage data sources and destinations

* [`SourceResource`](./doc/source.md) data sources
* [`SinkResource`](./doc/sink.md) data destinations

If there is only one data source, the processing part can produce a tuple and the infrastructure will send the data to the destinations, in this case the processing format is:

~~~java
Loop.from(src).into(snk1,snk2).forEach(it -> { ... });
~~~

as an alternative to the processing part, in addition to the input value, the `Consumers` are provided to write data directly to the corresponding destinations, in this case the processing format is:

~~~java
Loop.from(src).into(snk1,snk2).forEach((it,wr1,wr2) -> { ... });
~~~

[loop examples](./doc/ex-loop1to2.md)

Both formats can be parallelized, but only the former allows an implementation that maintains the original data order.

If there is more than one data source, the processing part must be provided with `Suppliers` to read the data from the sources, and `Consumers` to write the data, in this case the processing format is:

~~~java
Loop.from(src1, src2).into(snk1, snk2)
        .proceed((rd1, rd2, wr1, wr2) -> { ... });
~~~

[example of balancing](./doc/ex-balance.md)

In the case of sequential processing, the separation of the processing into a part dedicated to reading data, one dedicated to writing data and a specific part for data processing does not offer particular advantages. This separation is preparatory to parallel processing.

A sequential elaboration in form

~~~java
Loop.from(src).into(...).forEach(...);
~~~

can be parallel transformed by replacing `forEach` in `forEachParallel`:

~~~java
Loop.from(src).into(...).forEachParallel(numTask, ...);
~~~

without any modification of the data processing part.