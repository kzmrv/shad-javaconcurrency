/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.fj;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author Vasyl
 */
public class WordCounter {

    volatile int counter = 0;
    private int threshold;
    private ForkJoinPool pool;
    private String[] data;
    private Map<String, Integer> result;

    public WordCounter() {
        this(4);
    }

    public WordCounter(int threshold) {
        this.threshold = threshold;
        pool = ForkJoinPool.commonPool();
    }

    public Map<String, Integer> wordCount(String[] data) {
        result = new ConcurrentHashMap<>();
        this.data = data;
        CountTask mainTask = new CountTask(0, data.length - 1, data);
        pool.invoke(mainTask);
        return result;

    }

    class CountTask extends RecursiveAction {

        private final int from;
        private final int to;

        public CountTask(int from, int to, String[] data) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected void compute() {
            if (to - from > threshold) {
                CountTask left = new CountTask(from, ((from + to) / 2), data);
                CountTask right = new CountTask(((from + to) / 2) + 1, to,
                        data);
                left.fork();
                right.compute();
                left.join();
            } else {
                for (int i = from; i <= to; i++) {
                    boolean success = false;
                    while (!success) {
                        success = (result.putIfAbsent(data[i], 1) == null);
                        if (!success) {
                            // Checked increment
                            Integer old = result.get(data[i]);
                            success = result.replace(data[i], old, old + 1);
                        }
                    }
                }

            }
        }
    }
}
