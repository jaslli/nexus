package com.yww.nexus.specurity;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yww.nexus.modules.security.service.AuthorizationService;
import com.yww.nexus.modules.security.view.LoginReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 *      服务授权接口单元测试
 * </p>
 *
 * @author yww
 * @since 2023/12/7
 */
@SpringBootTest
public class AuthTests {

    @Autowired
    AuthorizationService authorizationService;

    @Test
    void loginTest() {
        String url = "http://127.0.0.1:9797/auth/login";
        LoginReq req = new LoginReq();
        req.setUsername("yww");
        req.setPassword("HbInpSPNimIRJzS2bZpTmH501aOvnUjf8cemOtAyqCQ7BPQ5YY0EOTs+f6teKi0rxnTQB6CCq9wGDbp22lU57z6UUDTPl22T4mRU8gw9lil23xEFiOMHSJM5T/aINARDtCj0ol5FR5dwKPHtHZeUhVlh3IBzjeGtREyERhPfBo8=");
        var response = HttpUtil.post(url, JSONUtil.toJsonStr(req));
        System.out.println(response);
    }

}
