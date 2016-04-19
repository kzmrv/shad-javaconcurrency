/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.mergesort;

import java.util.concurrent.RecursiveTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vasyl
 */
public class MergeParallelSorter {

    private double[] data;
    private double[] res;
    private boolean completed = false;

    public boolean isCompleted() {
        return completed;
    }

    public MergeParallelSorter(double[] data) {
        this.data = data;
        this.res = new double[data.length];
    }

    public double[] RecursiveSort() {
        Thread active
                = new Thread(new ParallelSortTask(0, data.length));
        active.start();
        try {
            active.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MergeParallelSorter.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return res;
    }

    private int BinarySearch(double median, int left, int right) {
        int lower = left;
        int upper = right;
        int curr = (left + right) / 2;
        while (upper > lower) {
            curr = (left + right) / 2;
            if (data[curr] >= median) {
                upper = curr;
            } else {
                lower = curr;
            }
        }
        return curr;
    }

    void Merge(int p1, int r1, int p2, int r2, int p3) {
        int n1 = r1 - p1 + 1;
        int n2 = r2 - p2 + 1;
        if (n1 < n2) {
            int temp = p1;
            p1 = p2;
            p2 = temp;
            temp = r1;
            r1 = r2;
            r2 = temp;
            temp = n1;
            n1 = n2;
            n2 = temp;
        }
        if (n1 == 0) {
            return;
        }
        int q1 = (p1 + r1) / 2;
        int q2 = BinarySearch(data[q1], p2, r2);
        int q3 = p3 + q1 - p1 + q2 - p2;
        res[q3] = data[q1];
        Thread leftThread
                = new Thread(new ParallelMergeTask(p1, q1 - 1, p2, q2 - 1, p3));
        Thread rightThread = new Thread(new ParallelMergeTask(q1 + 1, r1,
                q2, r2, q3 + 1));
        try {
            leftThread.join();
            rightThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MergeParallelSorter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class ParallelMergeTask implements Runnable {

        int p1, p2, r1, r2, p3;

        public ParallelMergeTask(int p1, int p2, int r1, int r2, int p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.r1 = r1;
            this.r2 = r2;
            this.p3 = p3;
        }

        @Override
        public void run() {
            Merge(p1, r1, p2, r2, p3);
        }

    }

    class ParallelSortTask implements Runnable {

        private int low, high;
        private volatile boolean completed;

        public ParallelSortTask(int low, int high) {
            this.low = low;
            this.high = high;
        }

        @Override
        public void run() {
            // Divide
            if (high - low > 1) {
                Thread threadLeft = new Thread(new ParallelSortTask(low,
                        low + high / 2));
                Thread threadRight = new Thread(new ParallelSortTask((low + high)
                        / 2 + 1, high));
                threadLeft.start();
                threadRight.start();
                try {
                    threadLeft.join();
                    threadRight.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ParallelSortTask.class.getName()).log(
                            Level.SEVERE, null, ex);
                }

                Merge(low, (low + high) / 2, (low + high) / 2, high, 0);

            } else {

            }

        }

    }

}
