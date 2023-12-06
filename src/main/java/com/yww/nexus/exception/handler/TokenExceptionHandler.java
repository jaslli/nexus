package com.yww.nexus.exception.handler;

import com.auth0.jwt.exceptions.*;
import com.yww.nexus.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * <p>
 *      Token验证的异常处理
 * </p>
 *
 * @author  yww
 * @since  2023/12/6
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 2)
public class TokenExceptionHandler {

    /**
     * 处理签名算法不匹配的异常信息
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = AlgorithmMismatchException.class)
    public R<String> algorithmMismatchExceptionHandler(AlgorithmMismatchException e, HttpServletRequest request) {
        log.error(">> AlgorithmMismatchException: {}, {}", request.getRequestURI(), e.getMessage());
        return R.failed(500, "签名算法不匹配", e.getMessage());
    }

    /**
     * 处理签名无效的异常信息
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = SignatureVerificationException.class)
    public R<String> signatureVerificationExceptionHandler(SignatureVerificationException e, HttpServletRequest request) {
        log.error(">> SignatureVerificationException: {}, {}", request.getRequestURI(), e.getMessage());
        return R.failed(500, "签名无效", e.getMessage());
    }

    /**
     * 处理令牌已过期的异常信息
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = TokenExpiredException.class)
    public R<String> tokenExpiredExceptionHandler(TokenExpiredException e, HttpServletRequest request) {
        log.error(">> TokenExpiredException: {}, {}", request.getRequestURI(), e.getMessage());
        return R.failed(500, "令牌已过期", e.getMessage());
    }

    /**
     * 处理缺少要验证的声明的异常信息
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = MissingClaimException.class)
    public R<String> tMissingClaimExceptionHandler(MissingClaimException e, HttpServletRequest request) {
        log.error(">> MissingClaimException: {}, {}", request.getRequestURI(), e.getMessage());
        return R.failed(500, "缺少要验证的声明", e.getMessage());
    }

    /**
     * 处理声明包含的值和预期不符合的声明的异常信息
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = IncorrectClaimException.class)
    public R<String> incorrectClaimExceptionHandler(IncorrectClaimException e, HttpServletRequest request) {
        log.error(">> IncorrectClaimException: {}, {}", request.getRequestURI(), e.getMessage());
        return R.failed(500, "声明包含的值和预期不符合", e.getMessage());
    }

    /**
     * 处理验证中的某一个步骤失败的声明的异常信息
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ResponseBody
    @ExceptionHandler(value = JWTVerificationException.class)
    public R<String> jwtVerificationExceptionHandler(JWTVerificationException e, HttpServletRequest request) {
        log.error(">> JWTVerificationException: {}, {}", request.getRequestURI(), e.getMessage());
        return R.failed(500, "验证中的某一个步骤失败", e.getMessage());
    }

}
