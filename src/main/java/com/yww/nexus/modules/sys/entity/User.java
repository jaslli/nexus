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
 *      (user)用户信息实体类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "User", description = "用户信息实体类")
public class User extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "数据ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "用户昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "用户头像地址")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "用户邮箱地址")
    @TableField("email")
    private String email;

    @Schema(description = "账号状态,0禁用|1正常")
    @TableField("status")
    private Boolean status;

}