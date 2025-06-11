package moneyexpense.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Kelas pembantu untuk menangani navigasi antar scene (halaman FXML)
 * di dalam aplikasi.
 */
public class NavigatorService {

    /**
     * Metode statis untuk berpindah dari satu scene ke scene lainnya.
     *
     * @param fxmlPath Path ke file FXML tujuan (misal: "/moneyexpense/view/MainView.fxml").
     * @param sourceNode Salah satu elemen UI dari scene asal (misal: sebuah tombol)
     * yang digunakan untuk mendapatkan Stage (window) saat ini.
     */
    public static void navigateTo(String fxmlPath, Node sourceNode) {
        try {
            // Memastikan path resource valid
            URL resourceUrl = NavigatorService.class.getResource(fxmlPath);
            if (resourceUrl == null) {
                System.err.println("File FXML tidak ditemukan di: " + fxmlPath);
                return;
            }

            // Memuat root layout dari file FXML tujuan
            Parent root = FXMLLoader.load(resourceUrl);

            // Mendapatkan stage (window) saat ini dari sourceNode
            Stage stage = (Stage) sourceNode.getScene().getWindow();

            // Membuat scene baru dengan root yang sudah dimuat
            Scene scene = new Scene(root);

            // Mengatur scene baru ke stage
            stage.setScene(scene);

            // Menampilkan stage
            stage.show();

        } catch (IOException e) {
            // Menangani error jika file FXML tidak bisa dimuat
            System.err.println("Gagal memuat FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
