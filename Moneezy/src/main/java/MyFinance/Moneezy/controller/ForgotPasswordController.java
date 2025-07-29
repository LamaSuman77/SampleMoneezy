package MyFinance.Moneezy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ForgotPasswordController {

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password"; // Renders forgot-password.html
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("username") String username,
                                        RedirectAttributes redirectAttributes) {

        // You can later enhance this to check if user exists and send a real reset link.
        redirectAttributes.addFlashAttribute("message", "Password reset instructions sent if user exists.");
        return "redirect:/login";
    }
}
