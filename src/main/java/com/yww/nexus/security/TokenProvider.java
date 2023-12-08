package com.yww.nexus.security;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yww.nexus.config.bean.SecretKeyProperties;
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
     * 加密算法
     */
    private final Algorithm TOKEN_ALGORITHM = Algorithm.HMAC512(SecretKeyProperties.tokenSecret);

    /**
     * 生成Token
     * 当前使用HMAC512的加密算法
     *
     * @return Token
     */
    public String createToken(String username) {
        // 设置负载
        Map<String, Object> payload = new HashMap<>(1) {
            @Serial
            private static final long serialVersionUID = 1L;
            {
                put(TokenConstant.USER_NAME, username);
            }
        };
        return JWT.create()
                // 设置payload
                .withIssuer(TokenConstant.TOKEN_ISSUER)
                .withSubject(TokenConstant.TOKEN_SUBJECT)
                .withAudience(TokenConstant.TOKEN_AUDIENCE)
                .withIssuedAt(DateUtil.date())
                .withJWTId(IdUtil.fastSimpleUUID())
                .withPayload(payload)
                // 签名
                .sign(TOKEN_ALGORITHM);
    }

    /**
     * 创建RefreshToken，用于刷新获取授权Token
     *
     * @param userName  用户名
     * @return          RefreshToken
     */
    public String createRefreshToken(String userName) {
        String refreshToken = createToken(userName);
        String md5RefreshToken = DigestUtil.md5Hex(refreshToken);
        // redis保存refreshToken便于记录状态
        redisUtils.setStr(md5RefreshToken, userName, properties.getRefreshExpirationTime(), TimeUnit.MINUTES);
        return refreshToken;
    }

    /**
     * 校验RefreshToken
     * 如果验证成功，则删除缓存，所以只能有一次验证成功的机会
     *
     * @param refreshToken  刷新Token
     * @param userName      Token解析得到的用户名
     * @return              校验成功返回True
     */
    public Boolean checkRefreshToken(String refreshToken, String userName) {
        if (StrUtil.isBlank(refreshToken)) {
            return false;
        }

        String md5RefreshToken = DigestUtil.md5Hex(refreshToken);
        String checkName = redisUtils.getStr(md5RefreshToken);
        if (StrUtil.isBlank(checkName)) {
            return false;
        }

        if (!userName.equals(checkName)) {
            return false;
        }
        redisUtils.del(md5RefreshToken);
        return true;
    }

    /**
     * 获取用户名
     *
     * @param token Token
     * @return 用户名
     */
    public String getUserName(String token) {
        DecodedJWT decoded = JWT.require(TOKEN_ALGORITHM).build().verify(token);
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
     * 获取请求头上的AccessToken，校验前缀并去掉
     *
     * @param request   请求信息
     * @return          去掉前缀的AccessToken
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
     * 获取请求头上的RefreshToken
     *
     * @param request   请求信息
     * @return          RefreshToken
     */
    public String getRefreshToken(HttpServletRequest request) {
        return request.getHeader(properties.getHeader());
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

    public static void main(String[] args) {
        // 设置负载
        Map<String, Object> payload = new HashMap<>(1) {
            @Serial
            private static final long serialVersionUID = 1L;
            {
                put(TokenConstant.USER_NAME, "yww");
            }
        };
        String token = JWT.create()
                // 设置payload
                .withIssuer(TokenConstant.TOKEN_ISSUER)
                .withSubject(TokenConstant.TOKEN_SUBJECT)
                .withAudience(TokenConstant.TOKEN_AUDIENCE)
                .withIssuedAt(DateUtil.date())
                .withJWTId(IdUtil.fastSimpleUUID())
                .withPayload(payload)
                // 签名
                .sign(Algorithm.HMAC512(SecretKeyProperties.tokenSecret));

        System.out.println(token);
    }

}
