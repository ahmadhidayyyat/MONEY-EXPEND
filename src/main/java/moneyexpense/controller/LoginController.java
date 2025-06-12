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
   // MENJADI SEPERTI INI
@FXML
void handleLoginButton(ActionEvent event) {
    String username = inputUsername.getText();
    String password = inputPassword.getText();

    // Reset pesan error setiap kali tombol diklik
    labelPesanError.setText("");

    if (username.isEmpty() || password.isEmpty()) {
        labelPesanError.setText("Username dan password tidak boleh kosong!");
        return;
    }

    try {
        // 1. Panggil service untuk melakukan proses login.
        //    Metode login di service akan mengembalikan true jika berhasil, false jika gagal.
        AuthService authService = AuthService.getInstance();
        boolean isLoginSuccess = authService.login(username, password);

        // 2. Cek hasilnya
        if (isLoginSuccess) {
            // Jika berhasil, navigasi ke halaman utama. Path sudah diperbaiki.
            NavigatorService.navigateTo("/moneyexpense/view/MainView.fxml", tombolLogin);
        } else {
            // Jika gagal (misal: password salah), tampilkan pesan error.
            labelPesanError.setText("Username atau password salah. Coba lagi.");
        }
    } catch (Exception e) {
        // Exception ini ditangkap jika ada masalah koneksi ke database.
        labelPesanError.setText("Terjadi error pada database. Coba lagi nanti.");
        System.err.println("Database error saat login: " + e.getMessage());
        e.printStackTrace();
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
        NavigatorService.navigateTo("/moneyexpense/view/RegisterView.fxml", tombolKeRegister);
    }
}
