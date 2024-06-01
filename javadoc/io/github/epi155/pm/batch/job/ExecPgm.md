Interface ExecPgm
=================
interface for running a program unconditionally

io.github.epi155.pm.batch.job.ExecPgm Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                       | Return type |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [execPgm(P p, C c, function.BiFunction<P,C,Integer> pgm)](#execpgmp-p-c-c-functionbifunctionp-c-integer-pgm)           | S           |
| **public abstract** | [execPgm(P p, String stepName, function.ToIntFunction<P> pgm)](#execpgmp-p-string-stepname-functiontointfunctionp-pgm) | S           |
| **public abstract** | [execPgm(C c, function.ToIntFunction<C> pgm)](#execpgmc-c-functiontointfunctionc-pgm)                                  | S           |
| **public abstract** | [execPgm(String stepName, IntSupplier pgm)](#execpgmstring-stepname-intsupplier-pgm)                                   | S           |
| **public abstract** | [execPgm(P p, C c, function.BiConsumer<P,C> pgm)](#execpgmp-p-c-c-functionbiconsumerp-c-pgm)                           | S           |
| **public abstract** | [execPgm(P p, String stepName, function.Consumer<P> pgm)](#execpgmp-p-string-stepname-functionconsumerp-pgm)           | S           |
| **public abstract** | [execPgm(C c, function.Consumer<C> pgm)](#execpgmc-c-functionconsumerc-pgm)                                            | S           |
| **public abstract** | [execPgm(String stepName, Runnable pgm)](#execpgmstring-stepname-runnable-pgm)                                         | S           |

Methods
=======
execPgm(P p, C c, function.BiFunction<P,C,Integer> pgm)
-------------------------------------------------------
Program launcher with user returnCode

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


execPgm(P p, String stepName, function.ToIntFunction<P> pgm)
------------------------------------------------------------
Program launcher with user returnCode

### Parameters

| Name     | Description                     |
| -------- | ------------------------------- |
| p        | job parameters                  |
| stepName | step name                       |
| pgm      | program (step) to execute       |
| P        | class to provide job parameters |

### Returns

instance of {@link S}


execPgm(C c, function.ToIntFunction<C> pgm)
-------------------------------------------
Program launcher with user returnCode

### Parameters

| Name | Description                        |
| ---- | ---------------------------------- |
| c    | program statistics                 |
| pgm  | program (step) to execute          |
| C    | class to manage program statistics |

### Returns

instance of {@link S}


execPgm(String stepName, IntSupplier pgm)
-----------------------------------------
Program launcher with user returnCode

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | step name                 |
| pgm      | program (step) to execute |

### Returns

instance of {@link S}


execPgm(P p, C c, function.BiConsumer<P,C> pgm)
-----------------------------------------------
Program launcher with automatic returnCode

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


execPgm(P p, String stepName, function.Consumer<P> pgm)
-------------------------------------------------------
Program launcher with automatic returnCode

### Parameters

| Name     | Description                     |
| -------- | ------------------------------- |
| p        | job parameters                  |
| stepName | step name                       |
| pgm      | program (step) to execute       |
| P        | class to provide job parameters |

### Returns

instance of {@link S}


execPgm(C c, function.Consumer<C> pgm)
--------------------------------------
Program launcher with automatic returnCode

### Parameters

| Name | Description                        |
| ---- | ---------------------------------- |
| c    | program statistics                 |
| pgm  | program (step) to execute          |
| C    | class to manage program statistics |

### Returns

instance of {@link S}


execPgm(String stepName, Runnable pgm)
--------------------------------------
Program launcher with automatic returnCode

### Parameters

| Name     | Description               |
| -------- | ------------------------- |
| stepName | step name                 |
| pgm      | program (step) to execute |

### Returns

instance of {@link S}


