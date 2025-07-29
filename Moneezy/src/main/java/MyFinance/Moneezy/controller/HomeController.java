package MyFinance.Moneezy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";  // Redirect root to /home
    }

    @GetMapping("/home")
    public String homePage() {
        return "welcome";  // Renders welcome.html
    }
}
