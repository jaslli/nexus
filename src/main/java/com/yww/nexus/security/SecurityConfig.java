package com.yww.nexus.security;

import cn.hutool.extra.spring.SpringUtil;
import com.yww.nexus.annotation.AnonymousAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *      SpringSecurity核心配置类
 * </p>
 *
 * @author yww
 * @since 2023/12/5
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 请求白名单
     */
    private static final String[] URL_WHITELIST = {
            // 登录和注销接口允许匿名访问
            "/login", "/logout",
            // 放行Knife4j的主页和swagger的资源请求
            "/doc.html", "/webjars/**", "/v3/**",
            // 放行druid数据源
            "/druid/**",
    };

    /**
     * 用户未登录或登陆过期处理类
     */
    private final TokenAuthenticationEntryPoint authenticationEntryPoint;
    /**
     * 用户登录成功的处理器
     */
    private final LoginSuccessHandler loginSuccessHandler;
    /**
     * 用户登录失败的处理器
     */
    private final LoginFailureHandler loginFailureHandler;
    /**
     * 用户认证失败的处理器
     */
    private final AccessFailureHandler accessFailureHandler;
    /**
     * 用户注销成功的处理器
     */
    private final LogoutSuccessfullyHandler logoutSuccessfullyHandler;
    /**
     * 用户服务Service
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 自定义Token的过滤器
     */
    @Bean
    TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(this.authenticationManager());
    }

    /**
     * 密码加密工具
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 移除{@code  ROLE_}前缀
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    /**
     * AuthenticationManager的构造器
     * 需要配置UserDetailService 和 PasswordEncoder
     * 先调用UserDetailService的loadUserByUsername()然后再使用PasswordEncoder.matches()比较
     */
    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }


    /**
     * 配置静态资源的处理，即在resources下的html，css，js文件（暂无用处）
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**/*.html", "/resources/**/*.css", "/resources/**/*.js");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 允许跨域
                .cors().and()
                // 关闭csrf
                .csrf().disable()
                // 防止iframe 造成跨域
                .headers()
                .frameOptions()
                .disable()
                .and()
                // 使用自定义session管理策略，让 SpringSecurity不创建和使用 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 登录配置
        http.formLogin()
                // 登录页的用户名属性名
                .usernameParameter("username")
                // 登录页的密码属性名
                .passwordParameter("password")
                // 登陆接口请求路径（POST请求）
                .loginProcessingUrl("/login")
                // 登陆成功处理器
                .successHandler(loginSuccessHandler)
                // 登陆失败处理器
                .failureHandler(loginFailureHandler);
        // 注销配置
        http.logout()
                // 注销登陆接口
                .logoutUrl("/logout")
                // 注销成功处理器
                .logoutSuccessHandler(logoutSuccessfullyHandler);
        // 异常处理器的配置
        http.exceptionHandling()
                // 未登录处理类
                .authenticationEntryPoint(authenticationEntryPoint)
                // 权限不足处理类
                .accessDeniedHandler(accessFailureHandler);
        // 自定义Token过滤器
        http.addFilter(tokenAuthenticationFilter());
        // 授权路由配置
        http
                .authorizeHttpRequests()
                // 放行白名单
                .requestMatchers(URL_WHITELIST).permitAll()
                // options 开放
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 自定义匿名访问所有URL放行
                .requestMatchers(getAnonymousUrl()).anonymous()
                // 其余请求进行认证访问
                .anyRequest().authenticated();
        return http.build();
    }

    /**
     * 获取所有标有注解AnonymousAccess的访问路径
     *
     * @return 需要匿名访问的访问路径
     */
    private String[] getAnonymousUrl() {
        // 获取所有的 RequestMapping
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = SpringUtil.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> res = new HashSet<>();
        // 循环 RequestMapping
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethods.entrySet()) {
            HandlerMethod value = infoEntry.getValue();
            // 获取方法上 AnonymousAccess 类型的注解
            AnonymousAccess methodAnnotation = value.getMethodAnnotation(AnonymousAccess.class);
            // 如果方法上标注了 AnonymousAccess 注解，就获取该方法的访问全路径
            if (methodAnnotation != null) {
                if (infoEntry.getKey().getPatternsCondition() != null) {
                    res.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                }
            }
        }
        return res.toArray(new String[0]);
    }

}
