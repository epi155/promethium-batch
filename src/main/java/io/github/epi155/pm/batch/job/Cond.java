package io.github.epi155.pm.batch.job;

public enum Cond {
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
