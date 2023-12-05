package com.yww.nexus.security;


import com.yww.nexus.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


/**
 * <p>
 *      登录成功处理器
 *      根据用户名(username)生成Token并返回
 * </p>
 *
 * @author   yww
 * @since  2023/12/5
 */
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private RedisUtils redisUtils;
//    private final IUserService userService;
//
//    @Autowired
//    public LoginSuccessHandler(IUserService userService) {
//        this.userService = userService;
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 获取用户名
        String username = authentication.getName();
//        // 获取用户权限
//        String authority = userService.getUserAuthorities(username);
//        // 生成Token
//        String token = TokenUtil.createToken(username, authority);
//        token = TokenConstant.TOKEN_PREFIX + token;
//        String userId = userService.getUserIdByUserName(username);
//        redisUtils.setStr(token, userId);
//        log.info("{}用户登录成功， 当前用户的ID为： {}", username, userId);
//        log.info("当前用户的Token为： {}", token);
//        ResponseUtil.response(response, Result.success(token));
    }

}
