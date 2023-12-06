package com.yww.nexus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;

/**
 * <p>
 *      (role)角色实体类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("role")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "Role", description = "角色实体类")
public class Role extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "数据ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "角色编码")
    @TableField("code")
    private String code;

    @Schema(description = "角色名称")
    @TableField("name")
    private String name;

    @Schema(description = "角色描述")
    @TableField("description")
    private String description;

}
