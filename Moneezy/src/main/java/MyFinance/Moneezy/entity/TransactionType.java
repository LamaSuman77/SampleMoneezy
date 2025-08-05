package MyFinance.Moneezy.entity;

public enum TransactionType {
    EXPENSE,
    REVENUE;

    @Override
    public String toString() {
        return name(); // You can change to name().toLowerCase() if needed
    }
}
