package com.yww.nexus.security;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.yww.nexus.config.bean.SecurityProperties;
import com.yww.nexus.constant.TokenConstant;
import com.yww.nexus.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *      Token工具类
 *  1. Header,记录令牌类型和签名算法
 *  2. payload,携带用户信息
 *      (1) iss(issuer), 签发者
 *      (2) sub(subject), 面向的主体
 *      (3) aud(audience), 接收方
 *      (4) nbf(notBefore), 开始生效生效时间戳
 *      (5) exp(expiresAt), 过期时间戳
 *      (6) iat(issuedAt ), 签发时间
 *      (7) jti(jwtId), 唯一标识
 *  3. signature, 签名，防止Token被篡改
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final RedisUtils redisUtils;
    private final SecurityProperties properties;

    /**
     * 生成Token
     * 当前使用HMAC512的加密算法
     *
     * @return Token
     */
    public static String createToken(String username) {
        // 设置Token头部（不设置也会默认有这两个值）
        Map<String, Object> header = new HashMap<>(2) {
            @Serial
            private static final long serialVersionUID = 1L;

            {
                put("alg", TokenConstant.TOKEN_ALG);
                put("typ", TokenConstant.TOKEN_TYP);
            }
        };
        // 设置负载
        Map<String, Object> payload = new HashMap<>(1) {
            @Serial
            private static final long serialVersionUID = 1L;

            {
                put(TokenConstant.USER_NAME, username);
            }
        };
        return JWT.create()
                // 设置header
                .withHeader(header)
                // 设置payload
                .withIssuer(TokenConstant.TOKEN_ISSUER)
                .withSubject(TokenConstant.TOKEN_SUBJECT)
                .withAudience(TokenConstant.TOKEN_AUDIENCE)
                .withJWTId(IdUtil.fastSimpleUUID())
                .withPayload(payload)
                // 签名
                .sign(Algorithm.HMAC512(TokenConstant.TOKEN_SECRET));
    }

    /**
     * 解析Token
     * 当前使用HMAC512的加密算法
     *
     * @param token Token
     */
    public DecodedJWT parse(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(TokenConstant.TOKEN_SECRET)).build();
        return jwtVerifier.verify(token);
    }

    /**
     * 获取用户名
     *
     * @param token Token
     * @return 用户名
     */
    public String getUserName(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(TokenConstant.TOKEN_SECRET)).build();
        DecodedJWT decoded = jwtVerifier.verify(token);
        return decoded.getClaim(TokenConstant.USER_NAME).asString();
    }

    /**
     * 获取用户名
     *
     * @param decoded 解析后的Token
     * @return 用户名
     */
    public String getUserName(DecodedJWT decoded) {
        return decoded.getClaim(TokenConstant.USER_NAME).asString();
    }

    /**
     * 是否开启Token自动续期
     */
    public boolean isOpenCheck() {
        return properties.isDelayToken();
    }

    /**
     * token自动续期
     * @param token 需要检查的token
     */
    public void checkRenewal(String token) {
        String loginKey = loginKey(token);
        // 获取过期时间(秒)
        long time = redisUtils.getExpire(loginKey) * 1000;
        // 计算过期时间
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();

        // 如果在续期检查的范围内，则续期
        if (differ <= properties.getDetect()) {
            long renew = time + properties.getRenew();
            redisUtils.expire(loginKey, renew, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 检查请求头是否存在Token，是否以指定前缀开头
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(properties.getHeader());
        // 判断Token是否为空
        if (StrUtil.isBlank(bearerToken)) {
            return null;
        }
        // 判断Token是否以指定前缀开头
        if (!bearerToken.startsWith(properties.getTokenStartWith())) {
            log.warn("非法Token：{}", bearerToken);
            return null;
        }
        return bearerToken.replace(properties.getTokenStartWith(), "");
    }

    /**
     * 获取登录用户RedisKey
     *
     * @param token Token
     * @return key
     */
    public String loginKey(String token) {
        String userName = getUserName(token);
        String md5Token = DigestUtil.md5Hex(token);
        return properties.getOnlineKey() + userName + "-" + md5Token;
    }

}
