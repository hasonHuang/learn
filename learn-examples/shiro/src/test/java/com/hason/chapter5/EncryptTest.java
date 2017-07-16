package com.hason.chapter5;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;

import java.security.Key;

/**
 * 第五章 编码与加密
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/16
 */
public class EncryptTest {

    // hiro 提供了 base64 和 16 进制字符串编码/解码的 API 支持，方便一些编码解码操作
    @Test
    public void testEncode() {
        String str = "hason";   // 待加密字符串

        // Base64 编码/解码
        String base64 = Base64.encodeToString(str.getBytes());
        String str2 = Base64.decodeToString(base64);

        // 16进制字符串 编码/解码
        String hex = Hex.encodeToString(str.getBytes());
        String str3 = new String(Hex.decode(hex));
    }

    // Shiro 提供的散列算法
    @Test
    public void testHash() {
        String str = "hason";   // 待加密字符串
        String salt = "666";    // 盐

        // MD5
        String md5 = new Md5Hash(str, salt).toString();
        // SHA256
        String sha256 = new Sha256Hash(str, salt).toString();
        // 通用的散列支持，调用 SimpleHash 时指定散列算法，其内部使用了 Java 的 MessageDigest 实现
        String simpleHash = new SimpleHash("SHA-1", str, salt).toString();

        // 为了方便使用，Shiro 提供了 HashService，默认提供了 DefaultHashService 实现
        // 1、首先创建一个 DefaultHashService，默认使用 SHA-512 算法；
        // 2、可以通过 hashAlgorithmName 属性修改算法；
        // 3、可以通过 privateSalt 设置一个私盐，其在散列时自动与用户传入的公盐混合产生一个新盐；
        // 4、可以通过 generatePublicSalt 属性在用户没有传入公盐的情况下是否生成公盐；
        // 5、可以设置 randomNumberGenerator 用于生成公盐；
        // 6、可以设置 hashIterations 属性来修改默认加密迭代次数；
        // 7、需要构建一个 HashRequest，传入算法、数据、公盐、迭代次数。
        DefaultHashService hashService = new DefaultHashService();  // 默认 SHA-512 算法
        hashService.setHashAlgorithmName("SHA-512");                // 修改算法
        hashService.setPrivateSalt(new SimpleByteSource("123"));    // 私盐，默认无
        hashService.setGeneratePublicSalt(true);                    // 是否生成公盐，默认false
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator()); // 用于生成公盐，默认这个
        hashService.setHashIterations(1);                           // 生成 Hash 值得迭代次数

        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("MD5")
                .setSource(ByteSource.Util.bytes(str))
                .setSalt(ByteSource.Util.bytes(salt))
                .setIterations(1)
                .build();

        Hash hash = hashService.computeHash(request);
        System.out.println(hash.toHex());
    }

    // Shiro 还提供对称式加密/解密算法的支持，如 AES、Blowfish 等；
    // 当前还没有提供对非对称加密/解密算法支持，未来版本可能提供。
    @Test
    public void testEncrypt() {
        String str = "haosn";   // 待加密字符串

        // ## AES 算法 ##
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128);   // 设置 key 长度
        // 生成 key
        Key key = aesCipherService.generateNewKey();
        // 加密
        ByteSource encrypt = aesCipherService.encrypt(str.getBytes(), key.getEncoded());
        // 解密
        ByteSource decrypt = aesCipherService.decrypt(encrypt.getBytes(), key.getEncoded());
        String decryptText = new String(decrypt.getBytes());
        Assert.assertEquals(str, decryptText);
    }
}
