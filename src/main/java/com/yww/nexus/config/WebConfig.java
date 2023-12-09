package com.yww.nexus.config;

import cn.hutool.core.date.DatePattern;
import jakarta.annotation.Nonnull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * <p>
 *      WEB请求配置
 * </p>
 *
 * @author yww
 * @since 2023/11/24
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = SERVLET)
public class WebConfig implements WebMvcConfigurer {

    /**
     * 重写跨域过滤器，实现全局跨域
     */
    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //  放行全部原始域
        config.addAllowedOriginPattern("*");
        //  允许跨越发送cookie
        config.setAllowCredentials(true);
        //  放行哪些请求方式
        config.addAllowedMethod("*");
        //  放行全部原始头信息
        config.addAllowedHeader("*");
        //  放行所有头信息
        config.addExposedHeader("*");
        // 2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        // 3. 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }

    /**
     * 增加GET请求参数中时间类型转换 {@link com.yww.nexus.config.jackson.JavaTimeModule}
     * <ul>
     * <li>HH:mm:ss -> LocalTime</li>
     * <li>yyyy-MM-dd -> LocalDate</li>
     * <li>yyyy-MM-dd HH:mm:ss -> LocalDateTime</li>
     * </ul>
     * @param registry  注册
     */
    @Override
    public void addFormatters(@Nonnull FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(DatePattern.NORM_TIME_FORMATTER);
        registrar.setDateFormatter(DatePattern.NORM_DATE_FORMATTER);
        registrar.setDateTimeFormatter(DatePattern.NORM_DATETIME_FORMATTER);
        registrar.registerFormatters(registry);
    }

}
