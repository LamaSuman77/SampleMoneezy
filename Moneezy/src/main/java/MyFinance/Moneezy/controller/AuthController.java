package MyFinance.Moneezy.controller;

import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Show registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // ✅ Process user registration
    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user, Model model) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        model.addAttribute("success", "User registered successfully!");
        return "login";
    }

    // ✅ Show login form, support flash messages (e.g., after forgot password)
    @GetMapping("/login")
    public String showLoginForm(Model model, @ModelAttribute("message") String message) {
        model.addAttribute("message", message);
        return "login";
    }

    // ❌ Removed conflicting POST /forgot-password method
}
