package com.yww.nexus.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.yww.nexus.constant.Constants;
import com.yww.nexus.exception.GlobalException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 *      登陆用户工具类
 * </P>
 *
 * @author yww
 * @since 2023/12/9
 */
public class SecurityUtil {

    /**
     * 获取当前登录的用户
     *
     * @return UserDetails
     */
    public static UserDetails getCurrentUser() {
        String username = getCurrentUsername();
        // 匿名用户
        if (username == null) {
            return null;
        }
        UserDetailsService userDetailsService = SpringUtil.getBean(UserDetailsService.class);
        return userDetailsService.loadUserByUsername(username);
    }

    /**
     * 获取当前登陆用户的用户名
     *
     * @return 用户名
     */
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new GlobalException(503, "当前登录状态过期");
        }
        // 判断是否是匿名用户
        if (Constants.ANONYMOUS_USER.equals(authentication.getPrincipal())) {
            return null;
        }
        // 获取用户名
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        throw new GlobalException(503, "当前登录状态异常");
    }

}
