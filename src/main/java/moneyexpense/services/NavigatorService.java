package moneyexpense.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class NavigatorService {

    @SuppressWarnings("exports")
    public static void navigateTo(String fxmlPath, Node sourceNode) {
        try {
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            URL fxmlUrl = NavigatorService.class.getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Error Navigasi: FXML tidak ditemukan di " + fxmlPath);
                return;
            }
            
            Parent root = FXMLLoader.load(fxmlUrl);
            Scene newScene = new Scene(root);

            // ==========================================================
            // === BAGIAN PENTING YANG MEMASTIKAN CSS SELALU DIMUAT ===
            // ==========================================================
            String cssPath = "/moneyexpense/styles/app.css";
            URL cssUrl = NavigatorService.class.getResource(cssPath);
            if (cssUrl != null) {
                newScene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Peringatan: File CSS tidak ditemukan saat navigasi.");
            }
            // ==========================================================
            
            stage.setScene(newScene);

            // Atur ulang ukuran stage ke ukuran sebelumnya.
            // Ini memastikan bahwa jika pengguna mengubah ukuran window,
            // ukuran tersebut dipertahankan saat berpindah scene.
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
        } catch (IOException e) {
            System.err.println("Gagal memuat FXML saat navigasi: " + fxmlPath);
            e.printStackTrace();
        }
    }
}