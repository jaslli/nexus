package com.yww.nexus.security;


import com.yww.nexus.utils.ResponseUtil;
import com.yww.nexus.view.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


/**
 * <p>
 *      登录失败的处理类
 * </p>
 *
 * @author   yww
 * @since  2023/12/5
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        ResponseUtil.response(response, R.failed(exception.getMessage(), 401, "登录失败"));
    }

}
