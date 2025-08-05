package MyFinance.Moneezy.controller;

import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.service.TransactionService;
import MyFinance.Moneezy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // MAIN DASHBOARD ROUTE
    @GetMapping("/dashboard")
    public String dashboardPage(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        double totalRevenue = transactionService.getTotalRevenue(user);
        double totalExpenses = transactionService.getTotalExpenses(user);
        double netSavings = transactionService.getNetSavings(user);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("netSavings", netSavings);

        return "dashboard"; // templates/dashboard.html
    }

    // REPORTS PAGE
    @GetMapping("/reports")
    public String reportsPage(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("revenues", transactionService.getMonthlyRevenues(user));
        model.addAttribute("expenses", transactionService.getMonthlyExpenses(user));
        return "reports"; // templates/reports.html
    }

    // User report endpoint (JSON)
    @GetMapping("/user/transactions/report")
    @ResponseBody
    public Map<String, Object> getUserReport(@RequestParam(defaultValue = "all") String period,
                                             @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.getUserByUsername(currentUser.getUsername());
        Map<String, Object> report = new HashMap<>();
        report.put("totalRevenue", transactionService.getTotalRevenue(user));
        report.put("totalExpenses", transactionService.getTotalExpenses(user));
        report.put("netBalance", transactionService.getNetSavings(user));
        report.put("transactions", transactionService.getAllByUser(user).size());
        return report;
    }

    // User profile update endpoint
    @PostMapping("/user/profile/update")
    @ResponseBody
    public Map<String, Object> updateUserProfile(@RequestBody Map<String, Object> profileData,
                                                 @AuthenticationPrincipal UserDetails currentUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.getUserByUsername(currentUser.getUsername());
            if (profileData.containsKey("email")) {
                user.setEmail((String) profileData.get("email"));
            }
            userService.saveUser(user);
            response.put("success", true);
            response.put("message", "Profile updated successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating profile: " + e.getMessage());
        }
        return response;
    }

    // User password change endpoint
    @PostMapping("/user/password/change")
    @ResponseBody
    public Map<String, Object> changeUserPassword(@RequestBody Map<String, String> passwordData,
                                                  @AuthenticationPrincipal UserDetails currentUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");

            User user = userService.getUserByUsername(currentUser.getUsername());

            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                response.put("success", false);
                response.put("message", "Current password is incorrect");
                return response;
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);

            response.put("success", true);
            response.put("message", "Password changed successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error changing password: " + e.getMessage());
        }
        return response;
    }
}
