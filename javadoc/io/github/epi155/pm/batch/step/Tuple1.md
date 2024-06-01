Class Tuple1
============
tuple with one elements

io.github.epi155.pm.batch.step.Tuple1 Summary
-------
#### Constructors
| Visibility | Signature |
| ---------- | --------- |
|            | Tuple1()  |
#### Fields
| Modifiers                | Field name                                       | Type                                     |
| ------------------------ | ------------------------------------------------ | ---------------------------------------- |
| **private static final** | [EMPTY](#iogithubepi155pmbatchsteptuple1?-empty) | io.github.epi155.pm.batch.step.Tuple1<?> |
| **private final**        | [t1](#o1-t1)                                     | O1                                       |
#### Methods
| Modifiers            | Method signature                                                                     | Return type                              |
| -------------------- | ------------------------------------------------------------------------------------ | ---------------------------------------- |
| **protected static** | [empty()](#empty)                                                                    | io.github.epi155.pm.batch.step.Tuple1<T> |
| **public**           | [onT1(function.Consumer<? super O1> action)](#ont1functionconsumer?-super-o1-action) | boolean                                  |

Fields
======
io.github.epi155.pm.batch.step.Tuple1<?> EMPTY
----------------------------------------------
*No description provided*


O1 t1
-----
*No description provided*


Methods
=======
empty()
-------
*No method description provided*


onT1(function.Consumer<? super O1> action)
------------------------------------------
performs the indicated action if the component of the tuple is different from null and is not in an error state

### Parameters

| Name   | Description       |
| ------ | ----------------- |
| action | action to perform |

### Returns

{@code true} if action is performed else {@code false}


