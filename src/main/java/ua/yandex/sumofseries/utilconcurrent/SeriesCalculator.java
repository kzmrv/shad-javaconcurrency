/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.sumofseries.utilconcurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author Vasyl
 */
public class SeriesCalculator {

    DoubleUnaryOperator series;
    double start, end;
    final double step = 0.005;
    int partitions;
    private double result;
    private double lastDuration;
    private ExecutorService pool;

    public SeriesCalculator(DoubleUnaryOperator series, double start,
            double end, int activeThreads) {
        this.series = series;
        this.start = start;
        this.end = end;
        partitions = activeThreads;
        pool = Executors.newFixedThreadPool(activeThreads);
    }

    public double Calculate() throws InterruptedException,
            ExecutionException {
        double startTime = System.currentTimeMillis();
        result = 0.0;
        double fragment = (end - start) / partitions;
        SeriesCalculatorTask[] tasks = new SeriesCalculatorTask[partitions];
        Future<Double>[] futures = new Future[partitions];
        for (int i = 0; i < partitions - 1; i++) {
            tasks[i] = new SeriesCalculatorTask(this, start + i
                    * fragment, start + (i + 1) * fragment + step);
            futures[i] = pool.submit(tasks[i]);
        }
        tasks[partitions - 1] = new SeriesCalculatorTask(this, start
                + (partitions - 1) * fragment, end);
        futures[partitions - 1] = pool.submit(tasks[partitions - 1]);
        double res = 0;
        for (int i = 0; i < partitions; i++) {
            res += futures[i].get();
        }
        double endTime = System.currentTimeMillis();
        lastDuration = endTime - startTime;
        return res;
    }

    public int SearchParameter(Iterable<Integer> values) throws
            InterruptedException, ExecutionException {
        double minTime = 0;
        int minValue = 0;
        for (int value : values) {
            partitions = value;
            pool = Executors.newFixedThreadPool(partitions);
            Calculate();
            double cTime = getLastDuration();
            if ((cTime < minTime) || (minTime == 0)) {
                minTime = cTime;
                minValue = value;
            }
        }
        return minValue;
    }

    public Double getLastDuration() {
        return lastDuration;
    }
}
