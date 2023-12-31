package com.yww.nexus.modules.security.rest;

import com.yww.nexus.base.R;
import com.yww.nexus.modules.security.service.AuthorizationService;
import com.yww.nexus.modules.security.view.LoginReq;
import com.yww.nexus.modules.security.view.LoginVo;
import com.yww.nexus.modules.security.view.RefreshVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *      授权接口
 * </p>
 *
 * @author yww
 * @since 2023/12/6
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统授权接口")
@RequestMapping("/auth")
public class AuthorizationController {

    private final AuthorizationService service;

    @Operation(summary = "登录授权接口")
    @PostMapping(value = "/login")
    public R<LoginVo> login(@Validated @RequestBody LoginReq req, HttpServletRequest request) {
        return R.ok(service.login(req, request));
    }

    @Operation(summary = "注销登录")
    @DeleteMapping(value = "/logout")
    public R<?> logout(HttpServletRequest request) {
        service.logout(request);
        return R.ok();
    }

    @Operation(summary = "刷新Token")
    @GetMapping(value = "/refresh")
    public R<RefreshVo> refresh(HttpServletRequest request) {
        return R.ok(service.refresh(request));
    }

}
