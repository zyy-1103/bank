package com.bank.utils;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Random;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 * 所有加密信息全部加盐
 * 存在cookie中的id需要加盐
 */
public class SM3 {
    private static final String SALT = "E~ThisIs+MySALT__";
    private static final String ENCODING = "UTF-8";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    //获取随机地址
    public static String getUrl() {
        String s = encryptWithSalt(String.valueOf(new Random().nextDouble())).substring(10, 50);
        return encryptWithSalt(s).substring(5, 35);
    }

    public static String encryptWithSalt(String paramStr) {
        return encrypt(SALT + paramStr + SALT);
    }

    public static String encrypt(String paramStr) {
        String resultHexString = "";
        try {
            byte[] srcData = paramStr.getBytes(ENCODING);
            byte[] resultHash = hash(srcData);
            resultHexString = ByteUtils.toHexString(resultHash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultHexString;
    }

    public static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }
}