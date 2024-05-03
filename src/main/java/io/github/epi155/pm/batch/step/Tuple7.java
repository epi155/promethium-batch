package io.github.epi155.pm.batch.step;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * tuple with seven elements
 *
 * @param <O1> 1st element type
 * @param <O2> 2nd element type
 * @param <O3> 3rd element type
 * @param <O4> 4th element type
 * @param <O5> 5th element type
 * @param <O6> 6th element type
 * @param <O7> 7th element type
 */
@Getter
@AllArgsConstructor(staticName = "of")
@Builder(setterPrefix = "with")
public class Tuple7<O1, O2, O3, O4, O5, O6, O7> {
    private final O1 t1;
    private final O2 t2;
    private final O3 t3;
    private final O4 t4;
    private final O5 t5;
    private final O6 t6;
    private final O7 t7;

    /**
     * performs the indicated action if the component of the tuple is different from null and is not in an error state
     *
     * @param action action to perform
     */
    public void onT1(Consumer<? super O1> action) {
        if (t1 != null)
            action.accept(t1);
    }

    /**
     * performs the indicated action if the component of the tuple is different from null and is not in an error state
     *
     * @param action action to perform
     */
    public void onT2(Consumer<? super O2> action) {
        if (t2 != null)
            action.accept(t2);
    }

    /**
     * performs the indicated action if the component of the tuple is different from null and is not in an error state
     *
     * @param action action to perform
     */
    public void onT3(Consumer<? super O3> action) {
        if (t3 != null)
            action.accept(t3);
    }

    /**
     * performs the indicated action if the component of the tuple is different from null and is not in an error state
     *
     * @param action action to perform
     */
    public void onT4(Consumer<? super O4> action) {
        if (t4 != null)
            action.accept(t4);
    }

    /**
     * performs the indicated action if the component of the tuple is different from null and is not in an error state
     *
     * @param action action to perform
     */
    public void onT5(Consumer<? super O5> action) {
        if (t5 != null)
            action.accept(t5);
    }

    /**
     * performs the indicated action if the component of the tuple is different from null and is not in an error state
     *
     * @param action action to perform
     */
    public void onT6(Consumer<? super O6> action) {
        if (t6 != null)
            action.accept(t6);
    }

    /**
     * performs the indicated action if the component of the tuple is different from null and is not in an error state
     *
     * @param action action to perform
     */
    public void onT7(Consumer<? super O7> action) {
        if (t7 != null)
            action.accept(t7);
    }
}
