package utils;

import java.util.HashSet;
import java.util.Set;

public class SetIntersector {

    private SetIntersector() {

    }

    public static <E> Set<E> intersectSets(Set<E> set1, Set<E> set2) {
        Set<E> set1Copy = new HashSet<>(set1);
        Set<E> set2Copy = new HashSet<>(set2);
        set1Copy.retainAll(set2Copy);
        return set1Copy;
    }
}
