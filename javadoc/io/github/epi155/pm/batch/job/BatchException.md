Class BatchException extends java.lang.RuntimeException
=======================================================
batch wrapper exception

io.github.epi155.pm.batch.job.BatchException Summary
-------
#### Constructors
| Visibility | Signature                                                                    |
| ---------- | ---------------------------------------------------------------------------- |
| public     | BatchException(java.lang.Throwable)                                          |
| public     | BatchException(int,java.lang.String,java.lang.Object...)                     |
| public     | BatchException(int,java.lang.Throwable,java.lang.String,java.lang.Object...) |
#### Fields
| Modifiers                | Field name                                 | Type |
| ------------------------ | ------------------------------------------ | ---- |
| **private static final** | [serialVersionUID](#long-serialversionuid) | long |
| **private final**        | [returnCode](#int-returncode)              | int  |
#### Methods
| Modifiers         | Method signature                                                                | Return type |
| ----------------- | ------------------------------------------------------------------------------- | ----------- |
| **public static** | [placeOf(StackTraceElement[] stackTrace)](#placeofstacktraceelement-stacktrace) | String      |

Fields
======
long serialVersionUID
---------------------
*No description provided*


int returnCode
--------------
Error code associated with the exception


Methods
=======
placeOf(StackTraceElement[] stackTrace)
---------------------------------------
Constructs the string with the location where the error occurred

### Parameters

| Name       | Description            |
| ---------- | ---------------------- |
| stackTrace | full stack trace array |

### Returns

String with error position


