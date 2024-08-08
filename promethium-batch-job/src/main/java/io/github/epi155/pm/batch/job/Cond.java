package io.github.epi155.pm.batch.job;

/**
 * Relation for condition code
 */
public enum Cond {
    /**
     * EQual
     */
    EQ {
        @Override
        boolean test(Integer rc, int cc) {
            return rc != null && rc == cc;
        }

        @Override
        Cond not() {
            return NE;
        }
    },
    /**
     * Not Equal
     */
    NE {
        @Override
        boolean test(Integer rc, int cc) {
            return rc != null && rc != cc;
        }

        @Override
        Cond not() {
            return EQ;
        }
    },
    /**
     * Less Than
     */
    LT {
        @Override
        boolean test(Integer rc, int cc) {
            return rc != null && rc < cc;
        }

        @Override
        Cond not() {
            return GE;
        }
    },
    /**
     * Greater Than
     */
    GT {
        @Override
        boolean test(Integer rc, int cc) {
            return rc != null && rc > cc;
        }

        @Override
        Cond not() {
            return LE;
        }
    },
    /**
     * Less Than
     */
    LE {
        @Override
        boolean test(Integer rc, int cc) {
            return rc != null && rc <= cc;
        }

        @Override
        Cond not() {
            return GT;
        }
    },
    /**
     * Greater Than
     */
    GE {
        @Override
        boolean test(Integer rc, int cc) {
            return rc != null && rc >= cc;
        }

        @Override
        Cond not() {
            return LT;
        }
    };

    abstract boolean test(Integer rc, int cc);

    abstract Cond not();
}
