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
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    public static final String[] JSON_WHITELIST_STR = { "org.springframework", "com.yww" };

    /**
     * SQL过滤时检测的关键词
     */
    public final static String[] KEYWORDS = { "master", "truncate", "insert", "select", "delete", "update", "declare",
            "alter", "drop", "sleep", "extractvalue", "concat" };

}
