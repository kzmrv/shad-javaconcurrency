/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.prodcons.threads;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vasyl
 */
public class BlockingCircularBufferTest {

    @Test
    public void testOneThread() {
        System.out.println("One thread");
        Queue collection = new LinkedList<>();
        collection.addAll(Arrays.asList(1, 2, 3, 4));
        BlockingCircularBuffer instance = new BlockingCircularBuffer<>(collection, 4);
        LinkedList<Integer> expResult = new LinkedList<Integer>();
        for (int i = 0; i < 4; i++) {
            assertEquals((Integer) collection.poll(), instance.get());
        }
        assertEquals(instance.isEmpty(), true);
    }

    @Test
    public void testFull() throws InterruptedException {
        System.out.println("TestFull");
        Queue collection = new LinkedList<>();
        collection.addAll(Arrays.asList(1, 2, 3, 4));
        BlockingCircularBuffer instance = new BlockingCircularBuffer<>(
                collection, 4);
        Thread thread = new Thread(() -> {
            instance.add(5);
            if (!Thread.currentThread().isInterrupted()) {
                fail("Success putting element in full buffer "
                        + instance.isFull());
            }
        });
        thread.start();
        Thread.sleep(200);
        thread.interrupt();

        assertEquals(instance.isFull(), true);
    }

    @Test
    public void testEmpty() throws InterruptedException {
        System.out.println("TestEmpty");
        BlockingCircularBuffer instance = new BlockingCircularBuffer<>(4);
        Thread thread = new Thread(() -> {
            Object t = instance.get();
            if (!Thread.currentThread().isInterrupted()) {
                fail("Success getting element from empty buffer" + t.toString());
            }
        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();

        assertEquals(instance.isEmpty(), true);
    }

    @Test
    public void testGetSize() throws InterruptedException {
        System.out.println("getSize");
        Queue collection = new LinkedList<>();

        BlockingCircularBuffer instance = new BlockingCircularBuffer<>(10);
        Thread thread = new Thread(() -> {
            instance.add(5);
            instance.add(6);
            instance.add(7);
        });
        thread.start();
        collection.addAll(Arrays.asList(1, 2, 3, 4));
        for (Object el : collection) {
            instance.add(el);
        }
        thread.join();
        assertEquals(7, instance.getSize());
    }

    @Test
    public void testCorrectElements() throws InterruptedException {
        System.out.println("correctElements");
        Queue<Integer> collection = new LinkedList<>();

        BlockingCircularBuffer<Integer> instance
                = new BlockingCircularBuffer<>(10);
        Thread thread = new Thread(() -> {
            instance.add(5);
            instance.add(6);
            instance.add(7);
        });
        thread.start();
        collection.addAll(Arrays.asList(1, 2, 3, 4));
        for (Integer el : collection) {
            instance.add(el);
        }
        thread.join();
        LinkedList<Integer> res = new LinkedList<>();
        while (!instance.isEmpty()) {
            res.add(instance.get());
        }
        res.sort(Comparator.naturalOrder());
        for (int i = 0; i < 7; i++) {
            assertEquals((long) i + 1, (long) res.get(i));
        }
        assertEquals(7, res.size());
    }

    @Test
    public void testPeekElements() throws InterruptedException {
        System.out.println("peek");
        Queue<Integer> collection = new LinkedList<>();

        BlockingCircularBuffer<Integer> instance
                = new BlockingCircularBuffer<>(10);

        collection.addAll(Arrays.asList(1, 2, 3, 4));
        for (Integer el : collection) {
            instance.add(el);
        }
        LinkedList<Integer> res = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            res.add(instance.peekAndPass());
        }
        for (int i = 0; i < 4; i++) {
            assertEquals((long) i + 1, (long) res.get(i));
        }
        assertEquals((long) 1, (long) res.get(4));
        assertEquals(5, res.size());
    }

}
