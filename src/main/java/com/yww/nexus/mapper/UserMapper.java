package com.yww.nexus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yww.nexus.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *      用户信息实体类 Mapper 接口
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户ID获取角色ID
     *
     * @param userId 用户名
     * @return 角色ID
     */
    String getRoleIdByUserId(@Param("userId") String userId);

    /**
     * 根据用户名获取用户ID
     *
     * @param username 用户名
     * @return 用户ID
     */
    User getUserIdByUserName(@Param("username") String username);

}
