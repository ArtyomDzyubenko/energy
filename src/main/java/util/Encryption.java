//https://gist.github.com/stonegao/5299984

package util;

import org.apache.log4j.Logger;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class Encryption {
    private static final Logger logger = Logger.getLogger(Encryption.class);
    private static final byte[] SALT = { (byte) 0x11, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xD4, (byte) 0x9F, (byte) 0x4B, (byte) 0x73};
    private static final int ITERATION_COUNT = 8;
    private static final String ALGORITHM_NAME = "PBEWithMD5AndDES";
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String SLASH = "/";
    private static final String LOW_LINE = "_";
    private static final String PERCENT = "%";
    private static final String SPACE = "%25";
    private static final String NEW_STRING = "\n";
    private static final String NEW_LINE = "%0A";

    private Encryption(){}

    public static String encrypt(String input) {
        try {
            KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            AlgorithmParameterSpec parameterSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance(ALGORITHM_NAME).generateSecret(keySpec);

            Cipher eCipher = Cipher.getInstance(key.getAlgorithm());
            eCipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

            byte[] encrypted = eCipher.doFinal(input.getBytes(UTF_8));

            return new String(Base64.getEncoder().encode(encrypted), UTF_8).replace(PLUS, MINUS)
                    .replace(SLASH, LOW_LINE).replace(PERCENT, SPACE).replace(NEW_STRING, NEW_LINE);

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                NoSuchPaddingException | BadPaddingException |
                InvalidKeySpecException | IllegalBlockSizeException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    public static String decrypt(String input) {
        try {
            input = input.replace(NEW_LINE, NEW_STRING).replace(SPACE, PERCENT)
                    .replace(LOW_LINE, SLASH).replace(MINUS, PLUS);

            byte[] decrypted = Base64.getDecoder().decode(input.getBytes());

            KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            AlgorithmParameterSpec parameterSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance(ALGORITHM_NAME).generateSecret(keySpec);

            Cipher dCipher = Cipher.getInstance(key.getAlgorithm());
            dCipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            byte[] dec = dCipher.doFinal(decrypted);

            return new String(dec, UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                NoSuchPaddingException | BadPaddingException |
                InvalidKeySpecException | IllegalBlockSizeException | IllegalArgumentException e) {

            logger.error(e.getMessage());
        }

        return null;
    }
}
