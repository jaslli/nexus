package com.yww.nexus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.util.List;

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
@TableName("menu")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "Menu", description = "菜单权限实体类")
public class Menu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "数据ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "上级数据ID")
    @TableField("pid")
    private Integer pid;

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
    private Integer sort;

    @Schema(description = "权限字段")
    @TableField("code")
    private String code;

    @Schema(description = "子菜单")
    @TableField(exist = false)
    private List<Menu> children;

}
