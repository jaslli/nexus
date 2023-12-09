package com.yww.nexus.annotation.rest;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yww.nexus.annotation.Limit;
import com.yww.nexus.constant.LimitType;
import com.yww.nexus.utils.RequestUtils;
import com.yww.nexus.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *      限流注解AOP
 * </p>
 *
 * @author yww
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {

    private final StringRedisTemplate stringRedisTemplate;

    public LimitAspect(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 声明切入点
     */
    @Pointcut("@annotation(com.yww.nexus.annotation.Limit)")
    private void pointCut() {}

    /**
     * 处理请求前通知
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Limit limit = method.getAnnotation(Limit.class);

        // 构建限流缓存KEY
        String limitKey = buildLimitKey(limit, joinPoint, method);
        log.debug(">> 限流缓存KEY: {}", limitKey);
        if (StrUtil.isBlank(limitKey)) {
            return;
        }

        // 执行限流lua脚本
        List<String> keys = Collections.singletonList(limitKey);
        String luaScript = buildLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long count = stringRedisTemplate.execute(redisScript, keys, String.valueOf(limit.count()), String.valueOf(limit.period()));

        if (count == null || count == 0) {
            log.error(limit.errMsg());
        }

        String name = limit.name();
        name = StrUtil.isNotBlank(name) ? name : method.getDeclaringClass() + "." + method.getName();
        log.debug("第{}次访问，KEY为 {}，描述为 [{}] 的接口", count, keys, name);
    }


    /**
     * 构建限流lua脚本
     *
     * @return 限流lua脚本
     */
    private static String buildLuaScript() {
        return """
                -- lua 下标从 1 开始
                -- 限流 key
                local key = KEYS[1]
                -- 限流大小
                local limit = tonumber(ARGV[1])
                local perSeconds = tonumber(ARGV[2])

                -- 获取当前流量大小
                local currentLimit = tonumber(redis.call('get', key) or 0)

                if currentLimit + 1 > limit then
                    -- 达到限流大小 返回
                    return 0;
                else
                    -- 没有达到阈值 value + 1
                    redis.call('INCRBY', key, 1)
                    -- EXPIRE的单位是秒
                    redis.call('EXPIRE', key, perSeconds)
                    return currentLimit + 1
                end
                """;
    }

    /**
     * 根据限流类型构建key
     *
     * @param limit         注解
     * @param joinPoint     连接点
     * @param method        限流方法
     * @return              限流Key
     */
    private String buildLimitKey(Limit limit, JoinPoint joinPoint, Method method) {
        // 获取注解信息
        String limitKey = null;
        String prefix = limit.prefix();
        String key = limit.key();
        key = StringUtils.isNotBlank(key) ? key : method.getDeclaringClass() + "." + method.getName();
        LimitType limitType = limit.limitType();

        // 根据限流类型构建限流Key
        Object[] args = joinPoint.getArgs();
        switch (limitType) {
            case IP:
                limitKey = buildKey(LimitType.IP, prefix, key, RequestUtils.getIpAddr());
                break;
            case USER:
                // 按用户username限流
                String username = SecurityUtil.getCurrentUsername();
                if (StrUtil.isNotBlank(username)) {
                    limitKey = buildKey(LimitType.USER, prefix, key, username);
                } else {
                    log.warn(">> 未找到登录用户信息,限流失败: {}", joinPoint);
                }
                break;
            case POJO_FIELD:
                String field = limit.field();
                if (StringUtils.isBlank(field)) {
                    log.warn(">> 未设置field,限流失败: {}", joinPoint);
                    break;
                }
                // 获取对象的属性值
                if (args == null || args.length == 0 || args[0] == null) {
                    log.warn(">> 未找到对象,限流失败: {}", joinPoint);
                    break;
                }
                String fieldValue = getPojoField(args[0], field);
                if (StringUtils.isBlank(fieldValue)) {
                    log.warn(">> 对象字段值为空,请检查限流字段是否准确,限流失败: {}", joinPoint);
                    break;
                }
                limitKey = buildKey(LimitType.POJO_FIELD, prefix, key, fieldValue);
                break;
            case PARAM:
                int keyIndex = limit.keyParamIndex();

                if (keyIndex < 0 || args == null || args.length < (keyIndex + 1) || args[keyIndex] == null) {
                    log.warn(">> 未找到参数或参数值为空,限流失败: {}, keyParamIndex={}", joinPoint, keyIndex);
                } else if (isValidKeyParamType(args[keyIndex])) {
                    limitKey = buildKey(LimitType.PARAM, prefix, key, String.valueOf(args[keyIndex]));
                } else {
                    log.warn(">> 设置的参数不是string/long/int/short/byte类型,限流失败: {}", joinPoint);
                }
                break;
            case KEY:
                limitKey = buildKey(LimitType.KEY, prefix, key, null);
                break;
            default:
                break;
        }
        return limitKey;
    }

    /**
     * 构建限流key
     *
     * @param type      限流类型
     * @param prefix    key前缀
     * @param key       key
     * @param param     参数
     * @return          限流key
     */
    public static String buildKey(LimitType type, String prefix, String key, String param) {
        String result = "";
        if (StringUtils.isNotBlank(prefix)) {
            result = prefix + ".";
        }
        switch (type) {
            case IP:
                result += "ip.";
                break;
            case USER:
                result += "user.";
                break;
            case PARAM:
                result += "param.";
                break;
            case POJO_FIELD:
                result += "filed.";
                break;
            case KEY:
                result += "key.";
                break;
            default:
                break;
        }
        result += key;
        // 参数不为空就添加参数
        if (StringUtils.isNotBlank(param)) {
            result += "." + param;
        }
        return result;
    }

    /**
     * 判断参数是否为string/long/int/short/byte类型
     *
     * @param param 参数
     * @return      如果属于以上类型返回true
     */
    private boolean isValidKeyParamType(Object param) {
        return (param instanceof String) || (param instanceof Long) || (param instanceof Integer) || (param instanceof Short)
                || (param instanceof Byte);
    }

    /**
     * 获取对象属性值
     *
     * @param pojo  对象
     * @param field 属性
     * @return      对象的属性值
     */
    private String getPojoField(Object pojo, String field) {
        try {
            JSONObject object = JSON.parseObject(JSON.toJSONString(pojo));
            return object.getString(field);
        } catch (Exception e) {
            return null;
        }
    }

}
