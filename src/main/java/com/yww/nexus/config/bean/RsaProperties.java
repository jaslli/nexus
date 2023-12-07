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
public class RsaProperties {

    public static String privateKey;

    @Value("${security.rsa.private-key}")
    public void setPrivateKey(String privateKey) {
        RsaProperties.privateKey = privateKey;
    }

}
