package com.zljin.gulimall.coupon.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zljin.gulimall.common.exception.BizCodeEnum;
import com.zljin.gulimall.common.utils.R;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * sentinel 自定义流控响应
 *
 */
@Component
public class GulimallSentinelConfig implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws IOException {
        R r = R.error(BizCodeEnum.TO_MANY_REQUEST.getCode(),BizCodeEnum.TO_MANY_REQUEST.getMsg());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(r));
    }
}
