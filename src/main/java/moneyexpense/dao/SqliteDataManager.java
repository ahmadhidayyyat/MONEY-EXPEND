package moneyexpense.dao;

import moneyexpense.models.Pengeluaran;
import moneyexpense.models.Pengguna;
import moneyexpense.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * @param: namaParameter, deskripsi singkat tentang parameter tersebut
 * @return: Deskripsi tentang nilai yang dikembalikan
 */

/**
 * Kelas ini adalah implementasi konkret dari IDataManager untuk database SQLite.
 * Bertanggung jawab penuh untuk semua operasi Create, Read, Update, Delete (CRUD)
 * ke database.
 */
public class SqliteDataManager implements IDataManager {

    // =================================================================================
    // Implementasi Operasi untuk Model Pengguna
    // =avadocs
    /**
     * Menyimpan objek Pengguna baru ke dalam database.
     * Password yang disimpan di dalam objek Pengguna diasumsikan sudah di-hash.
     * @param pengguna Objek Pengguna yang akan disimpan.
     */
    @Override
    public void simpanPengguna(Pengguna pengguna) {
        String sql = "INSERT INTO pengguna(username, password) VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pengguna.getUsername());
            pstmt.setString(2, pengguna.getPassword()); // Ini harus HASH password
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Dalam aplikasi nyata, ini seharusnya dilempar sebagai custom exception
            // atau di-log menggunakan framework logging.
            System.err.println("Error saat menyimpan pengguna: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Mencari seorang pengguna berdasarkan username.
     * @param username Username yang akan dicari.
     * @return Optional yang berisi objek Pengguna jika ditemukan, atau Optional kosong jika tidak.
     */
    @Override
    public Optional<Pengguna> cariPenggunaByUsername(String username) {
        String sql = "SELECT id, username, password FROM pengguna WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Pengguna pengguna = new Pengguna();
                pengguna.setId(rs.getInt("id"));
                pengguna.setUsername(rs.getString("username"));
                pengguna.setPassword(rs.getString("password"));
                return Optional.of(pengguna);
            }

        } catch (SQLException e) {
            System.err.println("Error saat mencari pengguna: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Memeriksa apakah sebuah username sudah terdaftar di database.
     * @param username Username yang akan diperiksa.
     * @return true jika username sudah ada, false jika belum.
     */
    @Override
    public boolean isUsernameExists(String username) {
        // Implementasi yang efisien dengan memanfaatkan metode yang sudah ada.
        return cariPenggunaByUsername(username).isPresent();
    }


    // =================================================================================
    // Implementasi Operasi untuk Model Pengeluaran
    // =================================================================================

    /**
     * Menyimpan objek Pengeluaran baru ke dalam database.
     * Metode ini juga akan mengambil ID yang dihasilkan oleh database dan mengaturnya
     * kembali ke objek Pengeluaran yang diberikan.
     * @param pengeluaran Objek Pengeluaran yang akan disimpan.
     */
    @Override
    public void simpanPengeluaran(Pengeluaran pengeluaran) {
        String sql = "INSERT INTO pengeluaran(id_pengguna, tanggal, jumlah, keterangan) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, pengeluaran.getIdPengguna());
            pstmt.setString(2, pengeluaran.getTanggal());
            pstmt.setDouble(3, pengeluaran.getJumlah());
            pstmt.setString(4, pengeluaran.getKeterangan());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Set ID yang didapat dari DB ke objek
                        pengeluaran.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat menyimpan pengeluaran: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Memperbarui data pengeluaran yang sudah ada di database.
     * @param pengeluaran Objek Pengeluaran dengan data terbaru. ID dari objek ini digunakan untuk mencari record yang akan diupdate.
     * @return true jika pembaruan berhasil, false jika gagal.
     */
    @Override
    public boolean updatePengeluaran(Pengeluaran pengeluaran) {
        String sql = "UPDATE pengeluaran SET tanggal = ?, jumlah = ?, keterangan = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pengeluaran.getTanggal());
            pstmt.setDouble(2, pengeluaran.getJumlah());
            pstmt.setString(3, pengeluaran.getKeterangan());
            pstmt.setInt(4, pengeluaran.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error saat memperbarui pengeluaran: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus sebuah data pengeluaran dari database berdasarkan ID-nya.
     * @param idPengeluaran ID dari pengeluaran yang akan dihapus.
     * @return true jika penghapusan berhasil, false jika gagal.
     */
    @Override
    public boolean hapusPengeluaran(int idPengeluaran) {
        String sql = "DELETE FROM pengeluaran WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPengeluaran);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error saat menghapus pengeluaran: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil satu data pengeluaran berdasarkan ID-nya.
     * @param idPengeluaran ID dari pengeluaran yang dicari.
     * @return Optional yang berisi objek Pengeluaran jika ditemukan, atau Optional kosong jika tidak.
     */
    @Override
    public Optional<Pengeluaran> getPengeluaranById(int idPengeluaran) {
        String sql = "SELECT id, id_pengguna, tanggal, jumlah, keterangan FROM pengeluaran WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPengeluaran);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Pengeluaran p = new Pengeluaran(
                        rs.getInt("id_pengguna"),
                        rs.getString("tanggal"),
                        rs.getDouble("jumlah"),
                        rs.getString("keterangan")
                );
                p.setId(rs.getInt("id"));
                return Optional.of(p);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil pengeluaran by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Mengambil semua data pengeluaran milik seorang pengguna.
     * @param idPengguna ID dari pengguna yang pengeluarannya akan ditampilkan.
     * @return List yang berisi semua objek Pengeluaran, atau list kosong jika tidak ada.
     */
    @Override
    public List<Pengeluaran> getAllPengeluaranByUserId(int idPengguna) {
        String sql = "SELECT id, tanggal, jumlah, keterangan FROM pengeluaran WHERE id_pengguna = ? ORDER BY tanggal DESC";
        List<Pengeluaran> daftarPengeluaran = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPengguna);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Pengeluaran p = new Pengeluaran(
                        idPengguna,
                        rs.getString("tanggal"),
                        rs.getDouble("jumlah"),
                        rs.getString("keterangan")
                );
                p.setId(rs.getInt("id"));
                daftarPengeluaran.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua pengeluaran: " + e.getMessage());
            e.printStackTrace();
        }
        return daftarPengeluaran;
    }


    // =================================================================================
    // Implementasi Operasi Agregasi
    // =================================================================================

    /**
     * Menghitung total jumlah pengeluaran untuk pengguna tertentu pada bulan dan tahun tertentu.
     * @param idPengguna ID pengguna.
     * @param bulan String bulan dalam format 2 digit (misal: "01" untuk Januari, "12" untuk Desember).
     * @param tahun String tahun dalam format 4 digit (misal: "2025").
     * @return Total jumlah pengeluaran dalam bentuk double.
     */
    @Override
    public double getTotalPengeluaranBulanIni(int idPengguna, String bulan, String tahun) {
        String sql = "SELECT SUM(jumlah) AS total FROM pengeluaran WHERE id_pengguna = ? AND strftime('%m', tanggal) = ? AND strftime('%Y', tanggal) = ?";
        double total = 0.0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPengguna);
            pstmt.setString(2, bulan);
            pstmt.setString(3, tahun);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (SQLException e) {
            System.err.println("Error saat menghitung total pengeluaran: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }
}