package com.yww.nexus.modules.sys.controller;

import com.yww.nexus.base.R;
import com.yww.nexus.modules.sys.entity.User;
import com.yww.nexus.modules.sys.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *      用户信息实体类 前端控制器
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "用户信息实体类接口")
@RequestMapping("/sys/user")
public class UserController {

    private final IUserService service;

    @PostMapping("/insert")
    public R<User> insert(@RequestBody User user) {
        boolean save = service.save(user);
        return R.ok(user);
    }

}
