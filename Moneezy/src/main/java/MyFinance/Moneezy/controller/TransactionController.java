package MyFinance.Moneezy.controller;

import MyFinance.Moneezy.entity.Account;
import MyFinance.Moneezy.entity.Transaction;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.service.AccountService;
import MyFinance.Moneezy.service.TransactionService;
import MyFinance.Moneezy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public TransactionController(TransactionService transactionService, UserService userService, AccountService accountService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.accountService = accountService;
    }

    // 🔹 View all transactions
    @GetMapping
    public String listAllTransactions(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        List<Transaction> transactions = transactionService.getAllByUser(user);
        model.addAttribute("transactions", transactions);
        return "transactions/list";
    }

    // 🔹 View one transaction
    @GetMapping("/view/{id}")
    public String viewTransaction(@PathVariable Long id, Model model, Principal principal) {
        Transaction transaction = transactionService.getById(id);
        if (transaction == null || !transaction.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/transactions";
        }
        model.addAttribute("transaction", transaction);
        return "transactions/view";
    }

    // 🔹 Show transaction form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transactions/add";
    }

    // 🔹 Add transaction & update balance
    @PostMapping("/add")
    public String addTransaction(@ModelAttribute Transaction transaction, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        List<Account> accounts = user.getAccounts(); // Assuming getter returns List<Account>
        if (accounts == null || accounts.isEmpty()) {
            // Handle case where user has no accounts
            return "redirect:/transactions?error=noaccount";
        }

        Account account = accounts.get(0); // Pick the first account

        transaction.setUser(user);
        transaction.setAccount(account);

        transactionService.addTransactionAndUpdateBalance(transaction);
        return "redirect:/transactions";
    }
}
