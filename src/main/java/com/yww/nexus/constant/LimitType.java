package com.yww.nexus.constant;

/**
 * <p>
 *      限流类型枚举
 * </P>
 *
 * @author yww
 * @since 2023/12/9
 */
public enum LimitType {

    /**
     * 针对每个IP进行限流
     */
    IP,
    /**
     * 针对用户进行限流
     */
    USER,
    /**
     * 针对对象的某个属性值进行限流
     */
    POJO_FIELD,
    /**
     * 针对某个参数进行限流
     */
    PARAM,
    /**
     * 直接对指定的key进行限流
     */
    KEY

}

