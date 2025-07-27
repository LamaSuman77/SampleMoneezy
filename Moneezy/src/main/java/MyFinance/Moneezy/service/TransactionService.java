package MyFinance.Moneezy.service;

import MyFinance.Moneezy.entity.Transaction;
import MyFinance.Moneezy.entity.TransactionType;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // ✅ Admin use: get all transactions
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    // ✅ Get all transactions for a specific user
    public List<Transaction> getAllByUser(User user) {
        return transactionRepository.findAllByUser(user);
    }

    // ✅ Create and save a transaction for a user
    public Transaction create(Transaction transaction, User user) {
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    // ✅ Delete a transaction by ID only if it belongs to the user
    public boolean deleteByIdAndUser(Long id, User user) {
        Optional<Transaction> tx = transactionRepository.findById(id);
        if (tx.isPresent() && tx.get().getUser().getId().equals(user.getId())) {
            transactionRepository.delete(tx.get());
            return true;
        }
        return false;
    }

    // ✅ Get transaction by ID
    public Transaction getById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    // ✅ Calculate total revenue for user
    public double calculateTotalRevenue(User user) {
        return transactionRepository.findAllByUser(user).stream()
                .filter(tx -> tx.getType() == TransactionType.REVENUE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // ✅ Calculate total expenses for user
    public double calculateTotalExpenses(User user) {
        return transactionRepository.findAllByUser(user).stream()
                .filter(tx -> tx.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // ✅ Recent transactions sorted by date descending
    public List<Transaction> getRecentTransactions(User user, int limit) {
        return transactionRepository.findAllByUser(user).stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .limit(limit)
                .toList();
    }

    // ✅ Save or update a transaction
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // ✅ Monthly revenue sums (Jan to Dec)
    public List<Double> getMonthlyRevenues(User user) {
        return getMonthlySums(user, TransactionType.REVENUE);
    }

    // ✅ Monthly expense sums (Jan to Dec)
    public List<Double> getMonthlyExpenses(User user) {
        return getMonthlySums(user, TransactionType.EXPENSE);
    }

    // ✅ Helper to calculate monthly totals by type
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

    // ✅ Public helper getters
    public double getTotalRevenue(User user) {
        return calculateTotalRevenue(user);
    }

    public double getTotalExpenses(User user) {
        return calculateTotalExpenses(user);
    }

    public double getNetSavings(User user) {
        return calculateTotalRevenue(user) - calculateTotalExpenses(user);
    }

    // ✅ Get all revenues for a user
    public List<Transaction> getAllRevenueByUser(User user) {
        return transactionRepository.findAllByUserAndType(user, TransactionType.REVENUE);
    }

    // ✅ Get all expenses for a user
    public List<Transaction> getAllExpenseByUser(User user) {
        return transactionRepository.findAllByUserAndType(user, TransactionType.EXPENSE);
    }
}
