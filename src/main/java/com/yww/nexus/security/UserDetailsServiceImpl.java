package com.yww.nexus.security;

import com.yww.nexus.moudles.sys.entity.Role;
import com.yww.nexus.moudles.sys.entity.User;
import com.yww.nexus.exception.GlobalException;
import com.yww.nexus.moudles.sys.service.IRoleService;
import com.yww.nexus.moudles.sys.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *      重写SpringSecurity的登录方法
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserService userService;
    private final IRoleService roleService;

    /**
     * 通过用户名查找用户信息
     *
     * @param username 用户名
     * @return UserDetails     用户信息
     * @throws UsernameNotFoundException 找不到对应用户名的用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("查询并获取用户授权信息----->username: {}", username);

        // 查询用户
        Optional<User> optionalUser = userService.getByUsername(username);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("查询不到用户信息！"));

        // 如果用户被禁用，直接抛出异常
        if (!user.getStatus()) {
            throw new GlobalException("用户已经被禁用！");
        }
        // 角色权限
        Set<Role> roleSet = userService.getRolesById(user.getId());
        // 菜单权限
        List<Integer> roleIds = roleSet.stream().map(Role::getId).toList();
        Set<String> menuCodes = roleService.getMenuCodesByRoleIds(roleIds);

        return AccountUser.builder()
                .userid(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .enabled(user.getStatus())
                .roles(roleSet.stream().map(Role::getCode).collect(Collectors.toList()))
                .permissions(List.copyOf(menuCodes))
                .build();
    }

}
