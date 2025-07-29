package MyFinance.Moneezy.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private double amount;

    private String description;

    @Enumerated(EnumType.STRING) // Store enum name like "EXPENSE"
    private TransactionType type;

    private String notes;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
