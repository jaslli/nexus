package com.yww.nexus.annotation;

import java.lang.annotation.*;

/**
 * <p>
 *      用于标记匿名访问的接口
 * </P>
 *
 * @author yww
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonymousAccess {
}
