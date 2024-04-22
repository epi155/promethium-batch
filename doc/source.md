## Source Resource

~~~java
<I> SourceResource<?,I> src = SourceResource.fromIterator(Iterable<I> iterable);
<U extends AutoCloseable & Iterable<I>, I> SourceResource<U,I> src = SourceResource.fromIterator(Supplier<U> ctor);
<U extends AutoCloseable, I> SourceResource<U,I> src = SourceResource.fromIterator(Supplier<U> ctor, Function<U,Iterator<I>> reader);
<U extends Stream<I>, I> SourceResource<U,I> src = SourceResource.fromStream(Stream<I> stream)
<U extends AutoCloseable & Supplier<I>, I> SourceResource<U,I> src = SourceResource.fromSupplier(Supplier<U> ctor);
<U extends AutoCloseable, I> SourceResource<U,I> src = SourceResource.fromSupplier(Supplier<U> ctor, Function<U,Supplier<I>> reader);
~~~