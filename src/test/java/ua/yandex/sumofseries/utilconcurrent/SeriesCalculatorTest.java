/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.sumofseries.utilconcurrent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Vasyl
 */
public class SeriesCalculatorTest {
     @Test
    public void testOneThread() throws InterruptedException,
            ExecutionException {
        SeriesCalculator instance = new SeriesCalculator(x -> Math.sin(x)
                * Math.cos(x), 0, 1000, 1);
        double res = instance.calculate();
        System.out.println("Single thread exection time is "
                + instance.getLastDuration());
        System.out.println("Single thread execution result is "
                + res);
        assertEquals(68, res, 2.0);
    }

    @Test
    public void testFiveThreads() throws InterruptedException,
            ExecutionException {
        SeriesCalculator instance = new SeriesCalculator(x -> Math.sin(x)
                * Math.cos(x), 0, 1000, 5);
        double res = instance.calculate();
        System.out.println("Five threads exection time is "
                + instance.getLastDuration());
        System.out.println("Five threads execution result is "
                + res);
        assertEquals(68, res, 2.0);

    }

    @Test
    public void testSevenThreads() throws InterruptedException,
            ExecutionException {
        SeriesCalculator instance = new SeriesCalculator(x -> Math.sin(x)
                * Math.cos(x), 0, 1000, 7);
        double res = instance.calculate();
        System.out.println("Seven threads exection time is "
                + instance.getLastDuration());
        System.out.println("Seven threads execution result is "
                + res);
        assertEquals(68, res, 2.0);

    }

    @Test
    public void testHundredThreads() throws InterruptedException,
            ExecutionException {
        SeriesCalculator instance = new SeriesCalculator(x -> Math.sin(x)
                * Math.cos(x), 0, 1000, 100);
        double res = instance.calculate();
        System.out.println("Hundred threads exection time is "
                + instance.getLastDuration());
        System.out.println("Hundred threads execution result is "
                + res);
        assertEquals(68, res, 2.0);

    }

    @Test
    public void testParameterSearch() throws InterruptedException,
            ExecutionException {
        SeriesCalculator instance = new SeriesCalculator(x -> Math.sin(x)
                * Math.cos(x), 0, 1000, 5);
        int p = instance.searchParameter(new LinkedList<Integer>(Arrays.asList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)));
        System.out.println("Optimal number of threads "
                + p);
    }
}
