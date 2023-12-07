package com.yww.nexus.security;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.*;
import com.yww.nexus.modules.security.service.OnlineUserService;
import com.yww.nexus.modules.security.service.UserCacheManager;
import com.yww.nexus.modules.security.view.OnlineUserDto;
import com.yww.nexus.modules.sys.service.IUserService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

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
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    OnlineUserService onlineUserService;
    @Autowired
    UserCacheManager userCacheManager;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 初步解析Token，去除前缀
        String token = tokenProvider.resolveToken(request);
        // Token为空的话直接放行
        if (StrUtil.isBlank(token)) {
            chain.doFilter(request, response);
            return;
        }

        // 检测获取解析Token，获取用户名
        String username = "";
        try {
            username = tokenProvider.getUserName(token);
        } catch (AlgorithmMismatchException | SignatureVerificationException | TokenExpiredException | MissingClaimException | IncorrectClaimException e) {
            errorHandler(request, response, e);
        }
        // 如果解析Token出错，直接放行
        if (StrUtil.isBlank(username)) {
            chain.doFilter(request, response);
            return;
        }

        // 获取缓存当中的在线用户信息
        OnlineUserDto onlineUserDto = null;
        boolean cleanUserCache = false;
        try {
            String loginKey = tokenProvider.loginKey(token);
            onlineUserDto = onlineUserService.getOne(loginKey);
        } catch (Exception e) {
            log.error(e.getMessage());
            cleanUserCache = true;
        } finally {
            // 如果此时获取不到在线用户信息，则删除用户缓存信息
            if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                userCacheManager.cleanUserCache(username);
            }
        }

        // 没有在线用户信息代表token已经失效
        if (onlineUserDto != null) {
            // 填充SecurityContextHolder
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            userDetails,
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 判断Token是否自动续期
            if (tokenProvider.isOpenCheck()) {
                tokenProvider.checkRenewal(token);
            }
        }

        chain.doFilter(request, response);
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
