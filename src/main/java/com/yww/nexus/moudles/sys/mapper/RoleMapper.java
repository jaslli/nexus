package com.yww.nexus.moudles.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yww.nexus.moudles.sys.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *      角色实体类 Mapper 接口
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    Set<String> getMenuCodesByRoleIds(List<Integer> roleIds);

}
