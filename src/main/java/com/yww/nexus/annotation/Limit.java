package com.yww.nexus.annotation;


import com.yww.nexus.constant.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *      配合Redis和AOP实现的限流注解
 *      在接口上使用即可，没有必需属性
 * </P>
 *
 * @author yww
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

    /**
     * 限流类型
     * 默认为针对IP限流
     */
    LimitType limitType() default LimitType.IP;

    /**
     * 资源名称，用于描述接口功能。
     * 默认为 类名 + 。 + 调用方法名
     */
    String name() default "";

    /**
     * 缓存key
     * 默认为 类名 + 。 + 调用方法名
     */
    String key() default "";

    /**
     * 项目前缀
     */
    String prefix() default "yww";

    /**
     * 时间范围，单位秒
     * 默认为60秒
     */
    int period() default 60;

    /**
     * 限制访问次数
     * 默认为20次
     */
    int count() default 20;

    /**
     * 对象里的属性名
     * 仅当{@link #limitType}为{@code LimitType.POJO_FIELD}时有用
     */
    String field() default "";

    /**
     * 要用来作为key组成的参数索引(从0开始), 该索引对应的参数必须为string/Long/Integer/Short/Byte
     * 仅当{@link #limitType}为{@code LimitType.PARAM}时有用
     */
    int keyParamIndex() default 0;

    /**
     * 达到限流上限时的错误提示
     */
    String errMsg() default "操作过于频繁，请稍后再试";

}
