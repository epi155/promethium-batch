Class PmSourceResourceSupplier implements io.github.epi155.pm.batch.step.SourceResource<U,I>
============================================================================================


io.github.epi155.pm.batch.step.PmSourceResourceSupplier Summary
-------
#### Constructors
| Visibility | Signature                                                                                                              |
| ---------- | ---------------------------------------------------------------------------------------------------------------------- |
| public     | PmSourceResourceSupplier(java.util.function.Supplier<U>,java.util.function.Function<U,java.util.function.Supplier<I>>) |
#### Fields
| Modifiers         | Field name                                                            | Type                                      |
| ----------------- | --------------------------------------------------------------------- | ----------------------------------------- |
| **private final** | [ctor](#javautilfunctionsupplieru-ctor)                               | function.Supplier<U>                      |
| **private final** | [reader](#javautilfunctionfunctionu-javautilfunctionsupplieri-reader) | function.Function<U,function.Supplier<I>> |
#### Methods
| Modifiers  | Method signature              | Return type          |
| ---------- | ----------------------------- | -------------------- |
| **public** | [get()](#get)                 | U                    |
| **public** | [iterator(U u)](#iteratoru-u) | Iterator<I>          |
| **public** | [supplier(U u)](#supplieru-u) | function.Supplier<I> |

Fields
======
function.Supplier<U> ctor
-----------------------------------
*No description provided*


function.Function<U,function.Supplier<I>> reader
--------------------------------------------------------------------
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

