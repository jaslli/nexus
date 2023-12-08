package com.yww.nexus.modules.security.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *      刷新Token接口的返回结果
 * </p>
 *
 * @author chenhao
 * @since 2023/12/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshVo {

    @Schema(description = "访问授权Token")
    private String accessToken;

    @Schema(description = "刷新Token")
    private String refreshToken;

}
