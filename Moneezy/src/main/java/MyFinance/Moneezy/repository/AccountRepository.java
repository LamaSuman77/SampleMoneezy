package MyFinance.Moneezy.repository;

import MyFinance.Moneezy.entity.Account;
import MyFinance.Moneezy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUser(User user);
}
