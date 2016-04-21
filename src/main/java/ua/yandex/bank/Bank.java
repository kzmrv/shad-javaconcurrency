/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.bank;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vasyl
 */
public class Bank {

    private Map<Integer, Account> database;

    public Bank() {
        this.database = new HashMap<>();
    }

    public void transfer(int from, int to, double amount) {
        if (from == to) {
            return;
        }

        Account sender = database.get(from);
        Account receiver = database.get(to);
        // Lock account with greater id (dining philosophers problem)
        Account maxId = (from > to) ? sender : receiver;
        Account minId = (from > to) ? receiver : sender;
        // Lock both accounts
        try {
            maxId.lock();
            try {
                minId.lock();
                if (sender.getMoney() < amount) {
                    throw new NotEnoughMoneyException("Not enough money on "
                            + "acccount " + from);
                }
                sender.withdraw(amount);
                receiver.deposit(amount);
            } finally {
                minId.unlock();
            }
        } finally {
            maxId.unlock();
        }

    }

    public void addAccount(Account account) {
        database.put(account.getId(), account);
    }

    static class NotEnoughMoneyException extends IllegalStateException {

        public NotEnoughMoneyException() {
        }

        public NotEnoughMoneyException(String s) {
            super(s);
        }

        public NotEnoughMoneyException(String message, Throwable cause) {
            super(message, cause);
        }

        public NotEnoughMoneyException(Throwable cause) {
            super(cause);
        }

    }

}
