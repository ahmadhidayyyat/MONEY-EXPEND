package moneyexpense.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import moneyexpense.models.Pengguna;
import moneyexpense.services.AuthService;
import moneyexpense.services.NavigatorService;

import java.sql.SQLException;

/**
 * Controller untuk mengelola logika dari LoginView.fxml.
 * Kelas ini menghubungkan input pengguna dari UI dengan logika bisnis
 * dan otentikasi di backend.
 */
public class LoginController {

    // --- Injeksi komponen dari FXML ---
    // Variabel ini harus memiliki nama yang sama persis dengan fx:id di file FXML.
    
    @FXML
    private TextField inputUsername;

    @FXML
    private PasswordField inputPassword;

    @FXML
    private Button tombolLogin;

    @FXML
    private Button tombolKeRegister;

    @FXML
    private Label labelPesanError;

    /**
     * Method ini dieksekusi ketika tombol 'Login' (fx:id="tombolLogin") diklik.
     * Mengambil data dari form, memvalidasi, dan mencoba untuk login.
     *
     * @param event Aksi event dari klik tombol.
     */
    @FXML
    void handleLoginButton(ActionEvent event) {
        String username = inputUsername.getText();
        String password = inputPassword.getText();

        // Validasi sederhana agar input tidak kosong
        if (username.isEmpty() || password.isEmpty()) {
            labelPesanError.setText("Username dan password tidak boleh kosong!");
            return;
        }

        try {
            // Memanggil metode login statis dari kelas Pengguna
            Pengguna pengguna = Pengguna.login(username, password);
            
            // Jika login berhasil (tidak ada SQLException yang dilempar),
            // simpan sesi pengguna menggunakan AuthService.
            AuthService.getInstance().login(pengguna);
            
            // Navigasi ke halaman utama aplikasi
            NavigatorService.navigateTo("/view/MainView.fxml", tombolLogin);

        } catch (SQLException e) {
            // Jika Pengguna.login() melempar SQLException, berarti login gagal.
            // Tampilkan pesan error di UI.
            labelPesanError.setText("Username atau password salah. Coba lagi.");
            System.err.println("Login error: " + e.getMessage());
        }
    }

    /**
     * Method ini dieksekusi ketika tombol 'Daftar di sini' (fx:id="tombolKeRegister") diklik.
     * Mengarahkan pengguna ke halaman registrasi.
     *
     * @param event Aksi event dari klik tombol.
     */
    @FXML
    void handleKeRegisterButton(ActionEvent event) {
        // Gunakan helper class untuk berpindah scene ke halaman registrasi
        NavigatorService.navigateTo("/view/RegisterView.fxml", tombolKeRegister);
    }
}
