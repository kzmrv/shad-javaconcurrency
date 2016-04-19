/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.prodcons.threads;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Vasyl Kazimirov
 */
public class BlockingCircularBuffer<T> {

    private Queue<T> storage;
    private int maxSize;
    private volatile int size;
    private volatile boolean empty;
    private volatile boolean full;

    // Builds the buffer from given collection with given maximum size.
    public BlockingCircularBuffer(
            Collection<? extends T> collection, int maxSize) {
        this.storage = new LinkedList<T>(collection);
        this.size = storage.size();
        this.maxSize = maxSize;
        refreshStates();
    }

    // Builds the buffer with given maximum size.
    public BlockingCircularBuffer(int maxSize) {
        this(new LinkedList<>(), maxSize);
    }

    // Gets the current number of elements in the buffer.
    public int getSize() {
        return this.size;
    }

    // Returns true if buffer is empty
    public boolean isEmpty() {
        return this.empty;
    }

    // Returns true if buffer is full
    public boolean isFull() {
        return this.full;
    }

    // Gets and removes currently pointed element.
    public synchronized T get() {
        while (this.empty) {
            try {
                wait();
            } catch (InterruptedException ex) {

            }
        }
        T data = storage.poll();
        size--;
        refreshStates();
        notifyAll();
        return data;
    }

    // Adds an element to the end of the buffer.
    public synchronized void add(T data) {
        while (this.full) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        storage.add(data);
        size++;
        refreshStates();
        notifyAll();

    }

    // Returns the current pointed element.
    // Moves pointer to the next element in buffer. Throws 
    // IllegalStateException on empty buffer.
    public synchronized T peekAndPass() {
        if (this.empty) {
            throw new IllegalStateException("Buffer is empty");
        }
        T first = storage.poll();
        storage.add(first);
        return first;

    }

    private synchronized void refreshStates() {
        this.empty = storage.isEmpty();
        this.full = (size >= maxSize);
    }

}
