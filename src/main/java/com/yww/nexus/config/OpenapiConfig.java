package com.yww.nexus.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *      Springdoc 配置文件
 * </P>
 *
 * @author yww
 * @since 2023/12/9
 */
@Configuration
public class OpenapiConfig {


    /**
     * springdoc-openapi的总体配置
     */
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(ywwApiInfo());
    }

    /**
     * 返回一个自定义的Info
     */
    public Info ywwApiInfo() {
        return new Info()
                // 应用名称
                .title("nexus")
                // 应用的描述
                .description("nexus")
                // 指向服务条款的URL地址
                .termsOfService("https://yww52.com")
                // API的联系人信息
                .contact(new Contact().name("yww").url("https://yww52.com").email("1141950370@qq.com"))
                // API的证书信息
                .license(new License().name("Apache 2.0").url("https://yww52.com"))
                // API版本信息
                .version("1.0");
    }

    /**
     * RBAC系统接口
     */
    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("RBAC的系统接口")
                .packagesToScan("com.yww.nexus.modules.security.rest", "com.yww.nexus.modules.sys.controller")
                .build();
    }

}
