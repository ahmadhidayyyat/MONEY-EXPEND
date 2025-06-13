package moneyexpense.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import moneyexpense.services.ManajemenAkun;
import moneyexpense.services.NavigatorService;

public class LoginController {

    @FXML private TextField inputUsername;
    @FXML private PasswordField inputPassword;
    @FXML private Button tombolLogin;
    @FXML private Button tombolKeRegister;

    private final ManajemenAkun manajemenAkun = new ManajemenAkun();

    @FXML
    void handleLoginButton(ActionEvent event) {
        String username = inputUsername.getText().trim();
        String password = inputPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", null, "Username dan password tidak boleh kosong!");
            return;
        }

        try {
            boolean usernameExists = manajemenAkun.isUsernameExists(username);
            if (!usernameExists) {
                showAlert(Alert.AlertType.ERROR, "Error Login", null, "Username tidak ditemukan.");
                return;
            }

            boolean loginSuccess = manajemenAkun.loginUser(username, password);
            if (loginSuccess) {
                NavigatorService.navigateTo("/moneyexpense/view/MainView.fxml", tombolLogin);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error Login", null, "Password salah. Coba lagi.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Terjadi error pada database. Coba lagi nanti.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleKeRegisterButton(ActionEvent event) {
        NavigatorService.navigateTo("/moneyexpense/view/RegisterView.fxml", tombolKeRegister);
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        if (header != null) alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
