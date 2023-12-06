package com.yww.nexus.view;

import com.yww.nexus.constant.RCode;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *      响应信息主体
 * </p>
 *
 * @author yww
 * @since 2023/11/23
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private T data;

    public static <T> R<T> ok() {
        return restResult(RCode.SUCCESS.getStatus(), null, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(RCode.SUCCESS.getStatus(), null, data);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(RCode.SUCCESS.getStatus(), msg, data);
    }

    public static <T> R<T> failed() {
        return restResult(RCode.FAILED.getStatus(), RCode.FAILED.getMessage(), null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(RCode.FAILED.getStatus(), msg, null);
    }

    public static <T> R<T> failed(T data) {
        return restResult(RCode.FAILED.getStatus(), RCode.FAILED.getMessage(), data);
    }

    public static <T> R<T> failed(RCode rCode) {
        return restResult(rCode.getStatus(), rCode.getMessage(), null);
    }

    public static <T> R<T> failed(Integer code, String msg) {
        return restResult(code, msg, null);
    }

    public static <T> R<T> failed(Integer code, String msg, T data) {
        return restResult(code, msg, data);
    }


    public static <T> R<T> restResult(int code, String msg, T data) {
        R<T> result = new R<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
