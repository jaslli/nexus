package com.yww.nexus.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.nexus.entity.Menu;

import java.util.List;

/**
 * <p>
 *      菜单权限实体类 服务类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据角色ID查询菜单信息
     *
     * @param roleId 角色ID
     * @return 菜单信息
     */
    List<Menu> getMenusByRoleId(String roleId);

    /**
     * 根据用户名查询菜单信息
     *
     * @param username 用户名
     * @return 菜单信息
     */
    List<Menu> getMenus(String username);

}
