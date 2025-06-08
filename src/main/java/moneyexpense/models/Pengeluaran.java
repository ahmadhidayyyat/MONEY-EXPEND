package moneyexpense.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import moneyexpense.utils.DatabaseConnection;

public class Pengeluaran {
    private int id;
    private int idPengguna;
    private String tanggal;
    private double jumlah;
    private String keterangan;

    public Pengeluaran(int idPengguna, String tanggal, double jumlah, String keterangan) {
        this.idPengguna = idPengguna;
        this.tanggal = tanggal;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getIdPengguna() { return idPengguna; }
    public String getTanggal() { return tanggal; }
    public double getJumlah() { return jumlah; }
    public String getKeterangan() { return keterangan; }

    public void setId(int id) { this.id = id; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    // Database operations
    public boolean simpan() {
        String sql = "INSERT INTO pengeluaran(id_pengguna, tanggal, jumlah, keterangan) VALUES(?,?,?,?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, this.idPengguna);
            pstmt.setString(2, this.tanggal);
            pstmt.setDouble(3, this.jumlah);
            pstmt.setString(4, this.keterangan);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating expense failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                    return true;
                } else {
                    throw new SQLException("Creating expense failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving expense: " + e.getMessage());
            return false;
        }
    }

    public static List<Pengeluaran> getPengeluaranByUser(int idPengguna) {
        String sql = "SELECT id, tanggal, jumlah, keterangan FROM pengeluaran WHERE id_pengguna = ? ORDER BY tanggal DESC";
        List<Pengeluaran> pengeluaranList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPengguna);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Pengeluaran p = new Pengeluaran(idPengguna, 
                    rs.getString("tanggal"), 
                    rs.getDouble("jumlah"), 
                    rs.getString("keterangan"));
                p.setId(rs.getInt("id"));
                pengeluaranList.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error getting expenses: " + e.getMessage());
        }
        
        return pengeluaranList;
    }

    public static boolean hapus(int idPengeluaran) {
        String sql = "DELETE FROM pengeluaran WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPengeluaran);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting expense: " + e.getMessage());
            return false;
        }
    }

    // 
    public boolean update() {
        String sql = "UPDATE pengeluaran SET tanggal = ?, jumlah = ?, keterangan = ? WHERE id = ? AND id_pengguna = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, this.tanggal);
            pstmt.setDouble(2, this.jumlah);
            pstmt.setString(3, this.keterangan);
            pstmt.setInt(4, this.id);
            pstmt.setInt(5, this.idPengguna);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating expense: " + e.getMessage());
            return false;
        }
    }

    // 
    public static Pengeluaran getById(int idPengeluaran, int idPengguna) {
        String sql = "SELECT id, id_pengguna, tanggal, jumlah, keterangan FROM pengeluaran WHERE id = ? AND id_pengguna = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPengeluaran);
            pstmt.setInt(2, idPengguna);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Pengeluaran p = new Pengeluaran(
                    rs.getInt("id_pengguna"),
                    rs.getString("tanggal"),
                    rs.getDouble("jumlah"),
                    rs.getString("keterangan"));
                p.setId(rs.getInt("id"));
                return p;
            }
        } catch (SQLException e) {
            System.err.println("Error getting expense by ID: " + e.getMessage());
        }
        return null;
    }
}