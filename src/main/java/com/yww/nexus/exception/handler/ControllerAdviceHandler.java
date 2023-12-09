package com.yww.nexus.exception.handler;

import com.yww.nexus.base.R;
import com.yww.nexus.exception.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * <p>
 *      全局异常处理
 * </p>
 *
 * @author  yww
 * @since  2023/12/6
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdviceHandler {

    /**
     * 处理自定义的服务异常信息
     * 统一处理GlobalException异常，异常处理顺序是从小异常到大异常。
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = GlobalException.class)
    public <T> R<T> globalExceptionHandler(GlobalException e, HttpServletRequest request) {
        log.error(">> global exception: {}, {}, {}", request.getRequestURI(), e.getCode(), e.getMessage());
        String errMessage = e.getMessage();
        // 防止空的错误信息
        if (StringUtils.isBlank(errMessage)) {
            errMessage = "服务出现未知错误！";
        }
        return R.failed(e.getCode(), errMessage);
    }

    /**
     * 异常信息
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public <T> R<T> defaultErrorHandler(Exception e, HttpServletRequest request) {
        log.error(">> 服务器内部错误 " + request.getRequestURI(), e);
        return R.failed(500, "服务出现未知错误！");
    }

}
