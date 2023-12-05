package com.yww.nexus.controller;

import com.yww.nexus.service.impl.MenuServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Log("获取菜单")
    @Operation(summary = "通过用户名查询权限信息", description = "根据用户名来查询对应的权限或菜单数据")
    @GetMapping("/getMenus/{username}")
    public Result<List<Menu>> getMenus(@PathVariable("username") String username) {
        return Result.success(service.getMenus(username));
    }

}
