package moneyexpense;

import java.util.List;
import java.sql.SQLException;

import moneyexpense.models.Pengeluaran;
import moneyexpense.models.Pengguna;
import moneyexpense.services.AuthService;
import moneyexpense.utils.DatabaseConnection;

public class MainExample {
    public static AuthService authService = AuthService.getInstance();

    public static void main(String[] args) {
        // Initialize database dengan pengecekan error
        try {
            DatabaseConnection.initializeDatabase();
            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            // Example user registration
            if (!Pengguna.isUsernameExists("testuser")) {
                Pengguna newUser = new Pengguna("testuser", "password123");
                if (newUser.simpan()) {
                    System.out.println("User registered successfully!");
                } else {
                    throw new SQLException("Failed to register user");
                }
            }

            // Example login
            Pengguna loggedInUser = Pengguna.login("testuser", "password123");
            System.out.println("Logged in as: " + loggedInUser.getUsername());

            authService.login(loggedInUser);

            // Example expense creation
            Pengeluaran expense = new Pengeluaran(
                loggedInUser.getId(), 
                "2023-11-15", 
                150000.0, 
                "Belanja bulanan");
            
            if (expense.simpan()) {
                System.out.println("Expense recorded successfully!");
                System.out.println("Expense ID: " + expense.getId());
            } else {
                throw new SQLException("Failed to record expense");
            }

            // Demo operasi update
            demoUpdatePengeluaran(loggedInUser);

            // Get all expenses for user
            tampilkanDaftarPengeluaran(loggedInUser);

        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demoUpdatePengeluaran(Pengguna pengguna) throws SQLException {
        System.out.println("\n=== DEMO OPERASI UPDATE ===");
        
        // Ambil daftar pengeluaran terlebih dahulu
        List<Pengeluaran> expenses = Pengeluaran.getPengeluaranByUser(pengguna.getId());
        
        if (expenses.isEmpty()) {
            System.out.println("Tidak ada data pengeluaran untuk diupdate");
            return;
        }

        // Ambil pengeluaran pertama untuk diupdate
        Pengeluaran expenseToUpdate = expenses.get(0);
        System.out.println("Data sebelum update:");
        tampilkanDetailPengeluaran(expenseToUpdate);

        // Lakukan update
        System.out.println("\nMemperbarui data...");
        expenseToUpdate.setTanggal("2023-11-16");
        expenseToUpdate.setJumlah(175000.0);
        expenseToUpdate.setKeterangan("Belanja bulanan (termasuk kebutuhan tambahan)");

        if (expenseToUpdate.update()) {
            System.out.println("Update berhasil!");
            
            // Verifikasi data setelah update
            Pengeluaran updatedExpense = Pengeluaran.getById(expenseToUpdate.getId(), pengguna.getId());
            System.out.println("Data setelah update:");
            tampilkanDetailPengeluaran(updatedExpense);
        } else {
            System.out.println("Update gagal!");
        }
    }

    private static void tampilkanDaftarPengeluaran(Pengguna pengguna) {
        System.out.println("\n=== DAFTAR PENGELUARAN TERKINI ===");
        List<Pengeluaran> expenses = Pengeluaran.getPengeluaranByUser(pengguna.getId());
        
        if (expenses.isEmpty()) {
            System.out.println("Belum ada catatan pengeluaran");
        } else {
            for (Pengeluaran p : expenses) {
                tampilkanDetailPengeluaran(p);
            }
        }
    }

    private static void tampilkanDetailPengeluaran(Pengeluaran p) {
        System.out.printf("ID: %d | %s | Rp%,.2f | %s\n", 
            p.getId(), p.getTanggal(), p.getJumlah(), p.getKeterangan());
    }
}