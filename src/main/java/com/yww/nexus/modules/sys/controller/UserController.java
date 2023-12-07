package com.yww.nexus.modules.sys.controller;

import com.yww.nexus.modules.sys.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

}
