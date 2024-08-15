package com.zljin.gulimall.product.exception;

import com.zljin.gulimall.common.exception.BizCodeEnum;
import com.zljin.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理JSR303的controller的异常
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.zljin.gulimall.product.controller")
public class ProductionExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleJSRException(MethodArgumentNotValidException e) {
        log.error("JSR303数据校验出现问题:{},异常类型{}",e.getMessage(),e.getClass());
        Map<String, String> map = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(t -> {
            map.put(t.getField(), t.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), BizCodeEnum.VAILD_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(), BizCodeEnum.UNKNOW_EXCEPTION.getMsg());
    }
}
