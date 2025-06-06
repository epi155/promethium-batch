package io.github.epi155.pm.batch.fault;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class MatchContext {
    public static final ThreadLocal<ClassMatcher> matcher = new ThreadLocal<>();

    private MatchContext() {
    }

    public interface ClassMatcher {
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

    public static class MatchByLib implements ClassMatcher {

        private final ClassLib lib;

        public MatchByLib(Class<?> claz) {
            this.lib = libOf(claz);
        }

        static ClassLib /*String*/ libOf(String className) {
            try {
                Class<?> clazz = Class.forName(className);
                return libOf(clazz);
//                return Objects.requireNonNull(libOf(clazz)).toString();
            } catch (ClassNotFoundException e) {
                log.error("Errore Interno su <{}> ", className, e);
                return null;
            }
        }
        public static String nameOf(String className) {
            if (className==null) return null;
            val lib = libOf(className);
            return lib==null ? null : lib.toString();
        }

        static ClassLib libOf(Class<?> clazz) {
            Optional<String> oLocation = Optional.ofNullable(clazz.getProtectionDomain())
                    .map(ProtectionDomain::getCodeSource)
                    .map(CodeSource::getLocation)
                    .map(URL::toString);
            if (oLocation.isPresent()) {
                String location = oLocation.get();
                int k1 = location.lastIndexOf('/');
                if (k1 >= 0) {
                    int k2 = location.lastIndexOf('/', k1 - 1);
                    if (k2 >= 0) {
                        String vrs = location.substring(k2 + 1, k1);
                        String lib = location.substring(k1 + 1);
                        return new ClassLib(lib, vrs);
                    }
                }
            }
            return null;
        }

        @Override
        public boolean match(String className) {
            return Objects.equals(lib, libOf(className));
        }
    }

    public static class MatchByPackagePrefix implements ClassMatcher {
        private final String prefix;

        public MatchByPackagePrefix(Class<?> claz, int w) {
            String pckName = claz.getPackage().getName();
            int sb = 0;
            for (int k = 0; k < w; k++) {
                int se = pckName.indexOf('.', sb + 1);
                if (se < 0) break;
                sb = se;
            }
            this.prefix = pckName.substring(0, sb);
        }

        public MatchByPackagePrefix(String className, int w) {
            int ku = className.lastIndexOf('.');
            String pckName = className.substring(0, ku);
            int sb = 0;
            for (int k = 0; k < w; k++) {
                int se = pckName.indexOf('.', sb + 1);
                if (se < 0) break;
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
            for (val prefix : prefixes) {
                if (className.startsWith(prefix))
                    return true;
            }
            return false;
        }
    }
}
