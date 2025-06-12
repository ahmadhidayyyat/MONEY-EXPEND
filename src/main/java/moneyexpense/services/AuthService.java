package moneyexpense.services;

import moneyexpense.dao.IDataManager;
import moneyexpense.dao.SqliteDataManager;
import moneyexpense.models.Pengguna;
import java.sql.SQLException;
import java.util.Optional;

public class AuthService {

    private static AuthService instance;
    private Pengguna currentUser;
    private IDataManager dataManager;

    private AuthService() {
        this.dataManager = new SqliteDataManager();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    // Metode ini untuk OTENTIKASI dari halaman LOGIN
    public boolean login(String username, String password) throws SQLException {
        Optional<Pengguna> optionalUser = dataManager.cariPenggunaByUsername(username);

        if (optionalUser.isEmpty()) {
            return false;
        }

        Pengguna userFromDb = optionalUser.get();
        
        // Ganti ini dengan BcryptService jika sudah diimplementasikan
        boolean passwordMatch = password.equals(userFromDb.getPassword());

        if (passwordMatch) {
            // Panggil metode baru untuk mengatur sesi
            setUserAsLoggedIn(userFromDb); 
            return true;
        }
        return false;
    }

    // --- METODE BARU ---
    // Metode ini untuk MENGATUR SESI setelah objek Pengguna sudah didapat
    // (misalnya setelah registrasi atau login sukses).
    public void setUserAsLoggedIn(Pengguna user) {
        this.currentUser = user;
        System.out.println("Sesi telah diatur untuk pengguna: " + user.getUsername());
    }


    public void logout() {
        this.currentUser = null;
    }

    public Pengguna getCurrentUser() {
        return this.currentUser;
    }

    public boolean isLoggedIn() {
        return this.currentUser != null;
    }
}