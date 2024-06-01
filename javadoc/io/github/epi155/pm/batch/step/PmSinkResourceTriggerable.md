Class PmSinkResourceTriggerable extends io.github.epi155.pm.batch.step.PmSinkResource<U,O>
==========================================================================================


io.github.epi155.pm.batch.step.PmSinkResourceTriggerable Summary
-------
#### Constructors
| Visibility | Signature                                                                                                       |
| ---------- | --------------------------------------------------------------------------------------------------------------- |
|            | PmSinkResourceTriggerable(java.util.function.Supplier<U>,java.util.function.BiConsumer<U,O>,java.lang.Runnable) |
#### Fields
| Modifiers         | Field name                         | Type     |
| ----------------- | ---------------------------------- | -------- |
| **private final** | [action](#javalangrunnable-action) | Runnable |
#### Methods
| Modifiers  | Method signature                   | Return type |
| ---------- | ---------------------------------- | ----------- |
| **public** | [accept(U u, O o)](#acceptu-u-o-o) | void        |

Fields
======
Runnable action
-------------------------
*No description provided*


Methods
=======
accept(U u, O o)
----------------
### Overrides/Implements:
accept(U u, O o) from io.github.epi155.pm.batch.step.PmSinkResource

*No method description provided*

### Parameters

| Name | Description               |
| ---- | ------------------------- |
| u    | *No description provided* |
| o    | *No description provided* |

