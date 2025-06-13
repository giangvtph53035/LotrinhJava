package service;

import model.Account;

import java.util.List;

public class ReportService {
    public void generate(List<Account> accounts) {
        long total = accounts.parallelStream()
                .mapToLong(Account::getBalance)
                .sum();

        System.out.println("Balances: " + total);

        accounts.stream()
                .filter(acc -> acc.getBalance() > 500_000)
                .forEach(acc -> System.out.printf("Account %d have high balance: %d\n",
                        acc.getId(), acc.getBalance()));
    }
}
