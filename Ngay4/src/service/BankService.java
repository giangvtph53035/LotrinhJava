package service;

import model.*;
import util.EmailService;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BankService {
    private final Semaphore semaphore = new Semaphore(10); 
    private final AtomicInteger successCount = new AtomicInteger();

    public void process(Transaction t) {
        try {
            semaphore.acquire();

            List<Account> locks = getLockOrder(t.from, t.to);
            locks.get(0).getLock().lock();
            locks.get(1).getLock().lock();

            if (t.from.withdraw(t.amount)) {
                t.to.deposit(t.amount);
                successCount.incrementAndGet();

                CompletableFuture.runAsync(() -> EmailService.sendConfirm(t));
            } else {
                System.out.printf("Account %d doesnt have enough money %n", t.from.getId());
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            t.to.getLock().unlock();
            t.from.getLock().unlock();
            semaphore.release();
        }
    }

    private List<Account> getLockOrder(Account a1, Account a2) {
        return a1.getId() < a2.getId() ? List.of(a1, a2) : List.of(a2, a1);
    }

    public int getSuccessCount() {
        return successCount.get();
    }
}
