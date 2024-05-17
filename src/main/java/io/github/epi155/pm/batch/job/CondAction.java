package io.github.epi155.pm.batch.job;

/**
 * interface that contains conditional operations to drive the execution of programs or procedures
 *
 * @param <T> class to operate on
 */
public interface CondAction<T> {
    /**
     * condition for not performing the next operation
     * <p>
     * example
     * <pre>
     * JCL.getInstance().job("job01")
     *     .execPgm("step01", this::step01)
     *     <b>.cond(0,NE)</b>.execPgm("step02", this::step02)
     *     .complete();
     * </pre>
     * if the previous program (step01) ends with a return code other than zero (NE),
     * it does not execute the program that follows the condition (step02)
     *
     * @param cc   value to test
     * @param cond condition to be tested
     * @return state to which to apply the operation
     */
    JobAction<T> cond(int cc, Cond cond);

    /**
     * condition for performing the next operation
     * <p>
     * example
     * <pre>
     * JCL.getInstance().job("job01")
     *     .execPgm("step01", this::step01)
     *     <b>.when(0,EQ)</b>.execPgm("step02", this::step02)
     *     .complete();
     * </pre>
     * if the previous program (step01) ends with a return code equal (EQ) to zero,
     * executes the program that follows the condition (step02)
     *
     * @param cc   value to test
     * @param cond condition to be tested
     * @return state to which to apply the operation
     */
    default JobAction<T> when(int cc, Cond cond) {
        return cond(cc, cond.not());
    }

    /**
     * condition for not performing the next operation
     * <p>
     * if the given name does not exist or has not been executed, the next program is executed
     * <p>
     * example
     * <pre>
     * JCL.getInstance().job("job01")
     *     .execPgm("step01", this::step01)
     *     <b>.cond(0,NE,"step01")</b>
     *         .execPgm("step02", this::step02)
     *     .complete();
     * </pre>
     * if the indicated program (step01) ends with a non-zero return code (NE),
     * it does not execute the program that follows the condition (step02)
     *
     * @param cc       value to test
     * @param cond     condition to be tested
     * @param stepName name of the step to test
     * @return state to which to apply the operation
     */
    JobAction<T> cond(int cc, Cond cond, String stepName);

    /**
     * condition for performing the next operation
     * <p>
     * if the given name does not exist or has not been executed, the next program is executed
     * <p>
     * example
     * <pre>
     * JCL.getInstance().job("job01")
     *     .execPgm("step01", this::step01)
     *     <b>.when(0,EQ,"step01")</b>
     *         .execPgm("step02", this::step02)
     *     .complete();
     * </pre>
     * if the indicated program (step01) ends with a return code equal (EQ) to zero,
     * executes the program that follows the condition (step02)
     *
     * @param cc       value to test
     * @param cond     condition to be tested
     * @param stepName name of the step to test
     * @return state to which to apply the operation
     */
    default JobAction<T> when(int cc, Cond cond, String stepName) {
        return cond(cc, cond.not(), stepName);
    }
}
