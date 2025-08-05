package MyFinance.Moneezy.service;

import MyFinance.Moneezy.entity.User;
import MyFinance.Moneezy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(User user) {
        // Prevent duplicate username (and email if you want that too)
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        // Optional: enforce unique email if you marked it unique in the entity
        // (Requires: boolean existsByEmail(String email) in UserRepository)
        // if (userRepository.existsByEmail(user.getEmail())) {
        //     return false;
        // }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /** Create or update a user. */
    public User save(User user) {
        return userRepository.save(user);
    }

    /** Alias to match controller calls (fixes "Cannot resolve method saveUser"). */
    public User saveUser(User user) {
        return save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean isPasswordMatching(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
