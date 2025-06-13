package moneyexpense.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import moneyexpense.exceptions.UsernameAlreadyExistsException;
import moneyexpense.services.ManajemenAkun;
import moneyexpense.services.NavigatorService;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private PasswordField inputKonfirmasiPassword;
    @FXML
    private Button tombolRegister; 
    @FXML
    private Button tombolKeLogin;
    @FXML
    private Label labelPesanError; 

    private final ManajemenAkun manajemenAkun = new ManajemenAkun();
    private final NavigatorService navigatorService = new NavigatorService();

    @FXML
    void handleRegisterButton(ActionEvent event) {
        String username = inputUsername.getText();
        String password = inputPassword.getText();
        String konfirmasiPassword = inputKonfirmasiPassword.getText();

        if (username.isEmpty() || password.isEmpty() || konfirmasiPassword.isEmpty()) {
            labelPesanError.setText("Semua field harus diisi!");
            labelPesanError.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!password.equals(konfirmasiPassword)) {
            labelPesanError.setText("Password dan konfirmasi tidak cocok!");
            labelPesanError.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            manajemenAkun.registrasiUser(username, password);
            labelPesanError.setText("Registrasi berhasil! Silakan kembali ke halaman login.");
            labelPesanError.setStyle("-fx-text-fill: green;");
        } catch (UsernameAlreadyExistsException e) {
            labelPesanError.setText(e.getMessage());
            labelPesanError.setStyle("-fx-text-fill: red;");
        } catch (SQLException e) {
            labelPesanError.setText("Terjadi kesalahan pada database. Coba lagi nanti.");
            labelPesanError.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    @FXML
    void handleKeLoginButton(ActionEvent event) {
        navigatorService.navigateTo("/moneyexpense/view/LoginView.fxml", tombolKeLogin);
    }
}