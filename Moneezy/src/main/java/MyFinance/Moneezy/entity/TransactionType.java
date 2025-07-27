package MyFinance.Moneezy.entity;

public enum TransactionType {
    EXPENSE,
    REVENUE;

    @Override
    public String toString() {
        return name(); // Or return name().toLowerCase() if needed
    }
}
