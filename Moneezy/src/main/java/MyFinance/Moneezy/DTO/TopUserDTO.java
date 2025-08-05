package MyFinance.Moneezy.DTO;

public class TopUserDTO {
    private String username;
    private long transactionCount;
    private double totalRevenue;
    private double totalExpense;
    private double netSavings;

    public TopUserDTO() {}

    public TopUserDTO(String username, long transactionCount, double totalRevenue, double totalExpense, double netSavings) {
        this.username = username;
        this.transactionCount = transactionCount;
        this.totalRevenue = totalRevenue;
        this.totalExpense = totalExpense;
        this.netSavings = netSavings;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getNetSavings() {
        return netSavings;
    }

    public void setNetSavings(double netSavings) {
        this.netSavings = netSavings;
    }
}
