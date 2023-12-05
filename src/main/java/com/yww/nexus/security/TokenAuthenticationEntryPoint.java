package com.yww.nexus.security;

import com.yww.nexus.utils.ResponseUtil;
import com.yww.nexus.view.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


/**
 * <p>
 *      未登录或登陆过期处理类
 * </p>
 *
 * @author   yww
 * @since  2023/12/5
 */
@Component
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        ResponseUtil.response(response, R.failed(403, "未进行登录", authException.getMessage()));
    }

}
