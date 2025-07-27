package MyFinance.Moneezy.service;

import MyFinance.Moneezy.entity.Budget;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    // ✅ Get all budgets for a specific user
    public List<Budget> getAllByUser(User user) {
        return budgetRepository.findAllByUser(user);
    }

    // ✅ Get a single budget (owned by user)
    public Budget getByIdAndUser(Long id, User user) {
        Optional<Budget> budget = budgetRepository.findById(id);
        return budget.filter(b -> b.getUser().getId().equals(user.getId())).orElse(null);
    }

    // ✅ Create a budget and assign user
    public Budget create(Budget budget, User user) {
        budget.setUser(user);
        return budgetRepository.save(budget);
    }

    // ✅ Delete budget only if owned by user
    public boolean deleteByIdAndUser(Long id, User user) {
        Budget budget = getByIdAndUser(id, user);
        if (budget != null) {
            budgetRepository.delete(budget);
            return true;
        }
        return false;
    }

    // ✅ Admin only
    public List<Budget> getAll() {
        return budgetRepository.findAll();
    }
}
