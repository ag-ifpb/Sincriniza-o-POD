package io.github.joaomarccos.ads.pod;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaomarcos
 */
public class MD5Util {

    public static String generateHash(String data) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(data.getBytes(), 0, data.length());
            String hash = new BigInteger(1, m.digest()).toString(16);
            return hash;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5Util.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static String generateHash(List lista) {
        StringBuilder builder = new StringBuilder();
        for (Object obj : lista) {
            builder.append(obj.toString());
        }

        return generateHash(builder.toString());
    }
}
