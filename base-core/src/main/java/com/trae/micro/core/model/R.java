package com.trae.micro.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功标记
     */
    private Boolean success;

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 其他参数
     */
    private Map<String, Object> params;

    private R() {
    }

    /**
     * 成功返回结果
     */
    public static <T> R<T> success() {
        return success(null);
    }

    /**
     * 成功返回结果
     *
     * @param data 返回数据
     */
    public static <T> R<T> success(T data) {
        return success(data, "操作成功");
    }

    /**
     * 成功返回结果
     *
     * @param data    返回数据
     * @param message 返回消息
     */
    public static <T> R<T> success(T data, String message) {
        R<T> r = new R<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    /**
     * 失败返回结果
     */
    public static <T> R<T> error() {
        return error(500, "操作失败");
    }

    /**
     * 失败返回结果
     *
     * @param message 返回消息
     */
    public static <T> R<T> error(String message) {
        return error(500, message);
    }

    /**
     * 失败返回结果
     *
     * @param code    返回码
     * @param message 返回消息
     */
    public static <T> R<T> error(Integer code, String message) {
        R<T> r = new R<>();
        r.setSuccess(false);
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    /**
     * 添加参数
     */
    public R<T> put(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    /**
     * 添加参数
     */
    public R<T> putAll(Map<String, Object> map) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.putAll(map);
        return this;
    }
}