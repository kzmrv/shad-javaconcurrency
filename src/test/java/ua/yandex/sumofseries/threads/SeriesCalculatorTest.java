/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.sumofseries.threads;

import java.util.Arrays;
import java.util.LinkedList;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Vasyl
 */
public class SeriesCalculatorTest {

    @Test
    public void testOneThread() throws InterruptedException {
        SeriesCalculator instance = new SeriesCalculator(x -> Math.sin(x)
                * Math.cos(x), 0, 1000, 1);
        double res = instance.Calculate();
        System.out.println("Single thread exection time is "
                + instance.getLastDuration());
        System.out.println("Single thread execution result is "
                + res);
        assertEquals(68, res, 2.0);
    }

    @Test
    public void testFiveThreads() throws InterruptedException {
        SeriesCalculator instance = new SeriesCalculator(x -> Math.sin(x)
                * Math.cos(x), 0, 1000, 5);
        double res = instance.Calculate();
        System.out.println("Five threads exection time is "
                + instance.getLastDuration());
        System.out.println("Five threads execution result is "
                + res);
        assertEquals(68, res, 2.0);

    }

    @Test
    public void testSevenThreads() throws InterruptedException {
        SeriesCalculator instance = new SeriesCalculator(x -> Math.sin(x)
                * Math.cos(x), 0, 1000, 7);
        double res = instance.Calculate();
        System.out.println("Seven threads exection time is "
                + instance.getLastDuration());
        System.out.println("Seven threads execution result is "
                + res);
        assertEquals(68, res, 2.0);

    }

    @Test
    public void testHundredThreads() throws InterruptedException {
        SeriesCalculator instance = new SeriesCalculator(x -> Math.sin(x)
                * Math.cos(x), 0, 1000, 100);
        double res = instance.Calculate();
        System.out.println("Hundred threads exection time is "
                + instance.getLastDuration());
        System.out.println("Hundred threads execution result is "
                + res);
        assertEquals(68, res, 2.0);

    }

}
