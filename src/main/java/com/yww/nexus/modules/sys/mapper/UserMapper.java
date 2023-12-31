package com.yww.nexus.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yww.nexus.modules.sys.entity.Role;
import com.yww.nexus.modules.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * <p>
 *      用户信息实体类 Mapper 接口
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户ID获取角色权限编码
     *
     * @param userId    用户ID
     * @return      角色权限编码集合
     */
    Set<Role> getRoleCodeById(Long userId);

}
