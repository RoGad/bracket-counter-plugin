package com.example.util;

import java.util.List;

public class Calc {

    public int sum(int[] values) {
        int total = 0;
        for (int v : values) {
            total += v;
        }
        return total;
    }

    public List<Integer> wrap(int value) {
        return List.of(value);
    }
}
