package com.yww.nexus.security;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yww.nexus.constant.TokenConstant;
import com.yww.nexus.service.IUserService;
import com.yww.nexus.utils.RedisUtils;
import com.yww.nexus.utils.TokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * <p>
 *      Token身份验证过滤器
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Slf4j
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    IUserService userService;
    @Resource
    RedisUtils redisUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 初步检测获取Token
        String token = resolveToken(request);
        if (StrUtil.isNotBlank(token)) {
            if (StrUtil.isBlank(redisUtils.getStr(request.getHeader(TokenConstant.TOKEN_HEADER)))) {
                // 验证并解析Token
                try {
                    DecodedJWT decoded = TokenUtil.parse(token);
                    // 根据Token获取用户名
                    String username = TokenUtil.getUserName(decoded);
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 填充SecurityContextHolder
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(username, null, TokenUtil.getAuthority(decoded));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (AlgorithmMismatchException | SignatureVerificationException | TokenExpiredException | MissingClaimException | IncorrectClaimException e) {
                    errorHandler(request, response, e);
                }
            }
            // 设置当前用户Token
            // ThreadLocalUtil.set(TokenConstant.ADMIN_TOKEN, token);
        }
        chain.doFilter(request, response);
    }

    /**
     * 检查请求头是否存在Token，是否以指定前缀开头
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TokenConstant.TOKEN_HEADER);
        // 判断Token是否为空
        if (StrUtil.isBlank(bearerToken)) {
            return null;
        }
        // 判断Token是否以指定前缀开头
        if (bearerToken.startsWith(TokenConstant.TOKEN_PREFIX)) {
            // 去掉令牌前缀
            return bearerToken.replace(TokenConstant.TOKEN_PREFIX, "");
        } else {
            log.warn("非法Token：{}", bearerToken);
        }
        return null;
    }


    /**
     * 处理异常信息
     *
     * @param request  请求
     * @param response 响应
     * @param e        异常
     */
    private void errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        if (e instanceof AlgorithmMismatchException) {
            resolver.resolveException(request, response, null, new AlgorithmMismatchException(e.getMessage()));
            return;
        }
        if (e instanceof TokenExpiredException) {
            resolver.resolveException(request, response, null, new TokenExpiredException(
                    e.getMessage(),
                    ((TokenExpiredException) e).getExpiredOn())
            );
            return;
        }
        if (e instanceof MissingClaimException) {
            resolver.resolveException(request, response, null, new MissingClaimException(
                    ((MissingClaimException) e).getClaimName())
            );
            return;
        }
        if (e instanceof IncorrectClaimException) {
            resolver.resolveException(request, response, null, new IncorrectClaimException(
                    e.getMessage(),
                    ((IncorrectClaimException) e).getClaimName(),
                    ((IncorrectClaimException) e).getClaimValue())
            );
            return;
        }
        if (e instanceof JWTVerificationException) {
            resolver.resolveException(request, response, null, new JWTVerificationException(
                    e.getMessage(),
                    e.getCause())
            );
        }
    }

}
