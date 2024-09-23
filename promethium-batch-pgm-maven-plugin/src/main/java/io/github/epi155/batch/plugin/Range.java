package io.github.epi155.batch.plugin;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Range implements Iterable<Integer> {
    private static final Pattern P_RANGE = Pattern.compile("(\\d+)\\s*[.][.]\\s*(\\d+)");
    private static final Pattern P_LIST = Pattern.compile("(\\d+)\\s*(,\\s*\\d+){0,250}");
    private static final Pattern P_EMPTY = Pattern.compile("(-|NULL|NIL|VOID|EMPTY)", Pattern.CASE_INSENSITIVE);
    private SortedSet<Integer> items;
    public static Range of(String s) {
        Matcher matchRange = P_RANGE.matcher(s);
        if (matchRange.find()) {
            int min = Integer.parseInt(matchRange.group(1));
            int max = Integer.parseInt(matchRange.group(2));
            Range range = new Range();
            range.items = IntStream.rangeClosed(min, max).boxed().collect(Collectors.toCollection(TreeSet::new));
            return range;
        }
        Matcher matchList = P_LIST.matcher(s);
        if (matchList.find()) {
            s = s.replaceAll("\\s+", "");
            String[] ss = s.split(",");
            SortedSet<Integer> set = new TreeSet<>();
            for(String n: ss) {
                set.add(Integer.parseInt(n));
            }
            Range range = new Range();
            range.items = set;
            return range;
        }
        Matcher matchEmpty = P_EMPTY.matcher(s);
        if (matchEmpty.find()) {
            Range range = new Range();
            range.items = Collections.emptySortedSet();
            return range;
        }
        throw new IllegalArgumentException(s);
    }

    @Override
    public @NotNull Iterator<Integer> iterator() {
        return items.iterator();
    }
}
