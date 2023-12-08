package com.yww.nexus.config.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>
 *      RSA配置获取类
 * </p>
 *
 * @author yww
 * @since 2023/12/6
 */
@Data
@Component
public class SecretKeyProperties {

    /**
     * Token参与签名的密钥
     */
    public static String tokenSecret;

    /**
     * 登录密码需要RSA加密的公钥和密钥
     */
    public static String privateKey;
    public static String publicKey;

    @Value("${security.secret-key.token-secret}")
    public void setTokenSecret(String tokenSecret) {
        SecretKeyProperties.tokenSecret = tokenSecret;
    }

    @Value("${security.secret-key.private-key}")
    public void setPrivateKey(String privateKey) {
        SecretKeyProperties.privateKey = privateKey;
    }

    @Value("${security.secret-key.public-key}")
    public void setPublicKey(String publicKey) {
        SecretKeyProperties.publicKey = publicKey;
    }

}
