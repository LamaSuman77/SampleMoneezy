package MyFinance.Moneezy.DTO;

import java.time.LocalDate;

public class TransactionDTO {
    private Long id;
    private String category;
    private double amount;
    private String type;
    private LocalDate date;

    public TransactionDTO() {}

    public TransactionDTO(Long id, String category, double amount, String type, LocalDate date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
