package com.yww.nexus.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yww.nexus.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * <p>
 *      (menu)菜单权限实体类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "Menu", description = "菜单权限实体类")
public class Menu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "数据ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "上级数据ID")
    @TableField("pid")
    private Long pid;

    @Schema(description = "菜单（权限）名称")
    @TableField("name")
    private String name;

    @Schema(description = "数据类型,0菜单|1按钮(权限)")
    @TableField("type")
    private Boolean type;

    @Schema(description = "菜单显示,0显示|1隐藏")
    @TableField("visible")
    private Boolean visible;

    @Schema(description = "菜单路由地址")
    @TableField("path")
    private String path;

    @Schema(description = "组件名称")
    @TableField("component")
    private String component;

    @Schema(description = "菜单图标")
    @TableField("icon")
    private String icon;

    @Schema(description = "排序字段")
    @TableField("sort")
    private Short sort;

    @Schema(description = "权限代码")
    @TableField("code")
    private String code;

}
