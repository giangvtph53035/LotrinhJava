package model;

public class Transaction {
    public final Account from;
    public final Account to;
    public final long amount;

    public Transaction(Account from, Account to, long amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}
