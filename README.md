# promethium-batch

In the mainframe world, a batch process is normally divided into several parts (step), and these are coordinated by a control script (JCL).

This library provides classes to facilitate development of the processing of the individual steps and to coordinate the execution of the steps.

A minimal example of a single step batch job is:

~~~java
public Integer call() {
    return JCL.getInstance().job("job01")
        .execPgm("step01", step01::run)
        .complete();
}
~~~

where `job01` is the name of the job, `step01` is the name of the step and `step01::run` is the reference to the method that executes the step.

The execution of the job produces a final report in the log of the type:

~~~
  Name   !  rc  !     Date-Time Start/Skip      !         Date-Time End         !       Lapse
---------+------+-------------------------------+-------------------------------+-------------------
step01...!    0 ! 2024-05-17T14:59:12.03243396  ! 2024-05-17T14:59:12.590157083 ! 00:00:00.557723123
---------+------+-------------------------------+-------------------------------+-------------------
job01....!    0 ! 2024-05-17T14:59:12.028019213 ! 2024-05-17T14:59:12.590998545 ! 00:00:00.562979332
~~~

A minimal example of a step implementation is

~~~java
    public void run() {
        val src = SourceResource.bufferedReader(inFile);
        val snk = SinkResource.bufferedWriter(outFile);
        Pgm.from(src).into(snk).forEach(it -> it);
    }
~~~

where `src` is the data source constructed from the input file, `snk` is the data destination constructed from the output file, and the `forEach` method argument provides the rule for transforming the input into the output.

Naturally a step can be written not using the proposed classes, it is sufficient that the method that implements the step returns a `void` or an `int`, in the latter case the returned value indicates whether the step ended correctly, with a warning or with an error.

These examples are just a starting point, the javadoc documentation provides explanations of all the methods, to be able to develop complex jobs and complex steps.


~~~java
public Integer call() {
    return JCL.getInstance().job("job02")
        .forkPgm("sort1", this::sort1)
        .forkPgm("sort2", this::sort2)
        .join()
        .cond(0,NE).execPgm("balance", this::balance)
        .execPgm("clean", this::clean)
        .complete();
}
~~~

