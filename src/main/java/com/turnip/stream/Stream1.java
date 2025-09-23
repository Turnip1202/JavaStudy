package com.turnip.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stream1 {

    public static void main(String[] args) {
        // 按照奇偶分组
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println(partitioned);  // 输出 {false=[1, 3, 5], true=[2, 4, 6]}


    }

}
