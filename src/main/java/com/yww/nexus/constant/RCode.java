package com.yww.nexus.constant;

import lombok.Getter;

/**
 * <p>
 *       结果状态码枚举
 * </p>
 *
 * @author yww
 * @since 2023/11/23
 */
@Getter
public enum RCode {

    /**
     * 成功
     */
    SUCCESS(200, "成功"),

    /**
     * 服务器错误
     */
    FAILED(500, "服务器发生错误");

    private final Integer status;
    private final String message;

    RCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

}
