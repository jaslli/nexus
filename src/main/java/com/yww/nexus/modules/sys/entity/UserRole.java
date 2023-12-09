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
 *      (user_role)用户角色关系实体类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_user_role")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "UserRole", description = "用户角色关系实体类")
public class UserRole extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "数据ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "角色ID")
    @TableField("role_id")
    private Long roleId;

}
