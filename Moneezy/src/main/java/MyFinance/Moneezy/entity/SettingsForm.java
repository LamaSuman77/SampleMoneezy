package MyFinance.Moneezy.entity;


public class SettingsForm {
    private String email;
    private String preferredCurrency;
    private boolean notificationsEnabled;
    private String newPassword;
    private String confirmPassword;

    //Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "SettingsForm{" +
                "email=" + email +
                ", preferredCurrency=" + preferredCurrency +
                ", notificationsEnabled=" + notificationsEnabled +
                ", newPassword=" + newPassword +
                ", confirmPassword=" + confirmPassword +
                '}';
    }
}
