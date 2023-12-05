package com.yww.nexus.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.nexus.entity.Menu;
import com.yww.nexus.entity.User;
import com.yww.nexus.mapper.UserMapper;
import com.yww.nexus.service.IMenuService;
import com.yww.nexus.service.IRoleService;
import com.yww.nexus.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *      用户信息实体类 服务实现类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final IRoleService roleService;
    private final IMenuService menuService;

    @Override
    public User getByUsername(String username) {
        User res = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        AssertUtils.notNull(res, "没有找到该用户！");
        return res;
    }

    @Override
    public String getUserAuthorities(String username) {
        StringBuilder authority = new StringBuilder();
        // 目前该系统一个用户只对应一个角色信息
        String roleId = getRoleIdByUserName(username);
        String roleCode = roleService.getById(roleId).getCode();
        if (StrUtil.isNotBlank(roleCode)) {
            authority.append("YW_").append(roleCode);
        }
        List<Menu> menus = menuService.getMenusByRoleId(roleId);
        if (menus.size() > 0) {
            for (Menu menu : menus) {
                if (StrUtil.isNotBlank(menu.getCode())) {
                    authority.append(",").append(menu.getCode());
                }
            }
        }
        return authority.toString();
    }

    @Override
    public String getRoleIdByUserName(String username) {
        return baseMapper.getRoleIdByUserId(getUserIdByUserName(username));
    }

    @Override
    public String getUserIdByUserName(String username) {
        return baseMapper.getUserIdByUserName(username).getId();
    }

}
