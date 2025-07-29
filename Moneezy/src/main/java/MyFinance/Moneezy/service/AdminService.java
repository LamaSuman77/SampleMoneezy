package MyFinance.Moneezy.service;

import MyFinance.Moneezy.entity.Admin;
import MyFinance.Moneezy.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username).orElse(null);
    }

    public void save(Admin admin) {
        adminRepository.save(admin);
    }

    public void delete(Admin admin) {
        adminRepository.delete(admin);
    }

    public boolean isOldPasswordCorrect(Admin admin, String rawOldPassword) {
        return passwordEncoder.matches(rawOldPassword, admin.getPassword());
    }

    public void changePassword(Admin admin, String oldPassword, String newPassword) {
        if (isOldPasswordCorrect(admin, oldPassword)) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);
        }
    }

    // ✅ This method is required for resetting user password
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
