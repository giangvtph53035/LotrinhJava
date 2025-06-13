package util;

import model.Transaction;

public class EmailService {
    public static void sendConfirm(Transaction t) {
        System.out.printf("Email: Transaction from %d to %d: %d VND\n",
                t.from.getId(), t.to.getId(), t.amount);
    }
}
