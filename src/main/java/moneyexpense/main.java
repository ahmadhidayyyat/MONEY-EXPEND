package moneyexpense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import moneyexpense.services.AuthService;
import moneyexpense.services.NavigatorService;

import java.io.IOException;

/**
 * Kelas utama untuk menjalankan aplikasi JavaFX Money Expense Tracker.
 * Kelas ini bertanggung jawab untuk memuat tampilan awal aplikasi.
 */
public class Main extends Application {

    /**
     * Metode start adalah titik masuk utama untuk semua aplikasi JavaFX.
     *
     * @param primaryStage Panggung utama (window) aplikasi yang disediakan oleh runtime JavaFX.
     * @throws IOException Jika file FXML tidak dapat ditemukan atau dimuat.
     */

    private AuthService authService;

    @Override
    public void init() throws Exception {
        authService = AuthService.getInstance();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // 1. Tentukan path ke file FXML awal Anda.
        //    Aplikasi harus selalu dimulai dari halaman Login.
        String fxmlFile = "/view/LoginView.fxml";
        
        // 2. Gunakan FXMLLoader untuk memuat file FXML.
        //    FXMLLoader akan membaca file FXML, membuat semua objek UI,
        //    dan secara otomatis membuat instance dari Controller yang ditentukan
        //    di dalam FXML (fx:controller="...").
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

        // 3. Buat Scene baru dengan root layout yang sudah dimuat.
        Scene scene = new Scene(root);

        // 4. Atur judul window (Stage).
        primaryStage.setTitle("Money Expense Tracker");

        // 5. Atur Scene untuk Stage.
        primaryStage.setScene(scene);
        
        // 6. Tampilkan Stage (window).
        primaryStage.show();
    }

    /**
     * Metode main adalah titik masuk standar untuk aplikasi Java.
     * Metode ini akan memanggil metode launch() dari kelas Application.
     *
     * @param args Argumen baris perintah (tidak digunakan di sini).
     */
    public static void main(String[] args) {
        launch(args);
    }
}