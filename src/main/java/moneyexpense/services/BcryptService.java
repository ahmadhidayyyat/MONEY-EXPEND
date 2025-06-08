package moneyexpense.services;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptService {
    // Method untuk menghasilkan hash password
    public static String hashPassword(String plainPassword) {
        // BCrypt.gensalt() menghasilkan salt acak (default log_rounds=10)
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    
    // Method untuk verifikasi password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
