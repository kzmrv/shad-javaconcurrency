/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.threadpool;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vasyl
 */
public class FixedThreadPoolTest {

    public FixedThreadPoolTest() {
    }

    @Test
    public void testSimple() throws Exception {
        System.out.println("simple");
        FixedThreadPool instance = new FixedThreadPool(4, 4);
        instance.submit(new SomeMessagingTask("Simple Test", 0.5));
        instance.submit(new SomeMessagingTask("Simple Test 1", 0.5));
        instance.submit(new SomeMessagingTask("Simple Test 2", 0.5));
        assertEquals(instance.isOverloaded(), false);
        instance.submit(new SomeMessagingTask("Simple Test 3", 0.5));
        assertEquals(instance.isOverloaded(), true);
        instance.invokeAll();
        instance.submit(new SomeMessagingTask("Simple Test finish", 0));
        Thread.sleep(1500);
    }

    @Test(expected = IllegalStateException.class)
    public void testOverflow() throws InterruptedException {
        System.out.println("overflow");

        FixedThreadPool instance = new FixedThreadPool(4, 4);
        for (int i = 0; i < 4; i++) {
            instance.submit(new SomeMessagingTask("Hello " + i, 1));
        }

        Runnable toFail = new SomeFailingTask(0);
        instance.submit(toFail);
        instance.invokeAll();
        fail("Overflow ignored");

    }

    @Test(expected = IllegalStateException.class)
    public void testCloseForInput() throws InterruptedException {
        System.out.println("closeForInput");
        FixedThreadPool instance = new FixedThreadPool(4, 4);
        instance.closeForInput();
        instance.submit(new SomeTask(1));
    }

    class SomeTask implements Runnable {

        private int time;

        public SomeTask(double seconds) {
            time = (int) seconds * 1000;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(FixedThreadPoolTest.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

    }

    class SomeFailingTask implements Runnable {

        private int time;

        public SomeFailingTask(double seconds) {
            time = (int) seconds * 1000;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(FixedThreadPoolTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            throw new AssertionError("Thread didnt fail");
        }

    }

    class SomeMessagingTask implements Runnable {

        private int time;
        private String message;

        public SomeMessagingTask(String message, double seconds) {
            time = (int) seconds * 1000;
            this.message = message;

        }

        @Override
        public void run() {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(FixedThreadPoolTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            System.out.println(message);

        }

    }

}
