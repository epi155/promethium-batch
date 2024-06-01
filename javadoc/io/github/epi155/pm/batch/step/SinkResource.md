Interface SinkResource
======================
interface to manage a sink (writer) type resource

io.github.epi155.pm.batch.step.SinkResource Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                                      | Return type                                                                |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------- |
| **public static**   | [of(function.Supplier<U> ctor, function.BiConsumer<U,O> writer)](#offunctionsupplieru-ctor-functionbiconsumeru-o-writer)                                              | io.github.epi155.pm.batch.step.SinkResource<U,O>                           |
| **public static**   | [of(function.Supplier<U> ctor, function.BiConsumer<U,O> writer, Runnable action)](#offunctionsupplieru-ctor-functionbiconsumeru-o-writer-runnable-action)             | io.github.epi155.pm.batch.step.SinkResource<U,O>                           |
| **public static**   | [of(function.Consumer<O> writer)](#offunctionconsumero-writer)                                                                                                        | io.github.epi155.pm.batch.step.SinkResource<?,O>                           |
| **public static**   | [of(function.Consumer<O> writer, Runnable action)](#offunctionconsumero-writer-runnable-action)                                                                       | io.github.epi155.pm.batch.step.SinkResource<?,O>                           |
| **public static**   | [bufferedWriter(File file, Charset cs, Runnable inc)](#bufferedwriterfile-file-charset-cs-runnable-inc)                                                               | io.github.epi155.pm.batch.step.SinkResource<java.io.BufferedWriter,String> |
| **public static**   | [bufferedWriter(File file, Runnable inc)](#bufferedwriterfile-file-runnable-inc)                                                                                      | io.github.epi155.pm.batch.step.SinkResource<java.io.BufferedWriter,String> |
| **public static**   | [bufferedWriter(File file, Charset cs)](#bufferedwriterfile-file-charset-cs)                                                                                          | io.github.epi155.pm.batch.step.SinkResource<java.io.BufferedWriter,String> |
| **public static**   | [bufferedWriter(File file)](#bufferedwriterfile-file)                                                                                                                 | io.github.epi155.pm.batch.step.SinkResource<java.io.BufferedWriter,String> |
| **public static**   | [bufferedWriter(File file, function.Function<O,String> enc, Charset cs, Runnable inc)](#bufferedwriterfile-file-functionfunctiono-string-enc-charset-cs-runnable-inc) | io.github.epi155.pm.batch.step.SinkResource<java.io.BufferedWriter,O>      |
| **public static**   | [bufferedWriter(File file, function.Function<O,String> enc, Charset cs)](#bufferedwriterfile-file-functionfunctiono-string-enc-charset-cs)                            | io.github.epi155.pm.batch.step.SinkResource<java.io.BufferedWriter,O>      |
| **public static**   | [bufferedWriter(File file, function.Function<O,String> enc, Runnable inc)](#bufferedwriterfile-file-functionfunctiono-string-enc-runnable-inc)                        | io.github.epi155.pm.batch.step.SinkResource<java.io.BufferedWriter,O>      |
| **public static**   | [bufferedWriter(File file, function.Function<O,String> enc)](#bufferedwriterfile-file-functionfunctiono-string-enc)                                                   | io.github.epi155.pm.batch.step.SinkResource<java.io.BufferedWriter,O>      |
| **public abstract** | [get()](#get)                                                                                                                                                         | U                                                                          |
| **public abstract** | [accept(U u, O o)](#acceptu-u-o-o)                                                                                                                                    | void                                                                       |

Methods
=======
of(function.Supplier<U> ctor, function.BiConsumer<U,O> writer)
--------------------------------------------------------------
static constructor

 <pre>
 val sink = SinkResource.of(
      () -> Files.newBufferedWriter(Path.of("foo.txt")),
      (wr, s) -> wr::write);
 </pre>{@code try/catch} omitted

### Parameters

| Name   | Description                            |
| ------ | -------------------------------------- |
| ctor   | resource constructor provider          |
| writer | method of the resource to send data to |
| U      | resource type                          |
| O      | element type consumed by the resource  |

### Returns

instance of {@link SinkResource}


of(function.Supplier<U> ctor, function.BiConsumer<U,O> writer, Runnable action)
-------------------------------------------------------------------------------
static constructor

### Parameters

| Name   | Description                            |
| ------ | -------------------------------------- |
| ctor   | resource constructor provider          |
| writer | method of the resource to send data to |
| action | action invoked after writing the data  |
| U      | resource type                          |
| O      | element type consumed by the resource  |

### Returns

instance of {@link SinkResource}


of(function.Consumer<O> writer)
-------------------------------
static constructor simple resource (does not require closing the resource)
 <pre>
 val sink = SinkResource.of(System.out::println);
 </pre>

### Parameters

| Name   | Description           |
| ------ | --------------------- |
| writer | data consumer         |
| O      | element type consumed |

### Returns

instance of {@link SinkResource}


of(function.Consumer<O> writer, Runnable action)
------------------------------------------------
static constructor simple resource (does not require closing the resource)
 <pre>val snk = SinkResource.of(System.out::println, c::inc);</pre>

### Parameters

| Name   | Description                           |
| ------ | ------------------------------------- |
| writer | data consumer                         |
| action | action invoked after writing the data |
| O      | element type consumed                 |

### Returns

instance of {@link SinkResource}


bufferedWriter(File file, Charset cs, Runnable inc)
---------------------------------------------------
static constructor of {@code <BufferedWriter, String>}

### Parameters

| Name | Description                            |
| ---- | -------------------------------------- |
| file | file                                   |
| cs   | charset                                |
| inc  | action after write (usually counter++) |

### Returns

instance of {@link SinkResource}


bufferedWriter(File file, Runnable inc)
---------------------------------------
static constructor of {@code <BufferedWriter, String>} with default charset

### Parameters

| Name | Description                            |
| ---- | -------------------------------------- |
| file | file                                   |
| inc  | action after write (usually counter++) |

### Returns

instance of {@link SinkResource}


bufferedWriter(File file, Charset cs)
-------------------------------------
static constructor of {@code <BufferedWriter, String>}

### Parameters

| Name | Description |
| ---- | ----------- |
| file | file        |
| cs   | charset     |

### Returns

instance of {@link SinkResource}


bufferedWriter(File file)
-------------------------
static constructor of {@code <BufferedWriter, String>} with default charset

### Parameters

| Name | Description |
| ---- | ----------- |
| file | file        |

### Returns

instance of {@link SinkResource}


bufferedWriter(File file, function.Function<O,String> enc, Charset cs, Runnable inc)
------------------------------------------------------------------------------------
static constructor of {@code <BufferedWriter, O>} with encoder O to String

### Parameters

| Name | Description                            |
| ---- | -------------------------------------- |
| file | file                                   |
| enc  | encoder function                       |
| cs   | charset                                |
| inc  | action after write (usually counter++) |
| O    | type write                             |

### Returns

instance of {@link SinkResource}


bufferedWriter(File file, function.Function<O,String> enc, Charset cs)
----------------------------------------------------------------------
static constructor of {@code <BufferedWriter, O>} with encoder O to String

### Parameters

| Name | Description      |
| ---- | ---------------- |
| file | file             |
| enc  | encoder function |
| cs   | charset          |
| O    | type write       |

### Returns

instance of {@link SinkResource}


bufferedWriter(File file, function.Function<O,String> enc, Runnable inc)
------------------------------------------------------------------------
static constructor of {@code <BufferedWriter, O>}
 with encoder O to String (default charset)

### Parameters

| Name | Description                            |
| ---- | -------------------------------------- |
| file | file                                   |
| enc  | encoder function                       |
| inc  | action after write (usually counter++) |
| O    | type write                             |

### Returns

instance of {@link SinkResource}


bufferedWriter(File file, function.Function<O,String> enc)
----------------------------------------------------------
static constructor of {@code <BufferedWriter, O>}
 with encoder O to String (default charset)

### Parameters

| Name | Description      |
| ---- | ---------------- |
| file | file             |
| enc  | encoder function |
| O    | type write       |

### Returns

instance of {@link SinkResource}


get()
-----
provides the native resource

### Returns

native resource


accept(U u, O o)
----------------
sends (writes) the data to the resource

### Parameters

| Name | Description                          |
| ---- | ------------------------------------ |
| u    | native resource                      |
| o    | data to send (write) to the resource |


