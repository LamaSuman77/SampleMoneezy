package MyFinance.Moneezy.repository;

import MyFinance.Moneezy.entity.Transaction;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ✅ For filtering by user only
    List<Transaction> findAllByUser(User user);

    // ✅ For filtering by user and type (e.g., REVENUE, EXPENSE)
    List<Transaction> findAllByUserAndType(User user, TransactionType type);
}
