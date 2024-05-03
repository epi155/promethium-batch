package io.github.epi155.pm.batch.step;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * tuple with one elements
 *
 * @param <O1> 1st element type
 */
@Getter
@AllArgsConstructor(staticName = "of")
class Tuple1<O1> {
    private static final Tuple1<?> EMPTY = new Tuple1<>(null);
    private final O1 t1;

    protected static <T> Tuple1<T> empty() {
        //noinspection unchecked
        return (Tuple1<T>) EMPTY;
    }

    /**
     * performs the indicated action if the component of the tuple is different from null and is not in an error state
     *
     * @param action action to perform
     * @return {@code true} if action is performed else {@code false}
     */
    public boolean onT1(Consumer<? super O1> action) {
        if (t1 != null) {
            action.accept(t1);
            return true;
        }
        return false;
    }
}
