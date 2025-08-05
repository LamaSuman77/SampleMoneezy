package MyFinance.Moneezy.service;

import MyFinance.Moneezy.entity.Account;
import MyFinance.Moneezy.entity.Transaction;
import MyFinance.Moneezy.entity.TransactionType;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.repository.AccountRepository;
import MyFinance.Moneezy.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    // 🔹 Add transaction & auto-update account balance
    public void addTransactionAndUpdateBalance(Transaction transaction) {
        Account account = transaction.getAccount();
        if (account == null) {
            throw new IllegalArgumentException("Transaction must be linked to an account.");
        }

        if (transaction.getType() == TransactionType.REVENUE) {
            account.setBalance(account.getBalance() + transaction.getAmount());
        } else if (transaction.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance() - transaction.getAmount());
        }

        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    // 🔹 Admin use: get all transactions
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    // 🔹 Get all transactions for a specific user
    public List<Transaction> getAllByUser(User user) {
        return transactionRepository.findAllByUser(user);
    }

    // 🔹 Create and save a transaction for a user (no balance update here)
    public Transaction create(Transaction transaction, User user) {
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    // 🔹 Delete a transaction by ID only if it belongs to the user
    public boolean deleteByIdAndUser(Long id, User user) {
        Optional<Transaction> tx = transactionRepository.findById(id);
        if (tx.isPresent() && tx.get().getUser().getId().equals(user.getId())) {
            transactionRepository.delete(tx.get());
            return true;
        }
        return false;
    }

    // 🔹 Get transaction by ID
    public Transaction getById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    // 🔹 Calculate total revenue for user
    public double getTotalRevenue(User user) {
        return transactionRepository.findAllByUser(user).stream()
                .filter(tx -> tx.getType() == TransactionType.REVENUE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // 🔹 Calculate total revenue across all users
    public double getTotalRevenueAllUsers() {
        return transactionRepository.findAll().stream()
                .filter(tx -> tx.getType() == TransactionType.REVENUE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // 🔹 Calculate total expenses for user
    public double getTotalExpenses(User user) {
        return transactionRepository.findAllByUser(user).stream()
                .filter(tx -> tx.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // 🔹 Calculate total expenses across all users
    public double getTotalExpensesAllUsers() {
        return transactionRepository.findAll().stream()
                .filter(tx -> tx.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // 🔹 Net savings = revenue - expense
    public double getNetSavings(User user) {
        return getTotalRevenue(user) - getTotalExpenses(user);
    }

    // 🔹 Recent transactions sorted by date
    public List<Transaction> getRecentTransactions(User user, int limit) {
        return transactionRepository.findAllByUser(user).stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .limit(limit)
                .toList(); // Java 16+; use Collectors.toList() if older
    }

    // 🔹 Save or update transaction
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // 🔹 Monthly revenue sums
    public List<Double> getMonthlyRevenues(User user) {
        return getMonthlySums(user, TransactionType.REVENUE);
    }

    // 🔹 Monthly expense sums
    public List<Double> getMonthlyExpenses(User user) {
        return getMonthlySums(user, TransactionType.EXPENSE);
    }

    // 🔹 Helper to calculate monthly totals
    private List<Double> getMonthlySums(User user, TransactionType type) {
        List<Double> monthlySums = new ArrayList<>(Collections.nCopies(12, 0.0));
        List<Transaction> transactions = transactionRepository.findAllByUserAndType(user, type);

        for (Transaction tx : transactions) {
            if (tx.getDate() != null) {
                int monthIndex = tx.getDate().getMonthValue() - 1;
                double currentSum = monthlySums.get(monthIndex);
                monthlySums.set(monthIndex, currentSum + Math.abs(tx.getAmount()));
            }
        }

        return monthlySums;
    }

    // 🔹 Get all revenues for user
    public List<Transaction> getAllRevenueByUser(User user) {
        return transactionRepository.findAllByUserAndType(user, TransactionType.REVENUE);
    }

    // 🔹 Get all expenses for user
    public List<Transaction> getAllExpenseByUser(User user) {
        return transactionRepository.findAllByUserAndType(user, TransactionType.EXPENSE);
    }
}
