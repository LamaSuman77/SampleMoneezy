package MyFinance.Moneezy.service;

import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // ✅ Register user
    public boolean register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) return false;

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    // ✅ Get user by username (for login/session)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // ✅ Get user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // ✅ Save updated user (settings updates)
    public void save(User user) {
        userRepository.save(user);
    }

    // ✅ Delete user account
    public void delete(User user) {
        userRepository.delete(user);
    }

    // ✅ Change password safely
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // ✅ Validate if passwords match
    public boolean isPasswordMatching(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
