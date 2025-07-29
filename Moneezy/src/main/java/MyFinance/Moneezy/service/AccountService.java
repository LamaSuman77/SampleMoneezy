package MyFinance.Moneezy.service;

import MyFinance.Moneezy.entity.Account;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Get all accounts for a specific user
    public List<Account> getAllByUser(User user) {
        return accountRepository.findAllByUser(user);
    }

    // Get one account if it belongs to the user (or null)
    public Account getByIdAndUser(Long id, User user) {
        Optional<Account> account = accountRepository.findById(id);
        return account.filter(a -> a.getUser().getId().equals(user.getId())).orElse(null);
    }

    // Create an account and associate it with a user
    public Account create(Account account, User user) {
        account.setUser(user);
        return accountRepository.save(account);
    }

    // Delete only if the account belongs to the user
    public boolean deleteByIdAndUser(Long id, User user) {
        Account account = getByIdAndUser(id, user);
        if (account != null) {
            accountRepository.delete(account);
            return true;
        }
        return false;
    }

    // For admin only: get all accounts across users
    public List<Account> getAll() {
        return accountRepository.findAll();
    }
}
