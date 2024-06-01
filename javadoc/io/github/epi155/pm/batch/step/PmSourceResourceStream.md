Class PmSourceResourceStream implements io.github.epi155.pm.batch.step.SourceResource<U,I>
==========================================================================================


io.github.epi155.pm.batch.step.PmSourceResourceStream Summary
-------
#### Constructors
| Visibility | Signature                                          |
| ---------- | -------------------------------------------------- |
| public     | PmSourceResourceStream(java.util.stream.Stream<I>) |
#### Fields
| Modifiers         | Field name                              | Type             |
| ----------------- | --------------------------------------- | ---------------- |
| **private final** | [stream](#javautilstreamstreami-stream) | stream.Stream<I> |
| **private final** | [iterator](#javautiliteratori-iterator) | Iterator<I>      |
#### Methods
| Modifiers  | Method signature              | Return type          |
| ---------- | ----------------------------- | -------------------- |
| **public** | [get()](#get)                 | U                    |
| **public** | [iterator(U u)](#iteratoru-u) | Iterator<I>          |
| **public** | [supplier(U u)](#supplieru-u) | function.Supplier<I> |

Fields
======
stream.Stream<I> stream
---------------------------------
*No description provided*


Iterator<I> iterator
------------------------------
*No description provided*


Methods
=======
get()
-----
### Overrides/Implements:
get() from io.github.epi155.pm.batch.step.SourceResource

*No method description provided*


iterator(U u)
-------------
### Overrides/Implements:
iterator(U u) from io.github.epi155.pm.batch.step.SourceResource

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| u    | *No description provided* |

supplier(U u)
-------------
### Overrides/Implements:
supplier(U u) from io.github.epi155.pm.batch.step.SourceResource

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| u    | *No description provided* |

