package com.yww.nexus.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yww.nexus.modules.sys.entity.Menu;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *      菜单权限实体类 Mapper 接口
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Mapper
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色ID获取菜单信息
     *
     * @param roleId 角色ID
     * @return 菜单信息
     */
    List<Menu> getMenusByRoleId(@Param("roleId") String roleId);

}
