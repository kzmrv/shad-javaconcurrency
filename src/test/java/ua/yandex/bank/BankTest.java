/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.bank;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

/**
 *
 * @author Vasyl
 */
public class BankTest {

    public BankTest() {
    }

    @Test
    public void testTransfer() throws InterruptedException {

        System.out.println("Transfer test started");
        Bank bank = new Bank();
        Random random = new Random();
        Account[] accounts = new Account[10000];
        // Seed with ints to avoid overflow
        for (int i = 0; i < 10000; i++) {
            accounts[i] = new Account(i, random.nextInt(100000));
            bank.addAccount(accounts[i]);
        }
        int p = (int) accounts[100].getMoney();
        long sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum += accounts[i].getMoney();
        }
        Thread[] threads = new Thread[10000];
        for (int i = 0; i < 10000; i++) {
            threads[i] = new Thread(new TransferTask(bank));
        }
        for (int i = 0; i < 10000; i++) {
            threads[i].start();
        }
        Thread.sleep(1000);
        for (int i = 0; i < 10000; i++) {
            threads[i].interrupt();
        }
        long sumAfter = 0;
        for (int i = 0; i < 10000; i++) {
            sumAfter += accounts[i].getMoney();
        }
        assertEquals(sum, sumAfter);
        System.out.println("Sum before " + sum);
        System.out.println("Sum after " + sumAfter);
        System.out.println(p);
        System.out.println(accounts[100].getMoney());
        System.out.println("Transfer test finished");
    }

    class TransferTask implements Runnable {

        private Bank bank;

        public TransferTask(Bank bank) {
            this.bank = bank;
        }

        @Override
        public void run() {
            Random random = new Random();
            while (!Thread.currentThread().isInterrupted()) {
                int from = random.nextInt(10000);
                int to = random.nextInt(10000);
                int amount = random.nextInt(1000);
                try {
                    bank.transfer(from, to, amount);
                } catch (Bank.NotEnoughMoneyException ex) {
                }
            }
        }

    }
}
