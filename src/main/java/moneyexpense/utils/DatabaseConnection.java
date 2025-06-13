package moneyexpense.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Kelas utilitas untuk mengelola koneksi ke database SQLite.
 * Database disimpan di direktori home pengguna untuk portabilitas.
 */
public class DatabaseConnection {
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String APP_DIR_PATH = USER_HOME + File.separator + ".moneyexpense";
    private static final String DB_PATH = APP_DIR_PATH + File.separator + "database.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() throws SQLException {
        // Pastikan direktori untuk database sudah ada.
        // `mkdirs()` akan membuat folder jika belum ada.
        File appDir = new File(APP_DIR_PATH);
        if (!appDir.exists()) {
            System.out.println("Membuat direktori aplikasi di: " + APP_DIR_PATH);
            appDir.mkdirs();
        }
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Menginisialisasi database dengan membuat tabel yang diperlukan jika belum ada.
     * Metode ini harus dipanggil sekali saat aplikasi pertama kali dimulai.
     */
    public static void initializeDatabase() {
        String createPenggunaTable = "CREATE TABLE IF NOT EXISTS pengguna (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "username TEXT UNIQUE NOT NULL, " +
                                    "password TEXT NOT NULL)";
                                    
        String createPengeluaranTable = "CREATE TABLE IF NOT EXISTS pengeluaran (" +
                                      "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                      "id_pengguna INTEGER NOT NULL, " +
                                      "tanggal TEXT NOT NULL, " +
                                      "jumlah REAL NOT NULL, " +
                                      "keterangan TEXT, " +
                                      "FOREIGN KEY (id_pengguna) REFERENCES pengguna(id) ON DELETE CASCADE)"; // Tambahkan ON DELETE CASCADE

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
            
            // Aktifkan dukungan foreign key di SQLite
            stmt.execute("PRAGMA foreign_keys = ON");
            
            // Buat tabel
            stmt.execute(createPenggunaTable);
            stmt.execute(createPengeluaranTable);
            
            System.out.println("Database berhasil diinisialisasi di path: " + DB_PATH);
            
        } catch (SQLException e) {
            // Jika inisialisasi database gagal, ini adalah error fatal.
            // Aplikasi tidak mungkin berjalan dengan benar.
            System.err.println("Error fatal saat inisialisasi database: " + e.getMessage());
            e.printStackTrace();
            // Pertimbangkan untuk keluar dari aplikasi jika DB gagal dibuat.
            // System.exit(1); 
        }
    }
}