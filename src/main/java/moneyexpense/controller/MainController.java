package moneyexpense.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import moneyexpense.models.Pengeluaran;
import moneyexpense.models.Pengguna;
import moneyexpense.services.AuthService;
import moneyexpense.services.ManajemenAkun;
import moneyexpense.services.NavigatorService;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class MainController implements Initializable {

    @FXML private Label labelSelamatDatang;
   
    @FXML private Label labelTotalExpenses;
    @FXML private Label labelAveragePerDay;
    @FXML private Label labelTotalTransactions;
    @FXML private TableView<Pengeluaran> tableViewPengeluaran;
    @FXML private TableColumn<Pengeluaran, String> kolomTanggal;
    @FXML private TableColumn<Pengeluaran, String> kolomKeterangan;
    @FXML private TableColumn<Pengeluaran, Double> kolomJumlah;
    @FXML private TableColumn<Pengeluaran, Void> kolomAksi;
    @FXML private DatePicker inputTanggal;
    @FXML private TextArea inputKeterangan;
    @FXML private TextField inputJumlah;
    @FXML private Button tombolTambah;
    @FXML private Button tombolUpdate;
    @FXML private Button tombolLogout;
    @FXML private Button cancelEditButton;

    private final ManajemenAkun manajemenAkun = new ManajemenAkun();
    private final NavigatorService navigatorService = new NavigatorService();
    private final AuthService authService = AuthService.getInstance();
    private final ObservableList<Pengeluaran> daftarPengeluaran = FXCollections.observableArrayList();
    private Pengeluaran pengeluaranTerpilih = null;

    @FXML
    void handleCancelEditButton(ActionEvent event) {
        clearForm();
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
    
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Tombol Edit
                    Button btnEdit = new Button("Edit");
                    btnEdit.getStyleClass().add("action-button-edit");
                    btnEdit.setOnAction((ActionEvent event) -> {
                        Pengeluaran data = getTableView().getItems().get(getIndex());
                        fillForm(data);
                    });
    
                    // Tombol Hapus
                    Button btnHapus = new Button("Hapus");
                    btnHapus.getStyleClass().add("action-button-delete");
                    btnHapus.setOnAction((ActionEvent event) -> {
                        Pengeluaran data = getTableView().getItems().get(getIndex());
                        handleHapus(data);
                    });
    
                    // Gabungkan dalam HBox
                    HBox hbox = new HBox(10, btnEdit, btnHapus);
                    hbox.setStyle("-fx-alignment: CENTER;");
    
                    setGraphic(hbox);
                }
            }
        };
    
        kolomAksi.setCellFactory(cellFactory);
    }
    
    

    private void updateSummaryBox() {
        double total = daftarPengeluaran.stream().mapToDouble(Pengeluaran::getJumlah).sum();
        Set<String> uniqueDates = new HashSet<>();
        for (Pengeluaran p : daftarPengeluaran) {
            uniqueDates.add(p.getTanggal());
        }
        long jumlahHariUnik = uniqueDates.size();
        int totalTransaksi = daftarPengeluaran.size();

        Locale localeID = new Locale("id", "ID");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(localeID);

        labelTotalExpenses.setText(currencyFormat.format(total));
        labelAveragePerDay.setText(currencyFormat.format(jumlahHariUnik > 0 ? total / jumlahHariUnik : 0));
        labelTotalTransactions.setText(String.valueOf(totalTransaksi));
    }

    @FXML
    void handleTambahButton(ActionEvent event) {
        try {
            String tanggal = inputTanggal.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE);
            double jumlah = Double.parseDouble(inputJumlah.getText());
            String keterangan = inputKeterangan.getText();

            boolean success = manajemenAkun.catatPengeluaranBaru(tanggal, jumlah, keterangan);
            if (!success) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Gagal mencatat pengeluaran.");
                return;
            }
            loadData();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", null, "Input jumlah tidak valid.");
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
                showAlert(Alert.AlertType.ERROR, "Error", null, "Gagal mengupdate pengeluaran.");
                return;
            }
            loadData();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", null, "Input jumlah tidak valid.");
        }
    }

    private void handleHapus(Pengeluaran pengeluaran) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus pengeluaran ini?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                manajemenAkun.hapusDataPengeluaran(pengeluaran.getId());
                loadData();
            }
        });
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        manajemenAkun.logoutUser();
        navigatorService.navigateTo("/moneyexpense/view/LoginView.fxml", tombolLogout);
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

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        if (header != null) alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void loadData() {
        daftarPengeluaran.setAll(manajemenAkun.getDaftarPengeluaranPengguna());
        tableViewPengeluaran.setItems(daftarPengeluaran);
    
        
    
        updateSummaryBox();
        clearForm();
    }
    
}
