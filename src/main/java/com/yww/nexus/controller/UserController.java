package com.yww.nexus.controller;

import com.yww.nexus.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/user")
@Tag(name = "用户信息实体类接口")
public class UserController {

    private final IUserService service;

    @Autowired
    public UserController(IUserService service) {
        this.service = service;
    }

    @Operation(summary = "通过用户名查询用户信息", description = "根据用户名来查询对应的用户数据")
    @GetMapping("/getById/{username}")
    public Result<User> getById(@PathVariable("username") String username) {
        return Result.success(service.getByUsername(username));
    }

}
