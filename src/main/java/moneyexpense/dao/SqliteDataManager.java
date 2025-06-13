package moneyexpense.dao;

import moneyexpense.models.Pengeluaran;
import moneyexpense.models.Pengguna;
import moneyexpense.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Kelas ini adalah implementasi konkret dari IDataManager untuk database SQLite.
 * Bertanggung jawab penuh untuk semua operasi Create, Read, Update, Delete (CRUD)
 * ke database.
 */
public class SqliteDataManager implements IDataManager {

    @Override
    public void simpanPengguna(Pengguna pengguna) throws SQLException { // Tambahkan throws
        String sql = "INSERT INTO pengguna(username, password) VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pengguna.getUsername());
            pstmt.setString(2, pengguna.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Jangan tangkap di sini, biarkan service yang menanganinya
            throw e; 
        }
    }

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

    @Override
    public boolean isUsernameExists(String username) {
        // Implementasi yang efisien dengan memanfaatkan metode yang sudah ada.
        return cariPenggunaByUsername(username).isPresent();
    }

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