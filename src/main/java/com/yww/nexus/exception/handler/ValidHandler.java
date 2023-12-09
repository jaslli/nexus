package com.yww.nexus.exception.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yww.nexus.base.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *      参数校验异常处理类
 * </p>
 *
 * @author  yww
 * @since  2023/12/6
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 2)
public class ValidHandler {

    /**
     * 处理单个参数异常的情况
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R<String> constraintViolationExceptionHandler(ConstraintViolationException e, HttpServletRequest request) {
        log.error(">> constraintViolation exception: {}, {}", request.getRequestURI(), e.getMessage());
        // 获取异常信息
        String errMessage = e.getMessage();
        // 防止空的错误信息
        if (StringUtils.isBlank(errMessage)) {
            errMessage = "服务器繁忙";
        }
        return R.failed(500, errMessage);
    }

    /**
     * 处理参数校验异常
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<Map<String,Object>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error(">> MethodArgumentNotValid exception: {}, {}", request.getRequestURI(), e.getMessage());
        // 获取异常信息
        BindingResult bindingResult = e.getBindingResult();
        Map<String,Object> res = new HashMap<>(16);
        bindingResult.getFieldErrors().forEach(error -> res.put(error.getField(), error.getDefaultMessage()));
        return R.failed(500, "参数校验异常", res);
    }

}
