/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.bank;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Vasyl
 */
public class Account {

        Lock lock = new ReentrantLock();
        Integer id;
        double money;

        public Account(Integer id, double money) {
            this.id = id;
            this.money = money;
        }

        public void withdraw(double amount) {
            try {
                lock.lock();
                if (this.money < amount) {
                    throw new Bank.NotEnoughMoneyException("Not enough money");
                } else {
                    this.money -= amount;
                }
            } finally {
                lock.unlock();
            }
        }

        public void deposit(double amount) {
            try {
                lock.lock();
                this.money += amount;
            } finally {
                lock.unlock();
            }
        }

        public Integer getId() {
            return this.id;
        }

        public double getMoney() {
            return money;
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }

    }
