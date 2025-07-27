package MyFinance.Moneezy.repository;

import MyFinance.Moneezy.entity.Budget;
import MyFinance.Moneezy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByUser(User user);
}
