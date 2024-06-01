package io.github.epi155.pm.batch.job;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.util.Collection;
import java.util.Objects;

class JobContext {
    private JobContext() {}
    static ThreadLocal<ClassMatcher> matcher = new ThreadLocal<>();

    interface ClassMatcher {
        boolean match(String className);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    static class ClassLib {
        private final String jar;
        private final String vrs;
        public String toString() {
            return jar.isEmpty() ? vrs : jar + "/" + vrs;
        }
    }

    static class MatchByLib implements ClassMatcher {

        private final ClassLib lib;

        public MatchByLib(Class<?> claz) {
            this.lib = libOf(claz);
        }

        @Override
        public boolean match(String className) {
            return Objects.equals(lib, libOf(className));
        }
        static ClassLib libOf(String className) {
            try {
                Class<?> clazz = Class.forName(className);
                return libOf(clazz);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        static ClassLib libOf(Class<?> clazz) {
            String location = clazz.getProtectionDomain().getCodeSource().getLocation().toString();
            int k1 = location.lastIndexOf('/');
            if (k1 >= 0) {
                int k2 = location.lastIndexOf('/', k1-1);
                if (k2 >= 0) {
                    String vrs = location.substring(k2+1, k1);
                    String lib = location.substring(k1+1);
                    return new ClassLib(lib, vrs);
                }
            }
            return null;
        }
    }

    public static class MatchByPackagePrefix implements ClassMatcher {
        private final String prefix;

        public MatchByPackagePrefix(Class<?> claz, int w) {
            String pckName = claz.getPackage().getName();
            int sb = 0;
            for(int k=0; k<w; k++) {
                int se = pckName.indexOf('.', sb+1);
                if (se<0) break;
                sb = se;
            }
            this.prefix = pckName.substring(0, sb);
        }

        @Override
        public boolean match(String className) {
            return className.startsWith(prefix);
        }
    }

    public static class MatchByPackagePrefixes implements ClassMatcher {
        private final Collection<String> prefixes;

        public MatchByPackagePrefixes(Collection<String> prefixes) {
            this.prefixes = prefixes;
        }

        @Override
        public boolean match(String className) {
            for (val prefix: prefixes) {
                if (className.startsWith(prefix))
                    return true;
            }
            return false;
        }
    }
}
