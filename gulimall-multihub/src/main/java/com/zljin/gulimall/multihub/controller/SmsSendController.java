package com.zljin.gulimall.multihub.controller;

import com.zljin.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @GetMapping("/sendcode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code){
        log.info(phone+"===>"+code);
        log.debug(phone+"===>"+code);
        log.warn(phone+"===>"+code);
        return R.ok();
    }
}
