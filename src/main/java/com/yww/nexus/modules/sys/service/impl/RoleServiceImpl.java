package com.yww.nexus.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.nexus.modules.sys.entity.Role;
import com.yww.nexus.modules.sys.mapper.RoleMapper;
import com.yww.nexus.modules.sys.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *      角色实体类 服务实现类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public Set<String> getMenuCodesByRoleIds(List<Long> roleIds) {
        if (roleIds.isEmpty()) {
            return null;
        }
        var codes = baseMapper.getMenuCodesByRoleIds(roleIds);
        return codes == null ? Collections.emptySet() : codes;
    }

}
