/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.lockfree;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author Vasyl
 */
public class LockFreeIndexer {

    private BigInteger core;
    private AtomicReference<BigInteger> wrapper;

    public LockFreeIndexer() {
        core = new BigInteger("1");
        wrapper = new AtomicReference<>(core);
    }

    public BigInteger next() {
        BigInteger currentValue;
        do {
            currentValue = wrapper.get();
        } while (!wrapper.compareAndSet(currentValue, currentValue.multiply(
                new BigInteger("2"))));
        return currentValue;
    }
}
