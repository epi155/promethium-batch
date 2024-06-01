Class Tuple4
============
tuple with four elements

io.github.epi155.pm.batch.step.Tuple4 Summary
-------
#### Constructors
| Visibility | Signature |
| ---------- | --------- |
| public     | Tuple4()  |
#### Fields
| Modifiers         | Field name   | Type |
| ----------------- | ------------ | ---- |
| **private final** | [t1](#o1-t1) | O1   |
| **private final** | [t2](#o2-t2) | O2   |
| **private final** | [t3](#o3-t3) | O3   |
| **private final** | [t4](#o4-t4) | O4   |
#### Methods
| Modifiers  | Method signature                                                                     | Return type |
| ---------- | ------------------------------------------------------------------------------------ | ----------- |
| **public** | [onT1(function.Consumer<? super O1> action)](#ont1functionconsumer?-super-o1-action) | void        |
| **public** | [onT2(function.Consumer<? super O2> action)](#ont2functionconsumer?-super-o2-action) | void        |
| **public** | [onT3(function.Consumer<? super O3> action)](#ont3functionconsumer?-super-o3-action) | void        |
| **public** | [onT4(function.Consumer<? super O4> action)](#ont4functionconsumer?-super-o4-action) | void        |

Fields
======
O1 t1
-----
*No description provided*


O2 t2
-----
*No description provided*


O3 t3
-----
*No description provided*


O4 t4
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


onT3(function.Consumer<? super O3> action)
------------------------------------------
performs the indicated action if the component of the tuple is different from null and is not in an error state

### Parameters

| Name   | Description       |
| ------ | ----------------- |
| action | action to perform |


onT4(function.Consumer<? super O4> action)
------------------------------------------
performs the indicated action if the component of the tuple is different from null and is not in an error state

### Parameters

| Name   | Description       |
| ------ | ----------------- |
| action | action to perform |


