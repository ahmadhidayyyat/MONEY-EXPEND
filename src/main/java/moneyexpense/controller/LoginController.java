package moneyexpense.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import moneyexpense.services.ManajemenAkun;
import moneyexpense.services.NavigatorService;

public class LoginController {

    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private Button tombolLogin;
    @FXML
    private Button tombolKeRegister;
    @FXML
    private Label labelPesan;

    private final ManajemenAkun manajemenAkun = new ManajemenAkun();
    private final NavigatorService navigatorService = new NavigatorService();

    @FXML
    void handleLoginButton(ActionEvent event) {
        String username = inputUsername.getText();
        String password = inputPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            labelPesan.setText("Username dan password tidak boleh kosong.");
            labelPesan.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean isSuccess = manajemenAkun.loginUser(username, password);

        if (isSuccess) {
            labelPesan.setText("Login berhasil!");
            labelPesan.setStyle("-fx-text-fill: green;");
            navigatorService.navigateTo("/moneyexpense/view/MainView.fxml", tombolLogin);
        } else {
            labelPesan.setText("Username atau password salah.");
            labelPesan.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void handleKeRegisterButton(ActionEvent event) {
        navigatorService.navigateTo("/moneyexpense/view/RegisterView.fxml", tombolKeRegister);
    }
}