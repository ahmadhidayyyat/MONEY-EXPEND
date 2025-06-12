package moneyexpense.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class NavigatorService {

    public static void navigateTo(String fxmlPath, Node sourceNode) {
        try {
            URL fxmlUrl = NavigatorService.class.getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Error Navigasi: FXML tidak ditemukan di " + fxmlPath);
                return;
            }
            
            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root);

            // ==========================================================
            // === BAGIAN PENTING YANG MEMASTIKAN CSS SELALU DIMUAT ===
            // ==========================================================
            String cssPath = "/moneyexpense/styles/app.css";
            URL cssUrl = NavigatorService.class.getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Peringatan: File CSS tidak ditemukan saat navigasi.");
            }
            // ==========================================================
            
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.setScene(scene);
            
        } catch (IOException e) {
            System.err.println("Gagal memuat FXML saat navigasi: " + fxmlPath);
            e.printStackTrace();
        }
    }
}