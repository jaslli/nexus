package com.yww.nexus.constant;

/**
 * <p>
 *      Token相关常量类
 * </p>
 *
 * @author  yww
 * @since 2023/12/5
 */
public class TokenConstant {

    /**
     * 用户名
     */
    public static final String USER_NAME = "userName";

    /**
     * Token的密钥
     */
    public static final String TOKEN_SECRET = "ywwww";

    /**
     * header的头部加密算法声明
     */
    public static final String TOKEN_ALG = "HMAC512";

    /**
     * header的头部Token类型
     */
    public static final String TOKEN_TYP = "JWT";

    /**
     * Token的签发者
     */
    public static final String TOKEN_ISSUER = "yww";

    /**
     * Token面向的主体
     */
    public static final String TOKEN_SUBJECT = "nexus";

    /**
     * Token的接收方
     */
    public static final String TOKEN_AUDIENCE = "vue-nexus";

}
