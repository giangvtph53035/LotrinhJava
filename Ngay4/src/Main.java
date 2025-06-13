import model.*;
import service.*;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            accounts.add(new Account(i, 1_000_000));
        }

        BankService bank = new BankService();
        ReportService report = new ReportService();
        ExecutorService pool = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 200; i++) {
            pool.submit(() -> {
                for (int j = 0; j < 5; j++) {
                    Account from = getRandom(accounts);
                    Account to = getRandom(accounts);
                    while (from == to) to = getRandom(accounts);

                    long amount = ThreadLocalRandom.current().nextInt(100_000);
                    bank.process(new Transaction(from, to, amount));
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Total successed transactions: " + bank.getSuccessCount());

        report.generate(accounts);
    }

    private static Account getRandom(List<Account> accounts) {
        return accounts.get(ThreadLocalRandom.current().nextInt(accounts.size()));
    }
}
