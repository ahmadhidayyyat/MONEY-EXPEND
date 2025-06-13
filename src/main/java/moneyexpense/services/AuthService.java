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

