package com.yww.nexus.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.nexus.entity.Role;
import com.yww.nexus.entity.User;

import java.util.Optional;
import java.util.Set;

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
    Optional<User> getByUsername(String username);

    /**
     * 根据用户ID获取用户角色的权限编码
     * @param id    用户ID
     * @return      权限编目集合
     */
    Set<Role> getRolesById(Integer id);
}
