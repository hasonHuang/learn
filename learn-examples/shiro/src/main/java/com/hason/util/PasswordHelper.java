package com.hason.util;

import com.hason.entity.User;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.security.SecureRandom;

/**
 * 密码工具, 基于 Shiro SimpleHash 实现加密，其内部使用了 Java MessageDigest 实现。
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/26
 */
public class PasswordHelper {

    /** 加密算法，默认 md5 */
    private String algorithmName = "md5";

    /** 加密算法的迭代次数, 表示在加密结果基础上再次加密的次数 */
    private int hashIterations = 1;

    /** 随机数生成器，生成盐 */
    private SecureRandom random = new SecureRandom();
    /** 随机数生成器每次生成的字节数组大小，16 bytes == 128 bit */
    private int byteSize = 16;

    /**
     * 加密用户的密码
     */
    public void encrypt(User user) {
        // 设置盐
        user.setSalt(nextBytes(byteSize));
        // new 一个加密算法
        SimpleHash simpleHash = new SimpleHash(
                algorithmName,
                user.getPassword(),
                user.getSalt(),
                hashIterations);
        user.setPassword(simpleHash.toHex());
    }

    /**
     * 获取一个位数长度为 8 * size 的随机数
     *
     * @param size 长度
     * @return 随机数，十六进制形式
     */
    private String nextBytes(int size) {
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return Hex.encodeToString(bytes);
    }
}
