package MyFinance.Moneezy.controller;

import MyFinance.Moneezy.DTO.TransactionDTO;
import MyFinance.Moneezy.DTO.UserDTO;
import MyFinance.Moneezy.entity.Admin;
import MyFinance.Moneezy.entity.Transaction;
import MyFinance.Moneezy.entity.TransactionType;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.repository.TransactionRepository;
import MyFinance.Moneezy.repository.UserRepository;
import MyFinance.Moneezy.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository,
                           TransactionRepository transactionRepository,
                           AdminService adminService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        long totalUsers = userRepository.count();
        long totalTransactions = transactionRepository.count();

        double totalRevenue = transactionRepository.findAll().stream()
                .filter(t -> t.getType() == TransactionType.REVENUE)
                .mapToDouble(Transaction::getAmount).sum();

        double totalExpenses = transactionRepository.findAll().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount).sum();

        model.addAttribute("adminUsername", principal.getName());
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalTransactions", totalTransactions);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalExpenses", totalExpenses);
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model,
                              @RequestParam(required = false) String query) {
        List<User> users;
        if (StringUtils.hasText(query)) {
            users = userRepository.findAll().stream()
                    .filter(user -> user.getUsername().contains(query) || user.getEmail().contains(query))
                    .collect(Collectors.toList());
        } else {
            users = userRepository.findAll();
        }
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
        model.addAttribute("users", userDTOs);
        return "admin/manage-users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Principal principal, HttpSession session) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getUsername().equals(principal.getName())) {
                userRepository.delete(user);
            } else {
                session.setAttribute("error", "You cannot delete your own account.");
            }
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/reset-password/{id}")
    public String resetUserPassword(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            user.setPassword(passwordEncoder.encode("default123"));
            userRepository.save(user);
        });
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/transactions")
    public String viewUserTransactions(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) return "redirect:/admin/users";

        User user = userOptional.get();
        List<Transaction> transactions = transactionRepository.findByUserOrderByDateDesc(user);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());

        Map<String, Double> monthlyRevenue = new TreeMap<>();
        Map<String, Double> monthlyExpense = new TreeMap<>();

        double totalRevenue = 0, totalExpense = 0;
        for (Transaction t : transactions) {
            String month = t.getDate().getMonth() + " " + t.getDate().getYear();
            if (t.getType() == TransactionType.REVENUE) {
                monthlyRevenue.merge(month, t.getAmount(), Double::sum);
                totalRevenue += t.getAmount();
            } else {
                monthlyExpense.merge(month, t.getAmount(), Double::sum);
                totalExpense += t.getAmount();
            }
        }

        double netBalance = totalRevenue - totalExpense;
        model.addAttribute("user", convertToUserDTO(user));
        model.addAttribute("transactions", transactionDTOs);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("monthlyExpense", monthlyExpense);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("netBalance", netBalance);
        return "admin/view-user-transactions";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        List<User> users = userRepository.findAll();

        Map<User, Long> transactionCounts = users.stream()
                .collect(Collectors.toMap(u -> u, u -> transactionRepository.findByUser(u).stream().count()));

        Map<User, Double> totalExpenses = users.stream()
                .collect(Collectors.toMap(u -> u, u -> transactionRepository.findByUserAndType(u, TransactionType.EXPENSE)
                        .stream().mapToDouble(Transaction::getAmount).sum()));

        Map<User, Double> totalRevenue = users.stream()
                .collect(Collectors.toMap(u -> u, u -> transactionRepository.findByUserAndType(u, TransactionType.REVENUE)
                        .stream().mapToDouble(Transaction::getAmount).sum()));

        Map<User, Double> totalSavings = users.stream()
                .collect(Collectors.toMap(u -> u,
                        u -> totalRevenue.getOrDefault(u, 0.0) - totalExpenses.getOrDefault(u, 0.0)));

        model.addAttribute("topActiveUsers", transactionCounts.entrySet().stream()
                .sorted(Map.Entry.<User, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> convertToUserDTO(entry.getKey()))
                .toList());

        model.addAttribute("topSpenders", totalExpenses.entrySet().stream()
                .sorted(Map.Entry.<User, Double>comparingByValue().reversed())
                .limit(5)
                .map(entry -> convertToUserDTO(entry.getKey()))
                .toList());

        model.addAttribute("topSavers", totalSavings.entrySet().stream()
                .sorted(Map.Entry.<User, Double>comparingByValue().reversed())
                .limit(5)
                .map(entry -> convertToUserDTO(entry.getKey()))
                .toList());

        return "admin/reports";
    }

    @GetMapping("/settings")
    public String settings(Model model, Principal principal) {
        Admin admin = adminService.getAdminByUsername(principal.getName());
        model.addAttribute("admin", admin);
        return "admin/settings";
    }

    @PostMapping("/settings")
    public String updateProfile(@ModelAttribute Admin admin,
                                @RequestParam String oldPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        Admin existingAdmin = adminService.getAdminByUsername(admin.getUsername());
        if (!passwordEncoder.matches(oldPassword, existingAdmin.getPassword())) {
            model.addAttribute("error", "Old password is incorrect");
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match");
        } else if (newPassword.length() < 8 || !newPassword.matches(".*[A-Z].*") || !newPassword.matches(".*\\d.*")) {
            model.addAttribute("error", "Password must be at least 8 characters, with one uppercase letter and one number");
        } else {
            existingAdmin.setPassword(passwordEncoder.encode(newPassword));
            adminService.saveAdmin(existingAdmin);
            model.addAttribute("success", "Password changed successfully");
        }
        model.addAttribute("admin", existingAdmin);
        return "admin/settings";
    }

    // ---------- DTO Converters ----------

    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.isEnabled());
        return dto;
    }

    private TransactionDTO convertToTransactionDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setDate(transaction.getDate());
        dto.setType(transaction.getType());
        dto.setDescription(transaction.getDescription());
        return dto;
    }
}
