package com.zljin.gulimall.auth.feign;

import com.zljin.gulimall.auth.vo.UserLoginVo;
import com.zljin.gulimall.auth.vo.UserRegistVo;
import com.zljin.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);
}
