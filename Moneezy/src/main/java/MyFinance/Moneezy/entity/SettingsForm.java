package MyFinance.Moneezy.entity;

public class SettingsForm {
    private String email;
    private String preferredCurrency;
    private boolean notificationsEnabled;

    // Password fields
    private String currentPassword;   // <— added
    private String newPassword;
    private String confirmPassword;

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPreferredCurrency() { return preferredCurrency; }
    public void setPreferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; }

    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    @Override
    public String toString() {
        // Avoid logging passwords in real apps; shown here without them for safety
        return "SettingsForm{" +
                "email='" + email + '\'' +
                ", preferredCurrency='" + preferredCurrency + '\'' +
                ", notificationsEnabled=" + notificationsEnabled +
                '}';
    }
}
