package com.yww.nexus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.nexus.entity.Menu;
import com.yww.nexus.mapper.MenuMapper;
import com.yww.nexus.service.IMenuService;
import com.yww.nexus.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *      菜单权限实体类 服务实现类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final IUserService userService;

    @Override
    public List<Menu> getMenusByRoleId(String roleId) {
        return baseMapper.getMenusByRoleId(roleId);
    }

    @Override
    public List<Menu> getMenus(String username) {
        System.out.println(username);
        String roleId = userService.getRoleIdByUserName(username);
        List<Menu> all = getMenusByRoleId(roleId);
        return all.stream()
                .filter(menu -> "0".equals(menu.getPid()))
                .peek(menu -> menu.setChildren(getChildrenList(menu, all)))
                .collect(Collectors.toList());
    }

    /**
     * 递归获取子节点
     *
     * @param menu  当前节点
     * @param all   所有菜单节点
     * @return      /
     */
    public static List<Menu> getChildrenList(Menu menu, List<Menu> all){
        return all.stream()
                .filter(item -> item.getPid().equals(menu.getId()))
                .peek(item -> item.setChildren(getChildrenList(item, all)))
                .collect(Collectors.toList());
    }

}

