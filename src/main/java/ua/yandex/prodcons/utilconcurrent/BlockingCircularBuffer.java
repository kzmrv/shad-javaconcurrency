/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.prodcons.utilconcurrent;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Vasyl
 */
public class BlockingCircularBuffer<T> {

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private Queue<T> storage;
    private int maxSize;
    private volatile int size;
    private volatile boolean empty;
    private volatile boolean full;

    public BlockingCircularBuffer(Collection<? extends T> collection,
            int maxSize) {
        this.storage = new LinkedList<T>(collection);
        this.size = collection.size();
        this.maxSize = maxSize;
        this.empty = storage.isEmpty();
        this.full = (size >= maxSize);
    }

    public BlockingCircularBuffer(int maxSize) {
        this(new LinkedList<>(), maxSize);
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isFull() {
        return this.full;
    }

    // Gets and removes the first(pointed) element of the buffer
    public T get() throws InterruptedException {
        lock.lock();
        try {
            while (empty) {
                notEmpty.await();
            }
            T data = storage.poll();
            size--;
            if (this.size == 0) {
                empty = true;
            }
            full = false;
            notFull.signal();
            return data;
        } finally {
            lock.unlock();
        }
    }

    // Adds element to the position before currently pointed(end)
    public void add(T data) {
        lock.lock();
        try {
            while (full) {
                notFull.await();
            }
            storage.add(data);
            size++;
            if (this.size >= maxSize) {
                full = true;
            }
            empty = false;
            notEmpty.signal();
        } catch (InterruptedException ex) {
        } finally {
            lock.unlock();
        }

    }

    // Returns the current pointed element.
    // Moves pointer to the next element in buffer. Throws 
    // IllegalStateException on empty buffer.
    public T peekAndPass() {
        lock.lock();
        try {
            if (empty) {
                throw new IllegalStateException("Buffer is empty");
            }
            T first = storage.poll();
            storage.add(first);
            return first;
        } finally {
            lock.unlock();
        }
    }

}
