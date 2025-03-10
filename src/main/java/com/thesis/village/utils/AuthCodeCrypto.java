package com.thesis.village.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author yh
 */
public class AuthCodeCrypto {
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String ENCODING = "UTF-8";

    // 从配置中心获取（示例值需替换）
    private static final String AES_KEY = System.getenv("AES_KEY");
    private static final String AES_IV = System.getenv("AES_IV");

    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(AES_KEY.getBytes(ENCODING), "AES"),
                new IvParameterSpec(AES_IV.getBytes(ENCODING)));

        byte[] encrypted = cipher.doFinal(plainText.getBytes(ENCODING));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(AES_KEY.getBytes(ENCODING), "AES"),
                new IvParameterSpec(AES_IV.getBytes(ENCODING)));

        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        return new String(cipher.doFinal(decoded), ENCODING);
    }
}
