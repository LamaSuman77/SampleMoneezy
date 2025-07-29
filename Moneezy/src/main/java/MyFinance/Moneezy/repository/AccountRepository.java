package MyFinance.Moneezy.repository;

import MyFinance.Moneezy.entity.Account;
import MyFinance.Moneezy.entity.User;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUser(User user);
}
