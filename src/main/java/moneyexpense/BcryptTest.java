package moneyexpense;

import moneyexpense.services.BcryptService;

public class BcryptTest {
    
    public static void main(String[] args) {
        String plainPassword = "password123";
        
        // 1. Hash Password
        String hashedPassword = BcryptService.hashPassword(plainPassword);
        System.out.println("Hashed Password: " + hashedPassword);
        
        // 2. Verifikasi Password
        boolean isMatch = BcryptService.verifyPassword(plainPassword, hashedPassword);
        System.out.println("Password Match: " + isMatch);
    }
    
    
}