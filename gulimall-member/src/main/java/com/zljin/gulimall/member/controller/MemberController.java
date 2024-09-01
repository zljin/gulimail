package com.zljin.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zljin.gulimall.common.exception.BizCodeEnum;
import com.zljin.gulimall.member.exception.PhoneExsitException;
import com.zljin.gulimall.member.exception.UsernameExistException;
import com.zljin.gulimall.member.feign.CouponFeignService;
import com.zljin.gulimall.member.vo.MemberLoginVo;
import com.zljin.gulimall.member.vo.MemberRegistVo;
import com.zljin.gulimall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zljin.gulimall.member.entity.MemberEntity;
import com.zljin.gulimall.member.service.MemberService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.R;



/**
 * 会员
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-16 15:30:47
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;


    @PostMapping("/oauth2/login")
    public R oauthlogin(@RequestBody SocialUser socialUser) throws Exception {

        MemberEntity entity =  memberService.login(socialUser);
        if(entity!=null){
            return R.ok().setData(entity);
        }else{
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMsg());
        }
    }


    @PostMapping("/regist")
    public R regist(@RequestBody MemberRegistVo vo){

        try{
            memberService.regist(vo);
        }catch (PhoneExsitException e){
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
        }catch (UsernameExistException e){
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(),BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        }

        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo){

        MemberEntity entity =  memberService.login(vo);
        if(entity!=null){
            return R.ok().setData(entity);
        }else{
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMsg());
        }
    }

    @GetMapping("/coupons")
    public R getCoupons(){
        MemberEntity user = new MemberEntity();
        user.setNickname("leonard");
        R memberCoupons = couponFeignService.memberCoupons();
        return R.ok().put("member",user).put("coupons",memberCoupons.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
