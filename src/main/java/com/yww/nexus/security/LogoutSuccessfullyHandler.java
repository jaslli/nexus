package com.yww.nexus.security;


import com.yww.nexus.utils.RedisUtils;
import com.yww.nexus.utils.ResponseUtil;
import com.yww.nexus.view.R;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;



/**
 * <p>
 *      退出登陆成功处理类
 * </p>
 *
 * @author   yww
 * @since  2023/12/5
 */
@Slf4j
@Component
public class LogoutSuccessfullyHandler implements LogoutSuccessHandler {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        String token = request.getHeader(TokenConstant.TOKEN_HEADER);
        String token = "";
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        log.info("用户退出登录成功， 用户的ID为： {}", redisUtils.get(token));
        log.info("用户的Token为： {}", token);
        redisUtils.deleteStr(token);
        ResponseUtil.response(response, R.ok("成功退出登录！"));
    }

}
