package Authentication;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Authentication {
    public static Boolean matchPassword (String inputPassword, String accountPassword) {
        String hashedPassword = hashPassword(inputPassword);
        return hashedPassword.equals(accountPassword);
    }

    public static String hashPassword(String password) {
        try {
            // called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            // digest the password into byte
            byte[] passDigest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger bi = new BigInteger(1, passDigest);
            // convert password into hex value
            return bi.toString(16);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}