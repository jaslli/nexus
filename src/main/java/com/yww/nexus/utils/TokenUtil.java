package com.yww.nexus.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.yww.nexus.constant.TokenConstant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serial;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *      Token工具类
 * 1. Header,记录令牌类型和签名算法
 * 2. payload,携带用户信息
 *  (1) iss(issuer), 签发者
 *  (2) sub(subject), 面向的主体
 *  (3) aud(audience), 接收方
 *  (4) nbf(notBefore), 开始生效生效时间戳
 *  (5) exp(expiresAt), 过期时间戳
 *  (6) iat(issuedAt ), 签发时间
 *  (7) jti(jwtId), 唯一标识
 * 3. signature, 签名，防止Token被篡改
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
public class TokenUtil {

    /**
     * 用户名
     */
    public static final String USER_NAME = "username";

    /**
     * 用户权限
     */
    public static final String AUTHORITY = "authority";

    /**
     * 生成Token
     * 当前使用HMAC512的加密算法
     *
     * @return Token
     */
    public static String createToken(String username, String authority) {
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
                put(USER_NAME, username);
                put(AUTHORITY, authority);
            }
        };
        // 过期时间三小时
        long now = DateUtil.current();
        long exp = now + 1000 * 3600 * 3;
        return JWT.create()
                // 设置header
                .withHeader(header)
                // 设置payload
                .withIssuer(TokenConstant.TOKEN_ISSUER)
                .withSubject(TokenConstant.TOKEN_SUBJECT)
                .withAudience(TokenConstant.TOKEN_AUDIENCE)
                .withNotBefore(new Date(now))
                .withExpiresAt(new Date(exp))
                .withIssuedAt(new Date(now))
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
    public static DecodedJWT parse(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(TokenConstant.TOKEN_SECRET)).build();
        return jwtVerifier.verify(token);
    }

    /**
     * 获取用户名
     *
     * @param decoded 解析后的Token
     * @return 用户名
     */
    public static String getUserName(DecodedJWT decoded) {
        return decoded.getClaim(USER_NAME).asString();
    }

    /**
     * 获取权限
     *
     * @param decoded Token
     * @return 权限
     */
    public static List<GrantedAuthority> getAuthority(DecodedJWT decoded) {
        String authority = decoded.getClaim(AUTHORITY).asString();
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }

}
