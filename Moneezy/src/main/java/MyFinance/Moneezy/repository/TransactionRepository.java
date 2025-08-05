package MyFinance.Moneezy.repository;

import MyFinance.Moneezy.entity.Transaction;
import MyFinance.Moneezy.entity.TransactionType;
import MyFinance.Moneezy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 🔹 All transactions for a specific user
    List<Transaction> findAllByUser(User user);

    // 🔹 All transactions for a user by type (REVENUE or EXPENSE)
    List<Transaction> findAllByUserAndType(User user, TransactionType type);

    // 🔹 All REVENUE or EXPENSE transactions across all users
    List<Transaction> findAllByType(TransactionType type);

    // 🔹 Ordered user transactions (for recent history)
    List<Transaction> findByUserOrderByDateDesc(User user);

    // ✅ FIXED: Sum total amount by transaction type (correct property: type)
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type")
    double sumAmountByType(@Param("type") TransactionType type);
}
