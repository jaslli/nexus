/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.yww.nexus.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Jwt参数配置
 *
 * @author yww
 * @since  2023/12/6
 */
@Data
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class SecurityProperties {

    /**
     * 是否限制单用户登录
     */
    private Boolean singleLogin;

    /**
     * Authorization
     */
    private String header;

    /**
     * 令牌前缀
     */
    private String tokenStartWith;

    /**
     * AccessToken令牌过期时间, 单位秒
     */
    private Long accessExpirationTime;

    /**
     * RefreshToken令牌过期时间，单位秒
     */
    private Long refreshExpirationTime;

    /**
     * 在线用户 key，根据 key 查询 redis 中在线用户的数据
     */
    private String onlineKey;

    /**
     * 是否开启token的自动续期
     */
    private boolean delayToken;

    /**
     * token 续期检查
     */
    private Long detect;

    /**
     * 续期时间
     */
    private Long renew;

    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }

}
