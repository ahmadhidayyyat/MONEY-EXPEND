package moneyexpense.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import moneyexpense.models.Pengguna;
import moneyexpense.services.AuthService;
import moneyexpense.services.NavigatorService;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller untuk mengelola logika dari MainView.fxml.
 * Kelas ini bertanggung jawab untuk menampilkan data setelah pengguna berhasil login
 * dan menangani interaksi pengguna di halaman utama.
 */
public class MainController implements Initializable {

    // --- Injeksi komponen dari FXML ---
    @FXML
    private Label labelSelamatDatang;

    @FXML
    private Button tombolLogout;

    /**
     * Metode ini dipanggil secara otomatis saat FXML dimuat.
     * Digunakan untuk inisialisasi data awal pada view.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Panggil AuthService untuk mendapatkan pengguna yang sedang login
        Pengguna penggunaSaatIni = AuthService.getInstance().getCurrentUser();
        
        // Periksa apakah ada pengguna yang login
        if (penggunaSaatIni != null) {
            // Jika ada, set teks label dengan nama pengguna
            labelSelamatDatang.setText("Selamat Datang, " + penggunaSaatIni.getUsername() + "!");
        } else {
            // Kasus jika halaman ini diakses tanpa login (seharusnya tidak terjadi)
            labelSelamatDatang.setText("Selamat Datang!");
            // Idealnya, navigasikan kembali ke halaman login jika tidak ada sesi
        }
    }

    /**
     * Metode ini dieksekusi ketika tombol 'Logout' (fx:id="tombolLogout") diklik.
     * Membersihkan sesi dan mengarahkan pengguna kembali ke halaman login.
     *
     * @param event Aksi event dari klik tombol.
     */
    @FXML
    void handleLogoutButton(ActionEvent event) {
        // Bersihkan sesi pengguna yang login
        AuthService.getInstance().logout();
        // Navigasi kembali ke halaman login
        NavigatorService.navigateTo("/view/LoginView.fxml", tombolLogout);
    }
}
