package com.yww.nexus.security;

import com.yww.nexus.base.R;
import com.yww.nexus.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * <p>
 *      无权限或权限不足处理类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Component
public class AccessFailureHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        ResponseUtil.response(response, R.failed( 403, "没有权限执行", accessDeniedException.getMessage()));
    }

}
