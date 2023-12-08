package com.yww.nexus.constant;

/**
 * <p>
 *      通用常量信息
 * </P>
 *
 * @author yww
 * @since 2023/11/25
 */
public class Constants {

    /**
     * SpringSecurity的请求白名单
     */
    public static final String[] URL_WHITELIST = {
            // 系统授权接口
            "/auth/login","/auth/refresh",
            // 放行Knife4j的主页和swagger的资源请求
            "/doc.html", "/webjars/**", "/v3/**",
            // 放行druid数据源
            "/druid/**",
    };

    /**
     * SpringSecurity的静态资源白名单
     */
    public static final String[] STATIC_WHITELIST = {
            "/resources/*/*.html",
            "/resources/*/*.css",
            "/resources/*/*.js",
            "/resources/*/*.ts"
    };

    /**
     * 仅允许解析的包名，范围越小越安全
     */
    public static final String[] JSON_WHITELIST_STR = {
            // security的所需实体类
            "com.yww.nexus.security",
            // 授权实体类
            "com.yww.nexus.modules.security.view",
            // 用户管理系统实体类
            "com.yww.nexus.modules.sys.entity",
    };

    /**
     * SQL过滤时检测的关键词
     */
    public final static String[] KEYWORDS = { "master", "truncate", "insert", "select", "delete", "update", "declare",
            "alter", "drop", "sleep", "extractvalue", "concat" };

}
