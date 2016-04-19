/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.threadpool;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vasyl
 */
public class FixedThreadPool {

    private Queue<Thread> workers;

    private int poolSize;
    private int maxTasks;
    private PrimitiveQueue<Runnable> tasks;
    private boolean closed;
    private boolean started;

    public FixedThreadPool(int poolSize, int maxTasks) {
        this.poolSize = poolSize;
        this.maxTasks = maxTasks;
        tasks = new SimpleBlockingLimitedQueue<Runnable>(maxTasks);

        workers = new LinkedList<>();
        LinkedList test = new LinkedList();
        for (int i = 0; i < poolSize; i++) {
            workers.add(new Worker(tasks, this));
        }
    }

    public boolean isOverloaded() {
        return (this.maxTasks <= this.tasks.getSize());
    }

    public synchronized void submit(Runnable task) throws InterruptedException {
        if (this.closed) {
            throw new IllegalStateException("Service closed");
        }
        if (!this.started && this.maxTasks <= this.tasks.getSize()) {
            throw new IllegalStateException("Service overloaded and not working"
                    + " yet");
        }
        tasks.add(task);
    }

    public synchronized void invokeAll() {
        if (this.closed) {
            throw new IllegalStateException("Service closed");
        }
        if (this.started) {
            return;
        }
        this.started = true;
        for (Thread worker : workers) {
            worker.start();
        }
    }

    public void closeForInput() {
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    class Worker extends Thread {

        private PrimitiveQueue<Runnable> tasksQueue;
        private FixedThreadPool pool;

        public Worker(PrimitiveQueue<Runnable> tasks, FixedThreadPool pool) {
            this.tasksQueue = tasks;
            this.pool = pool;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Runnable currentTask = tasksQueue.poll();
                    currentTask.run();
                    if ((pool.closed) && (tasksQueue.getSize() == 0)) {
                        return;
                    }
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
