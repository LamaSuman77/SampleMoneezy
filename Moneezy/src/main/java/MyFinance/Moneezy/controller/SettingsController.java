package MyFinance.Moneezy.controller;

import MyFinance.Moneezy.entity.SettingsForm;
import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SettingsController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Show settings page
    @GetMapping
    public String showSettingsPage(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        SettingsForm form = new SettingsForm();
        form.setEmail(user.getEmail());
        form.setPreferredCurrency(user.getPreferredCurrency());
        form.setNotificationsEnabled(user.isNotificationsEnabled());

        model.addAttribute("settingsForm", form);
        return "settings";
    }

    // Update settings
    @PostMapping
    public String updateSettings(@ModelAttribute SettingsForm settingsForm,
                                 Principal principal,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        User user = userService.getUserByUsername(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        // Update preferences
        user.setEmail(settingsForm.getEmail());
        user.setPreferredCurrency(settingsForm.getPreferredCurrency());
        user.setNotificationsEnabled(settingsForm.isNotificationsEnabled());

        // Handle password change if provided
        if (settingsForm.getNewPassword() != null && !settingsForm.getNewPassword().isBlank()) {
            if (!settingsForm.getNewPassword().equals(settingsForm.getConfirmPassword())) {
                model.addAttribute("error", "New password and confirm password do not match.");
                model.addAttribute("settingsForm", settingsForm);
                return "settings";
            }

            user.setPassword(passwordEncoder.encode(settingsForm.getNewPassword()));
        }

        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "Settings updated successfully.");
        return "redirect:/settings";
    }

    // Delete account
    @PostMapping("/delete")
    @Transactional
    public String deleteAccount(Principal principal,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {

        User user = userService.getUserByUsername(principal.getName());
        if (user != null) {
            userService.delete(user);

            // Invalidate session
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();

            redirectAttributes.addFlashAttribute("message", "Your account has been deleted.");
        }

        return "redirect:/logout";
    }
}
