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
 * Kelas Service yang menangani semua logika bisnis/transaksi untuk aplikasi.
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

    public void registrasiUser(String username, String plainPassword)
            throws UsernameAlreadyExistsException, SQLException {
        
        String hashedPassword = BcryptService.hashPassword(plainPassword);
        Pengguna penggunaBaru = new Pengguna(username, hashedPassword);

        try {
            // Metode ini sekarang melempar SQLException jika gagal
            dataManager.simpanPengguna(penggunaBaru); 
        } catch (SQLException e) {
            // SQLite error code for UNIQUE constraint is 19
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed")) {
                throw new UsernameAlreadyExistsException("Username '" + username + "' sudah terdaftar.");
            } else {
                // Untuk error database lain, lemparkan kembali agar bisa ditangani di level atas
                throw e;
            }
        }
    }

    public boolean isUsernameExists(String username) {
        return dataManager.isUsernameExists(username);
    }

    public boolean loginUser(String username, String plainPassword) {
        Optional<Pengguna> penggunaOpt = dataManager.cariPenggunaByUsername(username);

        if (penggunaOpt.isPresent()) {
            Pengguna pengguna = penggunaOpt.get();
            if (BcryptService.verifyPassword(plainPassword, pengguna.getPassword())) {
                // Gunakan AuthService untuk mengatur sesi
                authService.setUserAsLoggedIn(pengguna); 
                return true;
            }
        }
        return false;
    }

    public void logoutUser() {
        authService.logout();
    }

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

    public List<Pengeluaran> getDaftarPengeluaranPengguna() {
        Pengguna penggunaSaatIni = authService.getCurrentUser();
        if (penggunaSaatIni != null) {
            return dataManager.getAllPengeluaranByUserId(penggunaSaatIni.getId());
        }
        return new ArrayList<>(); // Kembalikan list kosong jika tidak ada yang login
    }

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

    public double getRingkasanTotalBulanIni() {
        Pengguna penggunaSaatIni = authService.getCurrentUser();
        if (penggunaSaatIni == null) return 0.0;
        
        LocalDate today = LocalDate.now();
        String bulan = today.format(DateTimeFormatter.ofPattern("MM")); // "01", "06", "12"
        String tahun = String.valueOf(today.getYear()); // "2025"
        
        return dataManager.getTotalPengeluaranBulanIni(penggunaSaatIni.getId(), bulan, tahun);
    }
}