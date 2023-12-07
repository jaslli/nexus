package com.yww.nexus.modules.security.view;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author yww
 * @since 2023/12/6
 */
@Getter
@Setter
public class LoginReq {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
