package com.maxdemarzi;

import java.util.Comparator;
import java.util.HashMap;

public class ScoreComparator <T extends Comparable<T>> implements Comparator<HashMap> {

    // Reverse comparator for score
    @Override
    public int compare(HashMap map1, HashMap map2) {
        return ((Float)map2.get("score")).compareTo((Float)map1.get("score"));
    }
}