package com.yww.nexus.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yww.nexus.modules.sys.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    Set<String> getMenuCodesByRoleIds(@Param("roleIds") List<Integer> roleIds);

}
