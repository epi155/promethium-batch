Interface ForkPgm implements io.github.epi155.pm.batch.job.Joinable
===================================================================
interface for running a program unconditionally

io.github.epi155.pm.batch.job.ForkPgm Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                       | Return type |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [forkPgm(P p, C c, function.BiFunction<P,C,Integer> pgm)](#forkpgmp-p-c-c-functionbifunctionp-c-integer-pgm)           | S           |
| **public abstract** | [forkPgm(P p, String stepName, function.ToIntFunction<P> pgm)](#forkpgmp-p-string-stepname-functiontointfunctionp-pgm) | S           |
| **public abstract** | [forkPgm(C c, function.ToIntFunction<C> pgm)](#forkpgmc-c-functiontointfunctionc-pgm)                                  | S           |
| **public abstract** | [forkPgm(String stepName, IntSupplier pgm)](#forkpgmstring-stepname-intsupplier-pgm)                                   | S           |
| **public abstract** | [forkPgm(P p, C c, function.BiConsumer<P,C> pgm)](#forkpgmp-p-c-c-functionbiconsumerp-c-pgm)                           | S           |
| **public abstract** | [forkPgm(P p, String stepName, function.Consumer<P> pgm)](#forkpgmp-p-string-stepname-functionconsumerp-pgm)           | S           |
| **public abstract** | [forkPgm(C c, function.Consumer<C> pgm)](#forkpgmc-c-functionconsumerc-pgm)                                            | S           |
| **public abstract** | [forkPgm(String stepName, Runnable pgm)](#forkpgmstring-stepname-runnable-pgm)                                         | S           |

Methods
=======
forkPgm(P p, C c, function.BiFunction<P,C,Integer> pgm)
-------------------------------------------------------
Program launcher in background with user returnCode

### Parameters

| Name | Description                        |
| ---- | ---------------------------------- |
| p    | job parameters                     |
| c    | program statistics                 |
| pgm  | program (step) to execute          |
| P    | class to provide job parameters    |
| C    | class to manage program statistics |

### Returns

instance of {@link S}


forkPgm(P p, String stepName, function.ToIntFunction<P> pgm)
------------------------------------------------------------
Program launcher in background with user returnCode

### Parameters

| Name     | Description                     |
| -------- | ------------------------------- |
| p        | job parameters                  |
| stepName | step name                       |
| pgm      | program (step) to execute       |
| P        | class to provide job parameters |

### Returns

instance of {@link S}


forkPgm(C c, function.ToIntFunction<C> pgm)
-------------------------------------------
Program launcher in background with user returnCode

### Parameters

| Name | Description                        |
| ---- | ---------------------------------- |
| c    | program statistics                 |
| pgm  | program (step) to execute          |
| C    | class to manage program statistics |

### Returns

instance of {@link S}


forkPgm(String stepName, IntSupplier pgm)
-----------------------------------------
Program launcher in background with user returnCode

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | step name                 |
| pgm      | program (step) to execute |

### Returns

instance of {@link S}


forkPgm(P p, C c, function.BiConsumer<P,C> pgm)
-----------------------------------------------
Program launcher in background with automatic returnCode

### Parameters

| Name | Description                        |
| ---- | ---------------------------------- |
| p    | job parameters                     |
| c    | program statistics                 |
| pgm  | program (step) to execute          |
| P    | class to provide job parameters    |
| C    | class to manage program statistics |

### Returns

instance of {@link S}


forkPgm(P p, String stepName, function.Consumer<P> pgm)
-------------------------------------------------------
Program launcher in background with automatic returnCode

### Parameters

| Name     | Description                     |
| -------- | ------------------------------- |
| p        | job parameters                  |
| stepName | step name                       |
| pgm      | program (step) to execute       |
| P        | class to provide job parameters |

### Returns

instance of {@link S}


forkPgm(C c, function.Consumer<C> pgm)
--------------------------------------
Program launcher in background with automatic returnCode

### Parameters

| Name | Description                        |
| ---- | ---------------------------------- |
| c    | program statistics                 |
| pgm  | program (step) to execute          |
| C    | class to manage program statistics |

### Returns

instance of {@link S}


forkPgm(String stepName, Runnable pgm)
--------------------------------------
Program launcher in background with automatic returnCode

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | step name                 |
| pgm      | program (step) to execute |

### Returns

instance of {@link S}


