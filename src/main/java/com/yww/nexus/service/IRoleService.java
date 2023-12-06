package com.yww.nexus.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.nexus.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *      角色实体类 服务类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
public interface IRoleService extends IService<Role> {

    /**
     * 根据角色ID获取权限信息
     *
     * @param roleIds   角色ID
     * @return          权限信息
     */
    Set<String> getMenuCodesByRoleIds(List<Integer> roleIds);

}
