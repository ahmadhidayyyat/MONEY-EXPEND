package moneyexpense.dao;

import moneyexpense.models.Pengguna;
import moneyexpense.models.Pengeluaran;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IDataManager {
    // --- Operasi Pengguna ---
    void simpanPengguna(Pengguna pengguna) throws SQLException;
    Optional<Pengguna> cariPenggunaByUsername(String username);
    boolean isUsernameExists(String username);

    // --- Operasi Pengeluaran ---
    void simpanPengeluaran(Pengeluaran pengeluaran);
    boolean updatePengeluaran(Pengeluaran pengeluaran);
    boolean hapusPengeluaran(int idPengeluaran);
    Optional<Pengeluaran> getPengeluaranById(int idPengeluaran);
    List<Pengeluaran> getAllPengeluaranByUserId(int idPengguna);
    
    // --- Operasi Agregasi ---
    double getTotalPengeluaranBulanIni(int idPengguna, String bulan, String tahun);
}