package moneyexpense.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainController {

    // --- Bagian Form Input ---
    @FXML
    private Label formTitleLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField amountField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label formErrorLabel;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelEditButton;

    // --- Bagian Lain ---
    @FXML
    private Button logoutButton;
    @FXML
    private VBox monthlyExpensesContainer;

    // Metode 'initialize' akan dipanggil secara otomatis setelah FXML dimuat.
    // Di sinilah kita akan memuat data awal.
    @FXML
    public void initialize() {
        System.out.println("MainViewController berhasil dimuat.");
        // Logika untuk memuat data pengeluaran akan ada di sini
    }

    // Metode ini akan dihubungkan ke tombol 'Add/Update Expense'
    @FXML
    void handleSubmitButton(ActionEvent event) {
        // Logika untuk menambah atau mengupdate pengeluaran
    }

    // Metode ini untuk tombol 'Cancel Edit'
    @FXML
    void handleCancelEditButton(ActionEvent event) {
        // Logika untuk membatalkan mode edit
    }

    // Metode ini untuk tombol 'Logout'
    @FXML
    void handleLogoutButton(ActionEvent event) {
        // Logika untuk logout
    }
}