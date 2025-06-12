package moneyexpense.services;

import moneyexpense.dao.IDataManager;
import moneyexpense.dao.SqliteDataManager;
import moneyexpense.exceptions.UsernameAlreadyExistsException;
import moneyexpense.models.Pengeluaran;
import moneyexpense.models.Pengguna;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Kelas Service yang menangani semua logika bisnis untuk aplikasi.
 * Menjembatani antara Controller (UI Logic) dan DAO (Data Logic).
 */
public class ManajemenAkun {

    private final IDataManager dataManager;
    private final AuthService authService;

    // Constructor untuk inisialisasi
    public ManajemenAkun() {
        this.dataManager = new SqliteDataManager();
        this.authService = AuthService.getInstance();
    }


    // =======================================================================
    // == BAGIAN LOGIKA BISNIS UNTUK AKUN PENGGUNA (Registrasi & Login)
    // =======================================================================

    /**
     * Logika bisnis untuk meregistrasikan pengguna baru.
     * @param username Username dari form input.
     * @param plainPassword Password dari form input (BELUM DI-HASH).
     * @throws UsernameAlreadyExistsException jika username sudah terdaftar.
     * @throws SQLException untuk error database lainnya.
     */
    public void registrasiUser(String username, String plainPassword)
            throws UsernameAlreadyExistsException, SQLException {
        
        String hashedPassword = BcryptService.hashPassword(plainPassword);
        Pengguna penggunaBaru = new Pengguna(username, hashedPassword);

        try {
            dataManager.simpanPengguna(penggunaBaru);
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new UsernameAlreadyExistsException("Username '" + username + "' sudah terdaftar.");
            } else {
                throw e;
            }
        }
    }

    /**
     * Logika bisnis untuk login pengguna.
     * Jika berhasil, data pengguna akan disimpan di AuthService.
     * @param username Username dari form input.
     * @param plainPassword Password dari form input.
     * @return true jika login berhasil, false jika gagal.
     */
    public boolean loginUser(String username, String plainPassword) {
        Optional<Pengguna> penggunaOpt = dataManager.cariPenggunaByUsername(username);

        if (penggunaOpt.isPresent()) {
            Pengguna pengguna = penggunaOpt.get();
            if (BcryptService.verifyPassword(plainPassword, pengguna.getPassword())) {
                // MENJADI INI:
                authService.setUserAsLoggedIn(pengguna); // Simpan sesi pengguna
                return true;
            }
        }
        return false;
    }

    /**
     * Logika untuk logout.
     */
    public void logoutUser() {
        authService.logout();
    }


    // =======================================================================
    // == BAGIAN LOGIKA BISNIS UNTUK PENGELUARAN (CRUD & Ringkasan)
    // =======================================================================

    /**
     * Mencatat pengeluaran baru untuk pengguna yang sedang login.
     * @param tanggal Tanggal pengeluaran.
     * @param jumlah Nominal pengeluaran.
     * @param keterangan Deskripsi pengeluaran.
     * @return true jika berhasil disimpan, false jika gagal (misal: tidak ada yang login).
     */
    public boolean catatPengeluaranBaru(String tanggal, double jumlah, String keterangan) {
        Pengguna penggunaSaatIni = authService.getCurrentUser();
        if (penggunaSaatIni == null || jumlah < 0) {
            return false; // Tidak bisa mencatat jika tidak login atau jumlah tidak valid
        }
        Pengeluaran pengeluaranBaru = new Pengeluaran(
            penggunaSaatIni.getId(), tanggal, jumlah, keterangan
        );
        dataManager.simpanPengeluaran(pengeluaranBaru);
        return true;
    }

    /**
     * Mengambil daftar semua pengeluaran milik pengguna yang sedang login.
     * @return List pengeluaran, atau list kosong jika tidak ada.
     */
    public List<Pengeluaran> getDaftarPengeluaranPengguna() {
        Pengguna penggunaSaatIni = authService.getCurrentUser();
        if (penggunaSaatIni != null) {
            return dataManager.getAllPengeluaranByUserId(penggunaSaatIni.getId());
        }
        return new ArrayList<>(); // Kembalikan list kosong jika tidak ada yang login
    }

    /**
     * Memperbarui data pengeluaran yang sudah ada.
     * Termasuk pengecekan keamanan untuk memastikan pengguna hanya bisa mengubah datanya sendiri.
     * @param pengeluaranToUpdate Objek pengeluaran dengan data yang sudah diperbarui.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateDataPengeluaran(Pengeluaran pengeluaranToUpdate) {
        Pengguna penggunaSaatIni = authService.getCurrentUser();
        if (penggunaSaatIni == null) return false;

        // Pengecekan keamanan: Ambil data asli dari DB untuk memastikan pemiliknya sama
        Optional<Pengeluaran> pengeluaranAsliOpt = dataManager.getPengeluaranById(pengeluaranToUpdate.getId());
        if (pengeluaranAsliOpt.isPresent()) {
            Pengeluaran pengeluaranAsli = pengeluaranAsliOpt.get();
            if (pengeluaranAsli.getIdPengguna() == penggunaSaatIni.getId()) {
                // Jika pemiliknya cocok, lanjutkan update
                return dataManager.updatePengeluaran(pengeluaranToUpdate);
            }
        }
        return false; // Gagal jika data tidak ditemukan atau bukan pemiliknya
    }

    /**
     * Menghapus data pengeluaran berdasarkan ID.
     * Termasuk pengecekan keamanan untuk memastikan pengguna hanya bisa menghapus datanya sendiri.
     * @param idPengeluaran ID dari pengeluaran yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean hapusDataPengeluaran(int idPengeluaran) {
        Pengguna penggunaSaatIni = authService.getCurrentUser();
        if (penggunaSaatIni == null) return false;
        
        // Pengecekan keamanan: Ambil data dari DB untuk memastikan pemiliknya sama
        Optional<Pengeluaran> pengeluaranOpt = dataManager.getPengeluaranById(idPengeluaran);
        if (pengeluaranOpt.isPresent()) {
            Pengeluaran pengeluaran = pengeluaranOpt.get();
            if (pengeluaran.getIdPengguna() == penggunaSaatIni.getId()) {
                // Jika pemiliknya cocok, lanjutkan hapus
                return dataManager.hapusPengeluaran(idPengeluaran);
            }
        }
        return false; // Gagal jika data tidak ditemukan atau bukan pemiliknya
    }

    /**
     * Menghitung total pengeluaran untuk bulan dan tahun berjalan.
     * @return Total pengeluaran dalam format double.
     */
    public double getRingkasanTotalBulanIni() {
        Pengguna penggunaSaatIni = authService.getCurrentUser();
        if (penggunaSaatIni == null) return 0.0;
        
        LocalDate today = LocalDate.now();
        String bulan = today.format(DateTimeFormatter.ofPattern("MM")); // "01", "06", "12"
        String tahun = String.valueOf(today.getYear()); // "2025"
        
        return dataManager.getTotalPengeluaranBulanIni(penggunaSaatIni.getId(), bulan, tahun);
    }
}