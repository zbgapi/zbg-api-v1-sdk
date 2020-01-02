package com.dd.api.utils;

import com.dd.api.exceptions.EncryptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要算法工具类
 * <p>
 * created by zhangzp on 2018/8/17.
 *
 * @author zhangzp
 */
public class DigestKit {
    private static final Logger LOGGER = LoggerFactory.getLogger(DigestKit.class);
    /**
     * 编码，UTF-8
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    // ======================== 摘要算法 =========================

    public static final String ALGORITHM_MD5 = "MD5";

    public static final String ALGORITHM_SHA1 = "SHA-1";
    public static final String ALGORITHM_SHA256 = "SHA-256";
    public static final String ALGORITHM_SHA384 = "SHA-384";
    public static final String ALGORITHM_SHA512 = "SHA-512";

    /**
     * HmacMD5算法
     */
    public static final String ALGORITHM_HMAC_MD5 = "HmacMD5";
    public static final String ALGORITHM_HMAC_SHA1 = "HmacSHA1";
    public static final String ALGORITHM_HMAC_SHA256 = "HmacSHA256";
    public static final String ALGORITHM_HMAC_SHA384 = "HmacSHA384";
    public static final String ALGORITHM_HMAC_SHA512 = "HmacSHA512";

    private DigestKit() {
    }

    /**
     * sha1 加密
     *
     * @param message 源字符串
     * @return 加密字符串
     */
    public static String sha1(String message) {
        return StringKit.toHex(messageDigest(message, ALGORITHM_SHA1));
    }

    /**
     * sha256 加密
     *
     * @param message 源字符串
     * @return 加密字符串
     */
    public static String sha256(String message) {
        return StringKit.toHex(messageDigest(message, ALGORITHM_SHA256));
    }

    /**
     * sha384 加密
     *
     * @param message 源字符串
     * @return 加密字符串
     */
    public static String sha384(String message) {
        return StringKit.toHex(messageDigest(message, ALGORITHM_SHA384));
    }

    /**
     * sha512 加密
     *
     * @param message 源字符串
     * @return 加密字符串
     */
    public static String sha512(String message) {
        return StringKit.toHex(messageDigest(message, ALGORITHM_SHA512));
    }

    /**
     * md5加密
     *
     * @param message 源字符串
     * @return 加密字符串
     */
    public static String md5(String message) {
        return StringKit.toHex(messageDigest(message, ALGORITHM_MD5));
    }

    /**
     * 给字节数组生成摘要
     *
     * @param message   字符串
     * @param algorithm 摘要算法
     * @return 摘要字节数组
     */
    private static byte[] messageDigest(String message, String algorithm) {
        return messageDigest(message.getBytes(UTF_8), algorithm);
    }

    /**
     * 给字节数组生成摘要
     *
     * @param message   字节码数组
     * @param algorithm 摘要算法
     * @return 摘要字节数组
     */
    private static byte[] messageDigest(byte[] message, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(message);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
    }

    /**
     * sha1 加密
     *
     * @param file 文件对象
     * @return 加密字符串
     * @throws EncryptException 如果文件不存在
     */
    public static String sha1(File file) {
        return messageDigest(file, ALGORITHM_SHA1);
    }

    /**
     * sha256 加密
     *
     * @param file 文件对象
     * @return 加密字符串
     * @throws EncryptException 如果文件不存在
     */
    public static String sha256(File file) {
        return messageDigest(file, ALGORITHM_SHA256);
    }

    /**
     * sha512 加密
     *
     * @param file 文件对象
     * @return 加密字符串
     * @throws EncryptException 如果文件不存在
     */
    public static String sha512(File file) {
        return messageDigest(file, ALGORITHM_SHA512);
    }

    /**
     * md5加密
     *
     * @param file 文件对象
     * @return 加密字符串
     * @throws EncryptException 如果文件不存在
     */
    public static String md5(File file) {
        return messageDigest(file, ALGORITHM_MD5);
    }

    /**
     * 给文件生成摘要
     *
     * @param file      文件
     * @param algorithm 摘要算法
     * @return 摘要字符串，如果失败返回空字符
     * @throws EncryptException 如果文件不存在，或算法不存在
     */
    @SuppressWarnings("all")
    private static String messageDigest(File file, String algorithm) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }

        int bufferSize = 256 * 1024;
        try (FileInputStream fis = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, digest)) {
            byte[] buffer = new byte[bufferSize];

            while (dis.read(buffer) > 0) {
            }

            digest = dis.getMessageDigest();
            return StringKit.toHex(digest.digest());
        } catch (IOException e) {
            LOGGER.error("encrypt file error", e);
            throw new EncryptException(e);
        }
    }

    /**
     * 使用hmac-md5算法 生成摘要并加密
     *
     * @param message 加密信息
     * @param key     密钥
     * @return 16进制加密字符串
     * @throws EncryptException 如果密钥非法
     */
    public static String hmacMd5(String message, String key) {
        return StringKit.toHex(hmac(message, ALGORITHM_HMAC_MD5, key));
    }

    /**
     * 使用hmac-sha1算法 生成摘要并加密
     *
     * @param message 加密信息
     * @param key     密钥
     * @return 16进制加密字符串
     * @throws EncryptException 如果密钥非法
     */
    public static String hmacSha1(String message, String key) {
        return StringKit.toHex(hmac(message, ALGORITHM_HMAC_SHA1, key));
    }

    /**
     * 使用hmac-sha256算法 生成摘要并加密
     *
     * @param message 加密信息
     * @param key     密钥
     * @return 16进制加密字符串
     * @throws EncryptException 如果密钥非法
     */
    public static String hmacSha256(String message, String key) {
        return StringKit.toHex(hmac(message, ALGORITHM_HMAC_SHA256, key));
    }

    /**
     * 使用hmac-sha384算法 生成摘要并加密
     *
     * @param message 加密信息
     * @param key     密钥
     * @return 16进制加密字符串
     * @throws EncryptException 如果密钥非法
     */
    public static String hmacSha384(String message, String key) {
        return StringKit.toHex(hmac(message, ALGORITHM_HMAC_SHA384, key));
    }

    /**
     * 使用hmac-sha512算法 生成摘要并加密
     *
     * @param message 加密信息
     * @param key     密钥
     * @return 16进制加密字符串
     * @throws EncryptException 如果密钥非法
     */
    public static String hmacSha512(String message, String key) {
        return StringKit.toHex(hmac(message, ALGORITHM_HMAC_SHA512, key));
    }

    /**
     * hmac生成摘要并加密
     *
     * @param message   加密信息
     * @param algorithm 摘要算法
     * @param key       密钥
     * @return 加密字节码
     * @throws EncryptException 如果密钥非法，或算法不存在
     */
    public static byte[] hmac(String message, String algorithm, String key) {
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(key.getBytes(UTF_8), algorithm);
        try {
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(algorithm);
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);

            byte[] bytes = message.getBytes(UTF_8);
            //完成 Mac 操作
            return mac.doFinal(bytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new EncryptException(e);
        }
    }

    /**
     * 生成sign签名字符串，将字符串进行md加密，处理后返回一个8位字符串
     *
     * @param message 数据字符串
     * @return sign签名字符串
     */
    public static String sign(String message) {
        if (StringKit.isEmpty(message)) {
            return "";
        }

        String md5 = md5(message);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            String str = md5.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            builder.append(StringKit.CHARS[x % 0x3E]);
        }
        return builder.toString();
    }

    /**
     * 性能高，碰撞率低的hash算法
     *
     * @param key 待计算对象
     * @return hash值
     */
    public static long murmurHash(Object key) {
        ByteBuffer buf = ByteBuffer.wrap(key.toString().getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;
        buf.order(byteOrder);
        return h;
    }
}
