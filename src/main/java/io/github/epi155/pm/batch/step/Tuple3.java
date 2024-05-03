package io.github.epi155.pm.batch.step;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * tuple with three elements
 *
 * @param <O1> 1st element type
 * @param <O2> 2nd element type
 * @param <O3> 3rd element type
 */
@Getter
@AllArgsConstructor(staticName = "of")
@Builder(setterPrefix = "with")
public class Tuple3<O1, O2, O3> {
    private final O1 t1;
    private final O2 t2;
    private final O3 t3;

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

}
