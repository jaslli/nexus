package com.yww.nexus.moudles.security.rest;

import com.yww.nexus.config.bean.RsaProperties;
import com.yww.nexus.moudles.security.view.LoginReq;
import com.yww.nexus.moudles.security.view.LoginVO;
import com.yww.nexus.security.AccountUser;
import com.yww.nexus.security.TokenProvider;
import com.yww.nexus.utils.RsaUtil;
import com.yww.nexus.view.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *      授权接口
 * </p>
 *
 * @author yww
 * @since 2023/12/6
 */
@RestController
@RequestMapping("/menu")
@Tag(name = "系统授权接口")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Operation(summary = "登录授权", description = "登录接口")
    @PostMapping(value = "/login")
    public R<LoginVO> login(@Validated @RequestBody LoginReq loginReq) throws Exception {
        // 密码解密
        String password = RsaUtil.decryptByPrivateKey(RsaProperties.privateKey, loginReq.getPassword());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginReq.getUsername(), password);
        // 使用认证管理器进行认证
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 将认证信息存储到安全上下文中
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 获取认证后的用户信息
        assert authentication != null;
        AccountUser accountUser = (AccountUser) authentication.getPrincipal();

        // 生成Token
        String token = TokenProvider.createToken(accountUser.getUsername());
        LoginVO loginVO = LoginVO.builder()
                .username(accountUser.getUsername())
                .nickname(accountUser.getNickname())
                .permissions(accountUser.getPermissions())
                .token(token)
                .build();

        return R.ok(loginVO);
    }

}
