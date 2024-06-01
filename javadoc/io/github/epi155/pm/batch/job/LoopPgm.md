Interface LoopPgm
=================
interface to run a program multiple times if the previous step completes successfully

io.github.epi155.pm.batch.job.LoopPgm Summary
-------
#### Methods
| Modifiers           | Method signature                                                                                                                                            | Return type |
| ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| **public abstract** | [forEachPgm(P p, function.Function<Q,C> c, function.BiFunction<Q,C,Integer> pgm)](#foreachpgmp-p-functionfunctionq-c-c-functionbifunctionq-c-integer-pgm)   | S           |
| **public abstract** | [forEachPgm(P p, function.Function<Q,String> name, function.ToIntFunction<Q> pgm)](#foreachpgmp-p-functionfunctionq-string-name-functiontointfunctionq-pgm) | S           |
| **public abstract** | [forEachPgm(P p, function.Function<Q,C> c, function.BiConsumer<Q,C> pgm)](#foreachpgmp-p-functionfunctionq-c-c-functionbiconsumerq-c-pgm)                   | S           |
| **public abstract** | [forEachPgm(P p, function.Function<Q,String> name, function.Consumer<Q> pgm)](#foreachpgmp-p-functionfunctionq-string-name-functionconsumerq-pgm)           | S           |

Methods
=======
forEachPgm(P p, function.Function<Q,C> c, function.BiFunction<Q,C,Integer> pgm)
-------------------------------------------------------------------------------
Loop program launcher with user returnCode

### Parameters

| Name | Description                                    |
| ---- | ---------------------------------------------- |
| p    | job parameters                                 |
| c    | program statistics (by q)                      |
| pgm  | program (step) to execute                      |
| P    | class to provide job parameters                |
| Q    | class on which to repeat the program execution |
| C    | class to manage program statistics             |

### Returns

instance of {@link S}


forEachPgm(P p, function.Function<Q,String> name, function.ToIntFunction<Q> pgm)
--------------------------------------------------------------------------------
Loop program launcher with user returnCode

### Parameters

| Name | Description                                    |
| ---- | ---------------------------------------------- |
| p    | job parameters                                 |
| name | step name (by q)                               |
| pgm  | program (step) to execute                      |
| P    | class to provide job parameters                |
| Q    | class on which to repeat the program execution |

### Returns

instance of {@link S}


forEachPgm(P p, function.Function<Q,C> c, function.BiConsumer<Q,C> pgm)
-----------------------------------------------------------------------
Loop program launcher with automatic returnCode

### Parameters

| Name | Description                                    |
| ---- | ---------------------------------------------- |
| p    | job parameters                                 |
| c    | program statistics (by q)                      |
| pgm  | program (step) to execute                      |
| P    | class to provide job parameters                |
| Q    | class on which to repeat the program execution |
| C    | class to manage program statistics             |

### Returns

instance of {@link S}


forEachPgm(P p, function.Function<Q,String> name, function.Consumer<Q> pgm)
---------------------------------------------------------------------------
Loop program launcher with automatic returnCode

### Parameters

| Name | Description                                    |
| ---- | ---------------------------------------------- |
| p    | job parameters                                 |
| name | step name (by q)                               |
| pgm  | program (step) to execute                      |
| P    | class to provide job parameters                |
| Q    | class on which to repeat the program execution |

### Returns

instance of {@link S}


