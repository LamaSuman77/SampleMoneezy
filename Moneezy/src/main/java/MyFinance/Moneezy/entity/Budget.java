package MyFinance.Moneezy.entity;

import jakarta.persistence.*;
import java.time.YearMonth;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private YearMonth month;

    private double amount;

    @ManyToOne
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public YearMonth getMonth() { return month; }
    public void setMonth(YearMonth month) { this.month = month; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
