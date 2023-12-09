package com.yww.nexus.modules.security.service;

import cn.hutool.core.util.StrUtil;
import com.yww.nexus.config.bean.SecretKeyProperties;
import com.yww.nexus.config.bean.SecurityProperties;
import com.yww.nexus.exception.GlobalException;
import com.yww.nexus.modules.security.view.LoginReq;
import com.yww.nexus.modules.security.view.LoginVo;
import com.yww.nexus.modules.security.view.RefreshVo;
import com.yww.nexus.security.TokenProvider;
import com.yww.nexus.security.model.AccountUser;
import com.yww.nexus.utils.RequestUtils;
import com.yww.nexus.utils.RsaUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *      系统授权服务
 * </p>
 *
 * @author yww
 * @since 2023/12/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final OnlineUserService onlineUserService;
    private final SecurityProperties properties;
    private final TokenProvider tokenProvider;


    public LoginVo login(LoginReq req, HttpServletRequest request) {
        // 密码解密
        String password;
        try {
            password = RsaUtil.decryptByPrivateKey(SecretKeyProperties.privateKey, req.getPassword());
        } catch (Exception e) {
            throw new GlobalException("RSA解密失败！");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(req.getUsername(), password);
        // 使用认证管理器进行认证
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 将认证信息存储到安全上下文中
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 获取认证后的用户信息
        assert authentication != null;
        AccountUser accountUser = (AccountUser) authentication.getPrincipal();

        // 生成 AccessToken 和 refreshToken
        String userName = accountUser.getUsername();
        String accessToken = tokenProvider.createToken(userName);
        String refreshToken = tokenProvider.createRefreshToken(userName);

        // 是否限制单用户登录，即一个用户只能有一个客户端
        if (properties.getSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.kickOutForUsername(accountUser.getUsername());
        }
        // 保存在线用户
        onlineUserService.save(accountUser, accessToken, request);

        log.info("----------> {}用户登录成功", accountUser.getUsername());
        return LoginVo.builder()
                .username(accountUser.getUsername())
                .nickname(accountUser.getNickName())
                .roles(accountUser.getRoles())
                .permissions(accountUser.getPermissions())
                .accessToken(properties.getTokenStartWith() + accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public void logout(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        // 删除在线登录用户信息
        onlineUserService.logout(token);
        // TODO redis删除RefreshToken缓存
        log.info("----------> {}用户注销成功", tokenProvider.getUserName(token));
    }

    public RefreshVo refresh(HttpServletRequest request) {
        // 获取RefreshToken
        String refreshToken = tokenProvider.getRefreshToken(request);
        if (StrUtil.isBlank(refreshToken)) {
            log.warn("检测不到请求中的RefreshToken！-----> {}", RequestUtils.getIpAddr(request));
            throw new GlobalException("检测不到请求中的RefreshToken！");
        }
        // 解析RefreshToken
        String userName = tokenProvider.getUserName(refreshToken);
        if (StrUtil.isBlank(userName)) {
            log.warn("解析RefreshToken失败！-----> {}", RequestUtils.getIpAddr(request));
            throw new GlobalException("解析RefreshToken失败！");
        }
        // 验证RefreshToken，验证成功便删除缓存
        if (!tokenProvider.checkRefreshToken(refreshToken, userName)) {
            log.warn("RefreshToken验证失败！-----> {}", RequestUtils.getIpAddr(request));
            throw new GlobalException("RefreshToken验证失败！");
        }

        // 重新生成AccessToken和RefreshToken
        String accessToken = tokenProvider.createToken(userName);
        String newRefreshToken = tokenProvider.createRefreshToken(userName);
        return RefreshVo.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}
