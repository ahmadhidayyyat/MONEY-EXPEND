package moneyexpense.utils;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

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
                                      "FOREIGN KEY (id_pengguna) REFERENCES pengguna(id))";

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
            
            // Aktifkan foreign key support
            stmt.execute("PRAGMA foreign_keys = ON");
            
            // Buat tabel
            stmt.execute(createPenggunaTable);
            stmt.execute(createPengeluaranTable);
            
            System.out.println("Database initialized successfully");
            
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}