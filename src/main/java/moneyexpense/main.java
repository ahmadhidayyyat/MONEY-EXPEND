package moneyexpense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    // Tentukan ukuran awal window berdasarkan tampilan terbesar (MainView.fxml)
    private static final double INITIAL_WIDTH = 1280;
    private static final double INITIAL_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tampilan awal adalah LoginView.fxml
            String fxmlPath = "/moneyexpense/view/LoginView.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);

            if (fxmlUrl == null) {
                // Ini akan dieksekusi jika file tidak ditemukan
                throw new IOException("File FXML tidak ditemukan. Pastikan path Anda benar: " + fxmlPath);
            }
            
            Parent root = FXMLLoader.load(fxmlUrl);
            // Buat scene dengan ukuran awal yang telah ditentukan
            // Ini memastikan window cukup besar untuk semua elemen, bahkan untuk tampilan terbesar.
            Scene scene = new Scene(root, INITIAL_WIDTH, INITIAL_HEIGHT);

            // Menambahkan CSS
            String cssPath = "/moneyexpense/styles/app.css";
            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Peringatan: File CSS tidak ditemukan di: " + cssPath);
            }

            primaryStage.setTitle("Money Expense Tracker");
            primaryStage.setScene(scene);
            // Ukuran stage akan mengikuti ukuran scene yang sudah diatur.
            // Jika pengguna mengubah ukuran window, NavigatorService akan mempertahankan ukuran baru tersebut.
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Gagal total saat memulai aplikasi:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        moneyexpense.utils.DatabaseConnection.initializeDatabase();
        
        launch(args);
    }
}