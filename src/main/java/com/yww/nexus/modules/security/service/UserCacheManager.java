package com.yww.nexus.modules.security.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.yww.nexus.security.AccountUser;
import com.yww.nexus.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 *      用户缓存信息
 * </p>
 *
 * @author yww
 * @since 2023/12/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCacheManager {

    private final RedisUtils redisUtils;

    @Value("${security.user-cache-time}")
    private long userCacheTime;

    public static final String CACHE_KEY = "user-login-cache:";

    /**
     * 清理用户缓存信息
     * 用户信息变更时
     *
     * @param userName 用户名
     */
    @Async
    public void cleanUserCache(String userName) {
        if (StrUtil.isNotBlank(userName)) {
            // 清除数据
            redisUtils.del(CACHE_KEY + userName);
        }
    }

    /**
     * 根据用户名获取用户缓存信息
     *
     * @param userName  用户名
     * @return          用户缓存信息
     */
    public Optional<AccountUser> getUserCache(String userName) {
        return Optional.ofNullable((AccountUser) redisUtils.get(CACHE_KEY + userName));
    }

    /**
     * 添加登录用户缓存
     *
     * @param userName      用户名
     * @param accountUser   登录用户信息
     */
    public void addUserCache(String userName, AccountUser accountUser) {
        // 添加数据, 避免数据同时过期
        long time = userCacheTime + RandomUtil.randomInt(900, 1800);
        redisUtils.set(CACHE_KEY + userName, accountUser, time);
    }

}
