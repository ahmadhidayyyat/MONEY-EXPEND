package moneyexpense.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import moneyexpense.exceptions.UsernameAlreadyExistsException;
import moneyexpense.services.ManajemenAkun;
import moneyexpense.services.NavigatorService;

import java.sql.SQLException;

public class RegisterController {

    @FXML private TextField inputUsername;
    @FXML private PasswordField inputPassword;
    @FXML private PasswordField inputKonfirmasiPassword;
    @FXML private Button tombolRegister;
    @FXML private Button tombolKeLogin;

    private final ManajemenAkun manajemenAkun = new ManajemenAkun();
    private final NavigatorService navigatorService = new NavigatorService();

    @FXML
    void handleRegisterButton(ActionEvent event) {
        String username = inputUsername.getText().trim();
        String password = inputPassword.getText();
        String konfirmasiPassword = inputKonfirmasiPassword.getText();

        if (username.isEmpty() || password.isEmpty() || konfirmasiPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", null, "Semua field harus diisi!");
            return;
        }
        if (!password.equals(konfirmasiPassword)) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", null, "Password dan konfirmasi tidak cocok!");
            return;
        }

        try {
            if (manajemenAkun.isUsernameExists(username)) {
                showAlert(Alert.AlertType.ERROR, "Error Registrasi", null, "Username sudah terdaftar!");
                return;
            }

            manajemenAkun.registrasiUser(username, password);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", null, "Akun berhasil dibuat! Mengalihkan ke halaman login...");

            // Navigasi otomatis ke login setelah 1 detik
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                Platform.runLater(() -> navigatorService.navigateTo("/moneyexpense/view/LoginView.fxml", tombolRegister));
            });
            thread.setDaemon(true);
            thread.start();

        } catch (UsernameAlreadyExistsException e) {
            showAlert(Alert.AlertType.ERROR, "Error Registrasi", null, e.getMessage());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Terjadi kesalahan pada database. Coba lagi nanti.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleKeLoginButton(ActionEvent event) {
        navigatorService.navigateTo("/moneyexpense/view/LoginView.fxml", tombolKeLogin);
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        if (header != null) alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
