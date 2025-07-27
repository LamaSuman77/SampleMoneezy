package MyFinance.Moneezy.controller;

import MyFinance.Moneezy.entity.Transaction;
import MyFinance.Moneezy.entity.TransactionType;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.service.TransactionService;
import MyFinance.Moneezy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/revenues")
public class RevenueController {

    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public RevenueController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    // 📄 LIST all revenues
    @GetMapping
    public String listRevenues(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        List<Transaction> revenues = transactionService.getAllByUser(user).stream()
                .filter(tx -> tx.getType() == TransactionType.REVENUE)
                .toList();
        model.addAttribute("revenues", revenues);
        return "revenues/list"; // → templates/revenues/list.html
    }

    // ➕ SHOW form to add revenue
    @GetMapping("/new")
    public String showAddForm(Model model) {
        Transaction revenue = new Transaction();
        revenue.setDate(LocalDate.now());
        model.addAttribute("revenue", revenue);
        return "revenues/form"; // Shared revenue form
    }

    // 💾 SAVE new revenue
    @PostMapping
    public String saveRevenue(@ModelAttribute("revenue") Transaction revenue, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        revenue.setUser(user);
        revenue.setType(TransactionType.REVENUE);
        transactionService.saveTransaction(revenue);
        return "redirect:/revenues";
    }

    // 🖊️ EDIT an existing revenue
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        Transaction revenue = transactionService.getById(id);
        if (revenue == null || !revenue.getUser().getUsername().equals(principal.getName()) || revenue.getType() != TransactionType.REVENUE) {
            return "redirect:/revenues";
        }
        model.addAttribute("revenue", revenue);
        return "revenues/form";
    }

    // ✅ UPDATE revenue
    @PostMapping("/update/{id}")
    public String updateRevenue(@PathVariable Long id, @ModelAttribute("revenue") Transaction revenueForm, Principal principal) {
        Transaction existing = transactionService.getById(id);
        if (existing != null && existing.getUser().getUsername().equals(principal.getName()) && existing.getType() == TransactionType.REVENUE) {
            existing.setAmount(revenueForm.getAmount());
            existing.setDate(revenueForm.getDate());
            existing.setDescription(revenueForm.getDescription());
            transactionService.saveTransaction(existing);
        }
        return "redirect:/revenues";
    }

    // ❌ DELETE revenue
    @GetMapping("/delete/{id}")
    public String deleteRevenue(@PathVariable Long id, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        transactionService.deleteByIdAndUser(id, user);
        return "redirect:/revenues";
    }
}
