package MyFinance.Moneezy.repository;

import MyFinance.Moneezy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    // 🔍 Admin search bar support
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);

    // 📊 Reporting (optional, for transaction count)
    @Query("SELECT u FROM User u ORDER BY size(u.transactions) DESC")
    List<User> findTop5ByOrderByTransactionsDesc();
}
