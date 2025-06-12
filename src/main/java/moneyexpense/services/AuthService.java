package moneyexpense.services;

import moneyexpense.models.Pengguna;

/**
 * Kelas ini bertanggung jawab untuk mengelola sesi pengguna yang sedang login.
 * Menggunakan pola Singleton untuk memastikan hanya ada satu instance dari
 * layanan ini di seluruh aplikasi, sehingga data pengguna yang login konsisten.
 */
public class AuthService {

    // Satu-satunya instance dari AuthService (Singleton)
    private static AuthService instance;
    
    // Variabel untuk menyimpan data pengguna yang sedang aktif/login
    private Pengguna currentUser;

    /**
     * Constructor dibuat private untuk mencegah pembuatan instance baru dari luar kelas.
     */
    private AuthService() {
        // Kosongkan, karena ini adalah bagian dari pola Singleton
    }

    /**
     * Metode static untuk mendapatkan satu-satunya instance dari kelas ini.
     * Jika instance belum ada, maka akan dibuat terlebih dahulu.
     *
     * @return instance dari AuthService.
     */
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Menyimpan objek Pengguna saat login berhasil.
     *
     * @param user Objek Pengguna yang telah diautentikasi.
     */
    public void login(Pengguna user) {
        this.currentUser = user;
    }

    /**
     * Menghapus data pengguna dari sesi saat logout.
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Mendapatkan objek Pengguna yang sedang login.
     *
     * @return Objek Pengguna yang sedang login, atau null jika tidak ada yang login.
     */
    public Pengguna getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Memeriksa apakah ada pengguna yang sedang login.
     *
     * @return true jika ada pengguna yang login, false jika tidak.
     */
    public boolean isLoggedIn() {
        return this.currentUser != null;
    }
}