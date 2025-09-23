package com.turnip.hutool;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.DigestUtil;
import java.nio.charset.StandardCharsets;

public class EncryptDemoFinal {

    public static void main(String[] args) {
        String content = "这是一段需要加密的测试文本";
        System.out.println("原始内容: " + content);

        // AES加密解密示例（修正乱码问题）
        aesDemoFixed(content);

        // RSA加密解密示例（无需修改，已正常）
        rsaDemo(content);
    }

    /**
     * 修正后的AES加密解密（显式指定模式和填充，避免乱码）
     */
    public static void aesDemoFixed(String content) {
        System.out.println("\n=== AES加密解密演示（修正版） ===");

        // 1. 显式生成AES 128位密钥（Hutool专门方法，更可靠）
        byte[] aesKey = SecureUtil.generateKey("AES").getEncoded(); // 密钥长度：128/192/256（需JCE支持256位）
        System.out.println("AES密钥(MD5): " + DigestUtil.md5Hex(aesKey));

        // 2. 显式指定AES模式和填充方式（ECB模式无需IV向量，适合简单场景；生产推荐CBC/GCM模式）
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, aesKey);

        // 3. 加密（Base64编码便于传输存储）
        String encryptStr = aes.encryptBase64(content, StandardCharsets.UTF_8);
        System.out.println("AES加密后(Base64): " + encryptStr);

        // 4. 解密（使用相同的模式/填充/密钥）
        String decryptStr = aes.decryptStr(encryptStr, StandardCharsets.UTF_8);
        System.out.println("AES解密后: " + decryptStr);
    }

    /**
     * RSA加密解密示例（已正常，无需修改）
     */
    public static void rsaDemo(String content) {
        System.out.println("\n=== RSA加密解密演示 ===");

        // 生成RSA密钥对（默认2048位，可指定长度如new RSA(4096)）
        RSA rsa = new RSA();
        String privateKey = rsa.getPrivateKeyBase64();
        String publicKey = rsa.getPublicKeyBase64();

        System.out.println("RSA私钥: " + privateKey);
        System.out.println("RSA公钥: " + publicKey);

        // 公钥加密 + 私钥解密（加密传输场景）
        byte[] encryptByPub = rsa.encrypt(content.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);
        String encryptStr = Base64.encode(encryptByPub);
        System.out.println("RSA公钥加密后(Base64): " + encryptStr);

        byte[] decryptByPri = rsa.decrypt(encryptByPub, KeyType.PrivateKey);
        String decryptStr = new String(decryptByPri, StandardCharsets.UTF_8);
        System.out.println("RSA私钥解密后: " + decryptStr);

        // 私钥加密 + 公钥解密（数字签名/身份验证场景）
        byte[] encryptByPri = rsa.encrypt(content.getBytes(StandardCharsets.UTF_8), KeyType.PrivateKey);
        String signStr = Base64.encode(encryptByPri);
        System.out.println("RSA私钥加密后(Base64): " + signStr);

        byte[] decryptByPub = rsa.decrypt(encryptByPri, KeyType.PublicKey);
        String verifyStr = new String(decryptByPub, StandardCharsets.UTF_8);
        System.out.println("RSA公钥解密后: " + verifyStr);
    }
}