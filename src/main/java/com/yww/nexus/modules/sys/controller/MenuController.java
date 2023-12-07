package com.yww.nexus.modules.sys.controller;

import com.yww.nexus.modules.sys.service.impl.MenuServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *      菜单权限实体类 前端控制器
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@RestController
@RequestMapping("/menu")
@Tag(name = "菜单权限实体类接口")
public class MenuController {

    private final MenuServiceImpl service;

    public MenuController(MenuServiceImpl service) {
        this.service = service;
    }

}
