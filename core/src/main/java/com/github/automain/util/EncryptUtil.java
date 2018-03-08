package com.github.automain.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EncryptUtil {

    private static final String MD5_LABEL = "MD5";
    private static final String SHA1_LABEL = "SHA1";
    private static final String SHA256_LABEL = "SHA-256";
    private static final String AES_LABEL = "AES";
    private static final String RSA_LABEL = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * byte数组转为十六进制字符串
     *
     * @param arr
     * @return
     */
    public static String byteArrayToHexStr(byte[] arr) {
        if (arr != null) {
            StringBuilder sb = new StringBuilder();
            int length = arr.length;
            for (int i = 0; i < length; i++) {
                String hex = Integer.toHexString(arr[i] & 0xff);
                hex = hex.length() == 1 ? "0" + hex : hex;
                sb.append(hex);
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 十六进制字符串转为byte数组
     *
     * @param hexStr
     * @return
     * @throws Exception
     */
    public static byte[] hexStrToByteArray(String hexStr) throws Exception {
        return new BigInteger(hexStr, 16).toByteArray();
    }

    /**
     * MD5加密
     *
     * @param content
     * @return
     */
    public static String MD5(byte[] content) throws NoSuchAlgorithmException {
        return oneWayEncrypt(MD5_LABEL, content);
    }

    /**
     * MD5加密文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String MD5(File file) throws Exception {
        return oneWayEncrypt(MD5_LABEL, file);
    }

    /**
     * SHA1加密
     *
     * @param content
     * @return
     */
    public static String SHA1(byte[] content) throws NoSuchAlgorithmException {
        return oneWayEncrypt(SHA1_LABEL, content);
    }

    /**
     * SHA1加密文件
     *
     * @param file
     * @return
     */
    public static String SHA1(File file) throws Exception {
        return oneWayEncrypt(SHA1_LABEL, file);
    }

    /**
     * SHA256加密
     *
     * @param content
     * @return
     */
    public static String SHA256(byte[] content) throws NoSuchAlgorithmException {
        return oneWayEncrypt(SHA256_LABEL, content);
    }

    /**
     * SHA256加密文件
     *
     * @param file
     * @return
     */
    public static String SHA256(File file) throws Exception {
        return oneWayEncrypt(SHA256_LABEL, file);
    }

    private static String oneWayEncrypt(String type, byte[] content) throws NoSuchAlgorithmException {
        if (content != null) {
            try {
                MessageDigest digest = MessageDigest.getInstance(type);
                digest.update(content);
                return byteArrayToHexStr(digest.digest());
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }

    private static String oneWayEncrypt(String type, File file) throws Exception {
        if (SystemUtil.checkFileAvailable(file)) {
            try (FileInputStream in = new FileInputStream(file);
                 FileChannel ch = in.getChannel()) {
                MessageDigest digest = MessageDigest.getInstance(type);
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                digest.update(byteBuffer);
                return byteArrayToHexStr(digest.digest());
            }
        }
        return null;
    }

    /**
     * BASE64编码
     *
     * @param content
     * @return
     */
    public static byte[] BASE64Encrypt(byte[] content) {
        return Base64.getEncoder().encode(content);
    }

    /**
     * BASE64编码成字符串
     *
     * @param content
     * @return
     */
    public static String BASE64Encode(byte[] content) {
        return Base64.getEncoder().encodeToString(content);
    }

    /**
     * BASE64解码
     *
     * @param content
     * @return
     */
    public static byte[] BASE64Decrypt(byte[] content) {
        return Base64.getDecoder().decode(content);
    }

    /**
     * BASE64解码字符串
     *
     * @param content
     * @return
     */
    public static byte[] BASE64Decode(String content) {
        return Base64.getDecoder().decode(content);
    }

    /**
     * AES加密
     *
     * @param content
     * @param password
     * @return
     */
    public static String AESEncrypt(byte[] content, String password) throws Exception {
        return AES(content, password, Cipher.ENCRYPT_MODE);
    }

    /**
     * AES解密
     *
     * @param content
     * @param password
     * @return
     */
    public static String AESDecrypt(byte[] content, String password) throws Exception {
        return AES(content, password, Cipher.DECRYPT_MODE);
    }

    private static String AES(byte[] content, String password, int mode) throws Exception {
        if (password != null && content != null) {
            //构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance(AES_LABEL);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes(PropertiesUtil.DEFAULT_CHARSET));
            //根据规则初始化密钥生成器,生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, secureRandom);
            //产生原始对称密钥
            SecretKey originalKey = keygen.generateKey();
            //获得原始对称密钥的字节数组
            byte[] raw = originalKey.getEncoded();
            //根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, AES_LABEL);
            //根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES_LABEL);
            //初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(mode, key);
            //根据密码器的初始化方式--加密：将数据加密
            if (Cipher.DECRYPT_MODE == mode) {
                content = BASE64Decrypt(content);
            }
            byte[] result = cipher.doFinal(content);
            //将字符串返回
            if (Cipher.ENCRYPT_MODE == mode) {
                return BASE64Encode(result);
            } else {
                return new String(result, PropertiesUtil.DEFAULT_CHARSET);
            }
        }
        return null;
    }

    /**
     * 获取RSA私钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static PrivateKey getRSAPrivateKey(String key) throws Exception {
        byte[] keyBytes = BASE64Decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_LABEL);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取RSA公钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static PublicKey getRSAPublicKey(String key) throws Exception {
        byte[] keyBytes = BASE64Decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_LABEL);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA私钥签名
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String signByPrivateKey(byte[] data, String privateKey) throws Exception {
        PrivateKey priKey = getRSAPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(priKey);
        signature.update(data);
        return BASE64Encode(signature.sign());
    }

    /**
     * RSA公钥验证签名
     *
     * @param data
     * @param publicKey
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verifyByPublicKey(byte[] data, String publicKey, String sign) throws Exception {
        PublicKey pubKey = getRSAPublicKey(publicKey);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(BASE64Decode(sign));
    }

    /**
     * RSA分段加密/解密
     *
     * @param data
     * @param key
     * @return
     */
    private static byte[] doRSAByKey(byte[] data, Key key, int cipherMode, int maxBlock) throws Exception {
        int inputLen = data.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Cipher cipher = Cipher.getInstance(RSA_LABEL);
            cipher.init(cipherMode, key);
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxBlock) {
                    cache = cipher.doFinal(data, offSet, maxBlock);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxBlock;
            }
            return out.toByteArray();
        }
    }

    /**
     * RSA公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        return doRSAByKey(data, getRSAPublicKey(publicKey), Cipher.ENCRYPT_MODE, MAX_ENCRYPT_BLOCK);
    }

    /**
     * RSA私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        return doRSAByKey(data, getRSAPrivateKey(privateKey), Cipher.ENCRYPT_MODE, MAX_ENCRYPT_BLOCK);
    }

    /**
     * RSA公钥解密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String publicKey) throws Exception {
        return doRSAByKey(data, getRSAPublicKey(publicKey), Cipher.DECRYPT_MODE, MAX_DECRYPT_BLOCK);
    }

    /**
     * RSA私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        return doRSAByKey(data, getRSAPrivateKey(privateKey), Cipher.DECRYPT_MODE, MAX_DECRYPT_BLOCK);
    }

}
