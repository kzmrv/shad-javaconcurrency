/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.sumofseries.threads;


/**
 *
 * @author Vasyl
 */
public class SeriesCalculatorTask implements Runnable {

    private final SeriesCalculator calculator;
    private final double start, end;
    private double res = 0;
    private boolean isCalculated;

    public SeriesCalculatorTask(SeriesCalculator calculator, double start,
            double end) {
        this.calculator = calculator;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (double curr = start; curr < end; curr += calculator.step) {
            res += calculator.series.applyAsDouble(curr);
        }
        isCalculated = true;
    }

    public double getResult() {
        if (!isCalculated) {
            throw new IllegalStateException("Not calculated yet");
        }
        return res;
    }
}
