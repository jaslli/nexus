package com.yww.nexus.modules.security.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 *      在线用户信息
 * </p>
 *
 * @author yww
 * @since 2023/12/7
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserDto {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;


    /**
     * 浏览器
     */
    private String browser;

    /**
     * IP
     */
    private String ip;

    /**
     * 登录时间
     */
    private Date loginTime;

}
