package com.yww.nexus.modules.security.service;

import cn.hutool.core.date.DateUtil;
import com.yww.nexus.config.bean.SecurityProperties;
import com.yww.nexus.modules.security.view.OnlineUserDto;
import com.yww.nexus.modules.sys.entity.User;
import com.yww.nexus.modules.sys.service.impl.UserServiceImpl;
import com.yww.nexus.security.TokenProvider;
import com.yww.nexus.security.model.AccountUser;
import com.yww.nexus.utils.RedisUtils;
import com.yww.nexus.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *      在线用户信息服务
 * </p>
 *
 * @author yww
 * @since 2023/12/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineUserService {

    private final SecurityProperties properties;
    private final TokenProvider tokenProvider;
    private final RedisUtils redisUtils;
    private final UserServiceImpl userService;


    /**
     * 根据Token获取在线用户信息
     *
     * @param token     Token信息
     * @return          在线用户信息
     */
    public OnlineUserDto getOne(String token) {
        String loginKey = tokenProvider.loginKey(token);
        return (OnlineUserDto) redisUtils.get(loginKey);
    }

    /**
     * 保存在线用户信息
     * @param accountUser   登录用户信息
     * @param token         token
     * @param request       请求信息
     */
    public long save(AccountUser accountUser, String token, HttpServletRequest request){

        OnlineUserDto onlineUserDto = null;
        try {
            // 获取在线用户信息
            String ip = RequestUtils.getIpAddr(request);
            String browser = RequestUtils.getBrowser(request);
            onlineUserDto = OnlineUserDto.builder()
                    .userName(accountUser.getUsername())
                    .nickName(accountUser.getNickName())
                    .browser(browser)
                    .ip(ip)
                    .loginTime(new Date())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        String loginKey = tokenProvider.loginKey(token);
        log.info("--------------->{}", loginKey);
        long current = DateUtil.current() + properties.getAccessExpirationTime() * 1000;
        redisUtils.set(loginKey, onlineUserDto, properties.getAccessExpirationTime(), TimeUnit.MINUTES);
        return current;
    }

    /**
     * 根据用户名清除在线用户信息
     *
     * @param username 用户名
     */
    @Async
    public void kickOutForUsername(String username) {
        String loginKey = properties.getOnlineKey() + username + "*";
        redisUtils.scanDel(loginKey);
    }

    /**
     * 退出登录
     *
     * @param token Token
     */
    public void logout(String token) {
        String loginKey = tokenProvider.loginKey(token);
        redisUtils.del(loginKey);
    }

    /**
     * 根据用户名生成对应的AccountUser
     * 只返回username和nickname
     *
     * @param userName  用户名
     * @return          登录用户
     */
    public AccountUser getAccountUser(String userName) {
        Optional<User> optionalUser = userService.getByUsername(userName);
        User user = optionalUser.orElseThrow();
        return AccountUser.builder()
                .userName(user.getUsername())
                .nickName(user.getNickname())
                .build();
    }

}
