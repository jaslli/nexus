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
