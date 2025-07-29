package MyFinance.Moneezy.config;

import MyFinance.Moneezy.entity.Admin;
import MyFinance.Moneezy.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner createDefaultAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminRepository.findByUsername("Admin").isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername("Admin");
                admin.setPassword(passwordEncoder.encode("admin@123"));
                admin.setEmail("admin@example.com");
                admin.setPreferredCurrency("USD");
                admin.setNotificationsEnabled(true);
                adminRepository.save(admin);
                System.out.println("Default admin user created.");
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }
}
