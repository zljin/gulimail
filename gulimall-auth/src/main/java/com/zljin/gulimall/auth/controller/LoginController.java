package com.zljin.gulimall.auth.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import com.zljin.gulimall.auth.feign.MemberFeignService;
import com.zljin.gulimall.auth.feign.MultihubFeignService;
import com.zljin.gulimall.auth.vo.UserLoginVo;
import com.zljin.gulimall.auth.vo.UserRegistVo;
import com.zljin.gulimall.common.constant.AuthServerConstant;
import com.zljin.gulimall.common.exception.BizCodeEnum;
import com.zljin.gulimall.common.utils.R;
import com.zljin.gulimall.common.vo.MemberRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Resource
    MultihubFeignService multihubFeignService;
    @Resource
    MemberFeignService memberFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone) {
        //1、接口防刷。
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000) {
                //60秒内不能再发
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        //2、验证码的再次校验,sms:code:phonenum -> 验证码
        String code = UUID.randomUUID().toString().substring(0, 5);
        String substring = code + "_" + System.currentTimeMillis();
        //redis缓存验证码，防止同一个phone在60秒内再次发送验证码
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, substring, 10, TimeUnit.MINUTES);
        multihubFeignService.sendCode(phone, code);
        return R.ok();
    }


    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result,
                         RedirectAttributes redirectAttributes,
                         HttpSession session){
        if(result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(a -> a.getField(), a -> a.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors",errors);
            //用户注册->/regist[post]---->转发/reg.html（路径映射默认都是get方式访问的。）
            //校验出错，转发到注册页
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //1、校验验证码
        String code = vo.getCode();

        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if(!StringUtils.isEmpty(s)){
            if(code.equals(s.split("_")[0])){
                //删除验证码;令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                //验证码通过。 //真正注册。调用远程服务进行注册
                R r = memberFeignService.regist(vo);
                if(r.getCode() == 0){
                    //成功
                    return "redirect:http://auth.gulimall.com/login.html";
                }else{
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", MapUtil.getStr(r,"msg",""));
                    redirectAttributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.gulimall.com/reg.html";
                }

            }else{
                Map<String, String> errors =  new HashMap<>();
                errors.put("code","验证码错误");
                redirectAttributes.addFlashAttribute("errors",errors);
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        }else {
            Map<String, String> errors =  new HashMap<>();
            errors.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",errors);
            //校验出错，转发到注册页
            return "redirect:http://auth.gulimall.com/reg.html";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes,
                        HttpSession session) {
        //远程登录
        R login = memberFeignService.login(vo);
        if (login.getCode() == 0) {
            MemberRespVo data = login.getData("data", new TypeReference<MemberRespVo>() {
            });
            ////成功放到session中
            session.setAttribute(AuthServerConstant.LOGIN_USER, data);
            return "redirect:http://gulimall.com";
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", MapUtil.getStr(login,"msg",""));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }


    @GetMapping({"/","/login.html"})
    public String loginPage(HttpSession session){
        return "login";
    }

    @GetMapping("/reg.html")
    public String regPage(HttpSession session){
        return "reg";
    }
}
