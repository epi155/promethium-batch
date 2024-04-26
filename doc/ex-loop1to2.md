## Example: loop, one source two sinks

### tuple style

~~~java
val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
val snk1 = SinkResource.of(System.out::println);
val snk2 = SinkResource.of(System.err::println);

Batch.from(src).into(snk1,snk2).forEach(
        it -> {
            if (it %2 == 0) {
                return Tuple2.of(it,null);
            } else {
                return Tuple2.of(null,it);
            }
        });
~~~

### writer style

~~~java
val src = SourceResource.fromStream(IntStream.range(1, 20).boxed());
val snk1 = SinkResource.of(System.out::println);
val snk2 = SinkResource.of(System.err::println);

Batch.from(src).into(snk1,snk2).forEach(
        (it,wr1,wr2) -> {
            if (it %2 == 0) {
                wr1.accept(it);
            } else {
                wr2.accept(it);
            }
        });
~~~