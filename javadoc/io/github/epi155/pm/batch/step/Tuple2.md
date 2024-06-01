Class Tuple2
============
tuple with two elements

io.github.epi155.pm.batch.step.Tuple2 Summary
-------
#### Constructors
| Visibility | Signature |
| ---------- | --------- |
| public     | Tuple2()  |
#### Fields
| Modifiers         | Field name   | Type |
| ----------------- | ------------ | ---- |
| **private final** | [t1](#o1-t1) | O1   |
| **private final** | [t2](#o2-t2) | O2   |
#### Methods
| Modifiers  | Method signature                                                                     | Return type |
| ---------- | ------------------------------------------------------------------------------------ | ----------- |
| **public** | [onT1(function.Consumer<? super O1> action)](#ont1functionconsumer?-super-o1-action) | void        |
| **public** | [onT2(function.Consumer<? super O2> action)](#ont2functionconsumer?-super-o2-action) | void        |

Fields
======
O1 t1
-----
*No description provided*


O2 t2
-----
*No description provided*


Methods
=======
onT1(function.Consumer<? super O1> action)
------------------------------------------
performs the indicated action if the component of the tuple is different from null and is not in an error state

### Parameters

| Name   | Description       |
| ------ | ----------------- |
| action | action to perform |


onT2(function.Consumer<? super O2> action)
------------------------------------------
performs the indicated action if the component of the tuple is different from null and is not in an error state

### Parameters

| Name   | Description       |
| ------ | ----------------- |
| action | action to perform |


