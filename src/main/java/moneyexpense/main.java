package moneyexpense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Kita akan memuat FXML untuk halaman utama untuk tes
            String fxmlPath = "/moneyexpense/view/LoginView.fxml"; 
            URL fxmlUrl = getClass().getResource(fxmlPath);

            if (fxmlUrl == null) {
                // Ini akan dieksekusi jika file tidak ditemukan
                throw new IOException("File FXML tidak ditemukan. Pastikan path Anda benar: " + fxmlPath);
            }
            
            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root);

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
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Gagal total saat memulai aplikasi:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}