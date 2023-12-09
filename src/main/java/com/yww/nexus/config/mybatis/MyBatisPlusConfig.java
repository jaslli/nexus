package com.yww.nexus.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 *      MyBatisPlus配置类
 * </P>
 *
 * @author yww
 * @since 2023/11/25
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MyBatisPlusConfig implements WebMvcConfigurer {

    /**
     * 插件配置
     * 1. 防全表更新与删除插件
     * 2. 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 使用重构的分页插件，指定数据库为MYSQL
        interceptor.addInnerInterceptor(new PigPaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }

    /**
     * SQL 过滤器避免SQL 注入
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SqlFilterArgumentResolver());
    }

}
