package com.platform.testing;

import com.google.common.base.Predicate;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @program: FuctionTest
 * @description:
 * @author: zhuruiqi
 * @create: 2022-06-07 14:56
 **/
public class FuctionTest {
    @Test
    public void test1() {
        Function<String, Integer> function = Integer::valueOf;
        System.out.println(function.apply("123"));
    }

    @Test
    public void test2() {
        Function<String, String> befor = name -> "befor 输入参数[" + name + "]";
        Function<String, String> after = name -> "after 输入参数[" + name + "]";

        System.out.println("compose:" + after.compose(befor).apply("demo"));
        System.out.println("andThen:" + befor.andThen(after).apply("demo"));
    }

    @Test
    public void test3() {
//        Predicate<Integer> predicate = x -> (x > 5);
//        predicate.apply(7);
//
//        Predicate predicate2 = (s) -> s.length() > 0;
//        ConcurrentHashMap

    }
}
