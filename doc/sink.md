## Sink Resource

~~~java
<O> SinkResource<?,O> snk = SinkResource.of(Consumer<O> writer);
<U extends AutoCloseable, O> SinkResource<U,O> snk = SinkResource.of(Supplier<U> ctor, BiConsumer<U,O> writer);
~~~