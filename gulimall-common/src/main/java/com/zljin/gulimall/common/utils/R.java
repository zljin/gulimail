

package com.zljin.gulimall.common.utils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConverter;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    //利用hutool json进行逆转
    public <T> T getData(String key, TypeReference<T> typeReference){
        Object data = get(key);//默认是map
        String s = JSONUtil.toJsonStr(data);
        T t = JSONUtil.toBean(s,typeReference,true);
        return t;
    }

    //利用hutool json进行逆转
    public <T> T getData(TypeReference<T> typeReference){
        Object data = get("data");//默认是map
        String s = JSONUtil.toJsonStr(data);
        T t = JSONUtil.toBean(s,typeReference,true);
        return t;
    }

    public R setData(Object data){
        put("data",data);
        return this;
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public  Integer getCode() {
        return (Integer) this.get("code");
    }
}
