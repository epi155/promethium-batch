Class PmSinkResource implements io.github.epi155.pm.batch.step.SinkResource<U,O>
================================================================================


io.github.epi155.pm.batch.step.PmSinkResource Summary
-------
#### Constructors
| Visibility | Signature                                                                         |
| ---------- | --------------------------------------------------------------------------------- |
| public     | PmSinkResource(java.util.function.Supplier<U>,java.util.function.BiConsumer<U,O>) |
#### Fields
| Modifiers         | Field name                                          | Type                     |
| ----------------- | --------------------------------------------------- | ------------------------ |
| **private final** | [ctor](#javautilfunctionsupplieru-ctor)             | function.Supplier<U>     |
| **private final** | [consumer](#javautilfunctionbiconsumeru-o-consumer) | function.BiConsumer<U,O> |
#### Methods
| Modifiers  | Method signature                   | Return type |
| ---------- | ---------------------------------- | ----------- |
| **public** | [get()](#get)                      | U           |
| **public** | [accept(U u, O o)](#acceptu-u-o-o) | void        |

Fields
======
function.Supplier<U> ctor
-----------------------------------
*No description provided*


function.BiConsumer<U,O> consumer
-------------------------------------------
*No description provided*


Methods
=======
get()
-----
### Overrides/Implements:
get() from io.github.epi155.pm.batch.step.SinkResource

*No method description provided*


accept(U u, O o)
----------------
### Overrides/Implements:
accept(U u, O o) from io.github.epi155.pm.batch.step.SinkResource

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| u    | *No description provided* |
| o    | *No description provided* |

