package com.yww.nexus.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.nexus.modules.sys.entity.Role;
import com.yww.nexus.modules.sys.entity.User;
import com.yww.nexus.modules.sys.mapper.UserMapper;
import com.yww.nexus.modules.sys.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public Optional<User> getByUsername(String username) {
        User user = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        return Optional.ofNullable(user);
    }

    public Set<Role> getRolesById(Integer id) {
        Set<Role> roles = baseMapper.getRoleCodeById(id);
        return roles == null ? Collections.emptySet() : roles;
    }

}
