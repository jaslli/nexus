package com.yww.nexus.utils;

import cn.hutool.json.JSONUtil;
import com.yww.nexus.base.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 *      用于接口外响应JSON的响应工具类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Slf4j
public class ResponseUtil {

    /**
     * 用于返回JSON数据
     *
     * @param response 请求响应
     * @param result   响应封装
     */
    public static void response(HttpServletResponse response, R<Object> result) {
        // 设置响应的Header
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        // 设置响应码
        response.setStatus(result.getCode());
        // 设置响应内容
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(result));
        } catch (IOException e) {
            log.warn(e.getMessage());
        } finally {
            if (null != writer) {
                writer.flush();
                writer.close();
            }
        }
    }

}
