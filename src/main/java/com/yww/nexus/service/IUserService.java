package com.yww.nexus.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.nexus.entity.User;

/**
 * <p>
 *      用户信息实体类 服务类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return User    用户信息
     */
    User getByUsername(String username);

    /**
     * 通过用户名获取用户权限信息
     *
     * @param username 用户名
     * @return 用户权限信息
     */
    String getUserAuthorities(String username);

    /**
     * 根据用户名查询角色ID
     *
     * @param username 用户名
     * @return 角色ID
     */
    String getRoleIdByUserName(String username);

    /**
     * 根据用户名查询用户ID
     *
     * @param username 用户名
     * @return 用户ID
     */
    String getUserIdByUserName(String username);

}
