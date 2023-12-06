package com.yww.nexus.moudles.security.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 *      登录返回结果封装类
 * </p>
 *
 * @author yww
 * @since 2023/12/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "权限")
    private List<String> permissions;

    @Schema(description = "访问token")
    private String token;

}
