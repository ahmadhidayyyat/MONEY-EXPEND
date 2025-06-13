package moneyexpense.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import moneyexpense.models.Pengeluaran;
import moneyexpense.models.Pengguna;
import moneyexpense.services.AuthService;
import moneyexpense.services.ManajemenAkun;
import moneyexpense.services.NavigatorService;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Label labelSelamatDatang;
    @FXML private Label labelTotalBulanIni;
    @FXML private TableView<Pengeluaran> tableViewPengeluaran;
    @FXML private TableColumn<Pengeluaran, String> kolomTanggal;
    @FXML private TableColumn<Pengeluaran, String> kolomKeterangan;
    @FXML private TableColumn<Pengeluaran, Double> kolomJumlah;
    @FXML private TableColumn<Pengeluaran, Void> kolomAksi;
    @FXML private DatePicker inputTanggal;
    @FXML private TextArea inputKeterangan; // Changed from TextField to TextArea
    @FXML private TextField inputJumlah;
    @FXML private Button tombolTambah;
    @FXML private Button tombolUpdate;
    @FXML private Button tombolLogout;
    @FXML private Button cancelEditButton; // Added FXML field

    private final ManajemenAkun manajemenAkun = new ManajemenAkun();
    private final NavigatorService navigatorService = new NavigatorService();
    private final AuthService authService = AuthService.getInstance();
    private final ObservableList<Pengeluaran> daftarPengeluaran = FXCollections.observableArrayList();
    private Pengeluaran pengeluaranTerpilih = null;

    @FXML
    void handleCancelEditButton(ActionEvent event) {
        // Logic for handling the cancel button click
        clearForm();
        tombolTambah.setVisible(true);
        tombolUpdate.setVisible(false);
        cancelEditButton.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pengguna penggunaSaatIni = authService.getCurrentUser();
        if (penggunaSaatIni != null) {
            labelSelamatDatang.setText("Selamat Datang, " + penggunaSaatIni.getUsername() + "!");
        }

        inputTanggal.setValue(LocalDate.now());
        setupTable();
        loadData();

        tombolTambah.setVisible(true);
        tombolUpdate.setVisible(false);
        cancelEditButton.setVisible(false);

        tableViewPengeluaran.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    fillForm(newSelection);
                }
            }
        );
    }

    private void setupTable() {
        kolomTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        kolomKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        kolomJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));

        kolomJumlah.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double jumlah, boolean empty) {
                super.updateItem(jumlah, empty);
                if (empty || jumlah == null) {
                    setText(null);
                } else {
                    setText(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(jumlah));
                }
            }
        });

        addButtonsToTable();
    }

    private void addButtonsToTable() {
        Callback<TableColumn<Pengeluaran, Void>, TableCell<Pengeluaran, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnHapus = new Button("Hapus");

            {
                btnHapus.setOnAction((ActionEvent event) -> {
                    Pengeluaran data = getTableView().getItems().get(getIndex());
                    handleHapus(data);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnHapus);
                }
            }
        };
        kolomAksi.setCellFactory(cellFactory);
    }
    
    private void loadData() {
        daftarPengeluaran.setAll(manajemenAkun.getDaftarPengeluaranPengguna());
        tableViewPengeluaran.setItems(daftarPengeluaran);

        double total = manajemenAkun.getRingkasanTotalBulanIni();
        labelTotalBulanIni.setText(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(total));
        clearForm();
    }
    
@FXML
void handleTambahButton(ActionEvent event) {
    try {
        String tanggal = inputTanggal.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE);
        double jumlah = Double.parseDouble(inputJumlah.getText());
        String keterangan = inputKeterangan.getText();

        boolean success = manajemenAkun.catatPengeluaranBaru(tanggal, jumlah, keterangan);
        if (!success) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Gagal mencatat pengeluaran. Pastikan data valid dan Anda telah login.");
            return;
        }
        loadData();
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.WARNING, "Peringatan", null, "Input jumlah tidak valid. Masukkan angka yang benar.");
    }
}

@FXML
void handleUpdateButton(ActionEvent event) {
    if (pengeluaranTerpilih == null) return;

    try {
        pengeluaranTerpilih.setTanggal(inputTanggal.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
        pengeluaranTerpilih.setJumlah(Double.parseDouble(inputJumlah.getText()));
        pengeluaranTerpilih.setKeterangan(inputKeterangan.getText());

        boolean success = manajemenAkun.updateDataPengeluaran(pengeluaranTerpilih);
        if (!success) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Gagal mengupdate pengeluaran. Pastikan data valid dan Anda memiliki izin.");
            return;
        }
        loadData();
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.WARNING, "Peringatan", null, "Input jumlah tidak valid. Masukkan angka yang benar.");
    }
}

private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    if (header != null) alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
}

    
    private void handleHapus(Pengeluaran pengeluaran) {
        manajemenAkun.hapusDataPengeluaran(pengeluaran.getId());
        loadData();
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        manajemenAkun.logoutUser();
        navigatorService.navigateTo( "/moneyexpense/view/LoginView.fxml", tombolLogout);
    }

    private void fillForm(Pengeluaran pengeluaran) {
        pengeluaranTerpilih = pengeluaran;
        inputTanggal.setValue(LocalDate.parse(pengeluaran.getTanggal()));
        inputKeterangan.setText(pengeluaran.getKeterangan());
        inputJumlah.setText(String.valueOf(pengeluaran.getJumlah()));

        tombolTambah.setVisible(false);
        tombolUpdate.setVisible(true);
        cancelEditButton.setVisible(true);
    }

    private void clearForm() {
        pengeluaranTerpilih = null;
        inputTanggal.setValue(LocalDate.now());
        inputKeterangan.clear();
        inputJumlah.clear();
        tableViewPengeluaran.getSelectionModel().clearSelection();

        tombolTambah.setVisible(true);
        tombolUpdate.setVisible(false);
        cancelEditButton.setVisible(false);
    }
}
