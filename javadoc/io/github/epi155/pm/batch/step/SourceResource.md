Interface SourceResource
========================
interface to manage a source type resource (reader).

io.github.epi155.pm.batch.step.SourceResource Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                            | Return type                                                                  |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------- |
| **public static**   | [fromIterator(function.Supplier<U> ctor, function.Function<U,Iterator<I>> reader)](#fromiteratorfunctionsupplieru-ctor-functionfunctionu-iteratori-reader)                  | io.github.epi155.pm.batch.step.SourceResource<U,I>                           |
| **public static**   | [fromIterator(function.Supplier<U> ctor)](#fromiteratorfunctionsupplieru-ctor)                                                                                              | io.github.epi155.pm.batch.step.SourceResource<U,I>                           |
| **public static**   | [fromStream(stream.Stream<I> stream)](#fromstreamstreamstreami-stream)                                                                                                      | io.github.epi155.pm.batch.step.SourceResource<U,I>                           |
| **public static**   | [fromIterator(Iterable<I> iterable)](#fromiteratoriterablei-iterable)                                                                                                       | io.github.epi155.pm.batch.step.SourceResource<?,I>                           |
| **public static**   | [fromSupplier(function.Supplier<U> ctor, function.Function<U,function.Supplier<I>> reader)](#fromsupplierfunctionsupplieru-ctor-functionfunctionu-functionsupplieri-reader) | io.github.epi155.pm.batch.step.SourceResource<U,I>                           |
| **public static**   | [fromSupplier(function.Supplier<U> ctor)](#fromsupplierfunctionsupplieru-ctor)                                                                                              | io.github.epi155.pm.batch.step.SourceResource<U,I>                           |
| **public static**   | [bufferedReader(File file, Charset cs, Runnable inc)](#bufferedreaderfile-file-charset-cs-runnable-inc)                                                                     | io.github.epi155.pm.batch.step.SourceResource<java.io.BufferedReader,String> |
| **public static**   | [bufferedReader(File file, Runnable inc)](#bufferedreaderfile-file-runnable-inc)                                                                                            | io.github.epi155.pm.batch.step.SourceResource<java.io.BufferedReader,String> |
| **public static**   | [bufferedReader(File file, Charset cs)](#bufferedreaderfile-file-charset-cs)                                                                                                | io.github.epi155.pm.batch.step.SourceResource<java.io.BufferedReader,String> |
| **public static**   | [bufferedReader(File file)](#bufferedreaderfile-file)                                                                                                                       | io.github.epi155.pm.batch.step.SourceResource<java.io.BufferedReader,String> |
| **public static**   | [bufferedReader(File file, function.Function<String,I> dec, Charset cs, Runnable inc)](#bufferedreaderfile-file-functionfunctionstring-i-dec-charset-cs-runnable-inc)       | io.github.epi155.pm.batch.step.SourceResource<java.io.BufferedReader,I>      |
| **public static**   | [bufferedReader(File file, function.Function<String,I> dec, Charset cs)](#bufferedreaderfile-file-functionfunctionstring-i-dec-charset-cs)                                  | io.github.epi155.pm.batch.step.SourceResource<java.io.BufferedReader,I>      |
| **public static**   | [bufferedReader(File file, function.Function<String,I> dec, Runnable inc)](#bufferedreaderfile-file-functionfunctionstring-i-dec-runnable-inc)                              | io.github.epi155.pm.batch.step.SourceResource<java.io.BufferedReader,I>      |
| **public static**   | [bufferedReader(File file, function.Function<String,I> dec)](#bufferedreaderfile-file-functionfunctionstring-i-dec)                                                         | io.github.epi155.pm.batch.step.SourceResource<java.io.BufferedReader,I>      |
| **public abstract** | [get()](#get)                                                                                                                                                               | U                                                                            |
| **public abstract** | [iterator(U u)](#iteratoru-u)                                                                                                                                               | Iterator<I>                                                                  |
| **public abstract** | [supplier(U u)](#supplieru-u)                                                                                                                                               | function.Supplier<I>                                                         |

Methods
=======
fromIterator(function.Supplier<U> ctor, function.Function<U,Iterator<I>> reader)
--------------------------------------------------------------------------------
static constructor

### Parameters

| Name   | Description                                                |
| ------ | ---------------------------------------------------------- |
| ctor   | resource constructor provider                              |
| reader | iterator method of the resource from which to receive data |
| U      | resource type                                              |
| I      | type of item produced by the resource                      |

### Returns

instance of {@link SourceResource}


fromIterator(function.Supplier<U> ctor)
---------------------------------------
static constructor (Iterable resource)

### Parameters

| Name | Description                           |
| ---- | ------------------------------------- |
| ctor | resource constructor provider         |
| U    | resource type                         |
| I    | type of item produced by the resource |

### Returns

instance of {@link SourceResource}


fromStream(stream.Stream<I> stream)
-----------------------------------
static constructor (Stream resource)

### Parameters

| Name   | Description                           |
| ------ | ------------------------------------- |
| stream | the stream of data                    |
| U      | resource type                         |
| I      | type of item produced by the resource |

### Returns

instance of {@link SourceResource}


fromIterator(Iterable<I> iterable)
----------------------------------
static constructor simple resource (does not require closing the resource)

### Parameters

| Name     | Description                                |
| -------- | ------------------------------------------ |
| iterable | iterable method from which to receive data |
| I        | type of item produced by the resource      |

### Returns

instance of {@link SourceResource}


fromSupplier(function.Supplier<U> ctor, function.Function<U,function.Supplier<I>> reader)
-----------------------------------------------------------------------------------------
static constructor

### Parameters

| Name   | Description                                                |
| ------ | ---------------------------------------------------------- |
| ctor   | resource constructor provider                              |
| reader | supplier method of the resource from which to receive data |
| U      | resource type                                              |
| I      | type of item produced by the resource                      |

### Returns

instance of {@link SourceResource}


fromSupplier(function.Supplier<U> ctor)
---------------------------------------
static constructor (Supplier resource)

### Parameters

| Name | Description                           |
| ---- | ------------------------------------- |
| ctor | resource constructor provider         |
| U    | resource type                         |
| I    | type of item produced by the resource |

### Returns

instance of {@link SourceResource}


bufferedReader(File file, Charset cs, Runnable inc)
---------------------------------------------------
static constructor of {@code SourceResource<BufferedReader, String>}

### Parameters

| Name | Description                           |
| ---- | ------------------------------------- |
| file | file                                  |
| cs   | charset                               |
| inc  | action after read (usually counter++) |

### Returns

instance of {@link SourceResource}


bufferedReader(File file, Runnable inc)
---------------------------------------
static constructor of {@code SourceResource<BufferedReader, String>} with default charset

### Parameters

| Name | Description                           |
| ---- | ------------------------------------- |
| file | file                                  |
| inc  | action after read (usually counter++) |

### Returns

instance of {@link SourceResource}


bufferedReader(File file, Charset cs)
-------------------------------------
static constructor of {@code SourceResource<BufferedReader, String>}

### Parameters

| Name | Description |
| ---- | ----------- |
| file | file        |
| cs   | charset     |

### Returns

instance of {@link SourceResource}


bufferedReader(File file)
-------------------------
static constructor of {@code SourceResource<BufferedReader, String>} with default charset

### Parameters

| Name | Description |
| ---- | ----------- |
| file | file        |

### Returns

instance of {@link SourceResource}


bufferedReader(File file, function.Function<String,I> dec, Charset cs, Runnable inc)
------------------------------------------------------------------------------------
static constructor of {@code SourceResource<BufferedReader, I>} with decoder String to I

### Parameters

| Name | Description                           |
| ---- | ------------------------------------- |
| file | file                                  |
| dec  | decoder function                      |
| cs   | charset                               |
| inc  | action after read (usually counter++) |
| I    | type read                             |

### Returns

instance of {@link SourceResource}


bufferedReader(File file, function.Function<String,I> dec, Charset cs)
----------------------------------------------------------------------
static constructor of {@code SourceResource<BufferedReader, I>} with decoder String to I

### Parameters

| Name | Description      |
| ---- | ---------------- |
| file | file             |
| dec  | decoder function |
| cs   | charset          |
| I    | type read        |

### Returns

instance of {@link SourceResource}


bufferedReader(File file, function.Function<String,I> dec, Runnable inc)
------------------------------------------------------------------------
static constructor of {@code SourceResource<BufferedReader, I>}
 with decoder String to I (default charset)

### Parameters

| Name | Description                           |
| ---- | ------------------------------------- |
| file | file                                  |
| dec  | decoder function                      |
| inc  | action after read (usually counter++) |
| I    | type read                             |

### Returns

instance of {@link SourceResource}


bufferedReader(File file, function.Function<String,I> dec)
----------------------------------------------------------
static constructor of {@code SourceResource<BufferedReader, I>}
 with decoder String to I (default charset)

### Parameters

| Name | Description      |
| ---- | ---------------- |
| file | file             |
| dec  | decoder function |
| I    | type read        |

### Returns

instance of {@link SourceResource}


get()
-----
provides the native resource

### Returns

native resource


iterator(U u)
-------------
iterator of the produced data

### Parameters

| Name | Description     |
| ---- | --------------- |
| u    | native resource |

### Returns

iterator of the produced data


supplier(U u)
-------------
supplier of the produced data

### Parameters

| Name | Description     |
| ---- | --------------- |
| u    | native resource |

### Returns

supplier of the produced data


