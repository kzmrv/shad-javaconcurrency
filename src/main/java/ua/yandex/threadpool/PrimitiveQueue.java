/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.threadpool;

/**
 *
 * @author Vasyl
 */
public interface PrimitiveQueue<T> {
    public void add(T element) throws InterruptedException;
    public T poll() throws InterruptedException;
    public int getSize();
}
