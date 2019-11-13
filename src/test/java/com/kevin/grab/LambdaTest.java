package com.kevin.grab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LambdaTest {

    public static void filter(List<String> list, Predicate expression){
        list.forEach( s -> {
            if(expression.test(s)){
                System.out.println(s);
            }
        });
    }

    public static void filter2(List<String> list, Predicate expression){
        list.stream().filter((s)-> (expression.test(s))).forEach((s)->{System.out.println(s);});
    }

    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("cherry");
        list.add("apple");

        String str="";
        for (String s : list) {
            s+=" is a fruit.";
            str+=s;
        }
        System.out.println(str);

        System.out.println(list.stream().map((s)-> s+" is a fruit.").reduce((st, s)-> st + s).get());

        System.out.println(list.stream().map((s)-> s.toUpperCase()).collect(Collectors.joining(",")));

        System.out.println(list.stream().filter((s)-> (s.contains("a"))).collect(Collectors.toList()));

        System.out.println(list.stream().distinct().collect(Collectors.toList()));

        list.stream().map((s)-> s+" is a fruit.").forEach((s)->{System.out.println(s);});

        filter(list, ( s )-> ((String)s).startsWith("b"));
        System.out.println("------------------");
        filter(list, ( s )-> true );

        System.out.println("------------------");
        filter2(list, ( s )-> ((String)s).startsWith("b"));
        System.out.println("------------------");
        filter2(list, ( s )-> false );

        List<Integer> nums = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics stats = nums.stream().mapToInt((x) -> x).summaryStatistics();
        System.out.println(stats.getMax()+","+stats.getMin()+","+stats.getAverage()+","+stats.getSum());
    }
}
