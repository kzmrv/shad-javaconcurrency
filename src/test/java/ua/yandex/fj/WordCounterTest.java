/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.fj;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vasyl
 */
public class WordCounterTest {

    public WordCounterTest() {
    }

    @Test
    public void testWordCount_smallSet() {
        System.out.println("wordCount");
        String[] data = new String[]{"123", "222", "44", "end", "44", "end"};
        WordCounter instance = new WordCounter();
        Map<String, Integer> expected = new HashMap<>();
        expected.put("123", 1);
        expected.put("44", 2);
        expected.put("222", 1);
        expected.put("end", 2);
        Map<String, Integer> result = instance.wordCount(data);
        for (String el : expected.keySet()) {
            assertEquals(result.get(el), expected.get(el));
        }
    }

    @Test
    public void testWordCount_bigSet() {
        for (int j = 0; j < 100; j++) {
            System.out.println("wordCount_bigSet");
            String[] data = new String[1000];
            // 0
            for (int i = 0; i < 1000; i += 10) {
                data[i] = "hello";
            }
            //1 and 6
            for (int i = 1; i < 1000; i += 5) {
                data[i] = "bye";
            }
            //2
            for (int i = 2; i < 1000; i += 20) {
                data[i] = "java";
            }
            //2
            for (int i = 12; i < 1000; i += 20) {
                data[i] = "123";
            }
            //3 and 8
            for (int i = 3; i < 1000; i += 5) {
                data[i] = "111";
            }
            //4 and 9
            for (int i = 4; i < 1000; i += 5) {
                data[i] = "abc";
            }
            //5
            for (int i = 5; i < 1000; i += 10) {
                data[i] = "vasa";
            }
            //7 (same as 4 and 9)
            for (int i = 7; i < 1000; i += 10) {
                data[i] = "abc";
            }
            WordCounter instance = new WordCounter(50);
            Map<String, Integer> expected = new HashMap<>();
            expected.put("hello", 100);
            expected.put("bye", 200);
            expected.put("java", 50);
            expected.put("123", 50);
            expected.put("111", 200);
            expected.put("abc", 300);
            expected.put("vasa", 100);
            Map<String, Integer> result = instance.wordCount(data);
            for (String el : result.keySet()) {
                System.out.println(result.get(el).toString() + "  ex  " + expected.get(el));
            }
            for (String el : expected.keySet()) {
                assertEquals(result.get(el), expected.get(el));
            }
        }
    }

}
