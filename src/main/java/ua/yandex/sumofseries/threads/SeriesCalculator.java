/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.sumofseries.threads;

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
    private double lastDuration;

    public SeriesCalculator(DoubleUnaryOperator series, double start,
            double end, int activeThreads) {
        this.series = series;
        this.start = start;
        this.end = end;
        partitions = activeThreads;
    }

    public double Calculate() throws InterruptedException {
        double startTime = System.currentTimeMillis();
        double fragment = (end - start) / partitions;
        Thread[] threads = new Thread[partitions];
        SeriesCalculatorTask[] tasks = new SeriesCalculatorTask[partitions];
        for (int i = 0; i < partitions - 1; i++) {
            tasks[i] = new SeriesCalculatorTask(this, start + i
                    * fragment, start + (i + 1) * fragment + step);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }
        tasks[partitions - 1] = new SeriesCalculatorTask(this, start
                + (partitions - 1) * fragment, end);
        threads[partitions - 1] = new Thread(tasks[partitions - 1]);
        threads[partitions - 1].start();
        double res = 0;
        for (int i = 0; i < partitions; i++) {
            threads[i].join();
            res += tasks[i].getResult();
        }
        double endTime = System.currentTimeMillis();
        lastDuration = endTime - startTime;
        return res;
    } 

    public Double getLastDuration() {
        return lastDuration;
    }
}
