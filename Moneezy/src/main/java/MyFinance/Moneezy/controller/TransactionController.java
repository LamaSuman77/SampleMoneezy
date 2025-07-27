package MyFinance.Moneezy.controller;

import MyFinance.Moneezy.entity.Transaction;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.service.TransactionService;
import MyFinance.Moneezy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    // ✅ View all transactions (Revenue + Expense)
    @GetMapping("/transactions")
    public String listAllTransactions(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        List<Transaction> transactions = transactionService.getAllByUser(user);
        model.addAttribute("transactions", transactions);
        return "transactions/list"; // → templates/transactions/list.html
    }

    // ✅ View a single transaction by ID
    @GetMapping("/transactions/view/{id}")
    public String viewTransaction(@org.springframework.web.bind.annotation.PathVariable Long id, Model model, Principal principal) {
        Transaction transaction = transactionService.getById(id);
        if (transaction == null || !transaction.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/transactions";
        }
        model.addAttribute("transaction", transaction);
        return "transactions/view"; // → templates/transactions/view.html
    }
}
