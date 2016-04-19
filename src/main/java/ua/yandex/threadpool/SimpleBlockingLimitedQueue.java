/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.threadpool;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Vasyl
 */
public class SimpleBlockingLimitedQueue<T> implements PrimitiveQueue<T> {

    private LinkedList<T> storage;
    private int limit;

    public SimpleBlockingLimitedQueue(int limit) {
        this.storage = new LinkedList<T>();
        this.limit = limit;
    }

    @Override
    public synchronized void add(T element) throws InterruptedException {
        while (getSize() >= limit) {
            wait();
        }
        storage.add(element);
        notifyAll();
    }

    @Override
    public synchronized T poll() throws InterruptedException {
        while(getSize() == 0) {
            wait();
        }
        T res = storage.poll();
        notifyAll();
        return res;
    }
    
    @Override
    public int getSize() {
        return storage.size();
    }

}
