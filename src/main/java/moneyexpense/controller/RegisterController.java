package moneyexpense.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import moneyexpense.models.Pengguna; // Anda perlu membuat kelas ini nanti
import moneyexpense.services.NavigatorService;

import java.sql.SQLException;

/**
 * Controller untuk mengelola logika dari RegisterView.fxml.
 * Menangani pembuatan akun pengguna baru.
 */
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

    /**
     * Dieksekusi saat tombol 'Register' diklik.
     * Memvalidasi input dan mencoba membuat akun baru.
     *
     * @param event Aksi event dari klik tombol.
     */
    @FXML
    void handleRegisterButton(ActionEvent event) {
        String username = inputUsername.getText();
        String password = inputPassword.getText();
        String konfirmasiPassword = inputKonfirmasiPassword.getText();

        // 1. Validasi input
        if (username.isEmpty() || password.isEmpty() || konfirmasiPassword.isEmpty()) {
            labelPesanError.setText("Semua field wajib diisi!");
            return;
        }

        if (!password.equals(konfirmasiPassword)) {
            labelPesanError.setText("Password dan konfirmasi password tidak cocok!");
            return;
        }

        // 2. Jika validasi lolos, coba buat akun baru
        try {
            // Anda perlu membuat logika untuk registrasi di backend (misal, di kelas Pengguna atau service)
            // Pengguna.register(username, password); 

            // Jika berhasil, beri pesan sukses dan arahkan ke login
            // Untuk sekarang kita anggap selalu berhasil dan langsung navigasi
            System.out.println("Registrasi berhasil untuk username: " + username);
            NavigatorService.navigateTo("/view/LoginView.fxml", tombolRegister);

        } catch (Exception e) { // Sebaiknya gunakan exception yang lebih spesifik
            // Jika username sudah ada, atau ada error database
            labelPesanError.setText("Username sudah digunakan atau terjadi error.");
            System.err.println("Registrasi error: " + e.getMessage());
        }
    }

    /**
     * Dieksekusi saat tombol 'Log In Now' diklik.
     * Mengarahkan pengguna kembali ke halaman login.
     *
     * @param event Aksi event dari klik tombol.
     */
    @FXML
    void handleKeLoginButton(ActionEvent event) {
        NavigatorService.navigateTo("/moneyexpense/view/LoginView.fxml", tombolKeLogin);
    }
}