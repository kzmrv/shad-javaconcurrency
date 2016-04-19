/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.lockfree;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.print.attribute.standard.JobStateReason;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vasyl
 */
public class LockFreeIndexerTest {

    public LockFreeIndexerTest() {
    }

    @Test
    public void testNext() throws InterruptedException {
        LockFreeIndexer indexer = new LockFreeIndexer();
        ConcurrentLinkedQueue<String>[] queue = new ConcurrentLinkedQueue[10];
        LinkedList<Thread> threads = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            queue[i] = new ConcurrentLinkedQueue<>();
            int t = i;
            threads.add(new Thread(new Runnable() {

                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        queue[t].add(indexer.next().toString());
                    }
                }
            }));
            threads.get(i).start();
        }

        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) {
            threads.get(i).interrupt();
        }
        Thread.sleep(1000);
        LinkedList<String> results = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            for (String el : queue[i]) {
                results.add(el);
            }
        }

        results.sort(new CustomStringComparator());
        BigInteger current = BigInteger.ONE;
        BigInteger two = new BigInteger("2");
        for (int i = 1; i < results.size(); i++) {
            assertEquals(current.multiply(two).toString(),
                    results.get(i).toString());
            current = current.multiply(two);
        }
        System.out.println("Test success! Number of elements "
                + results.size());

    }

    class CustomStringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            if (o1.length() > o2.length()) {
                return 1;
            }
            if (o2.length() > o1.length()) {
                return -1;
            }
            for (int i = 0; i < o1.length(); i++) {
                if (o1.charAt(i) > o2.charAt(i)) {
                    return 1;
                }
                if (o1.charAt(i) < o2.charAt(i)) {
                    return -1;
                }
            }
            return 0;
        }

    }
}
