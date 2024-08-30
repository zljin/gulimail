package com.zljin.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.member.entity.MemberEntity;
import com.zljin.gulimall.member.exception.PhoneExsitException;
import com.zljin.gulimall.member.exception.UsernameExistException;
import com.zljin.gulimall.member.vo.MemberLoginVo;
import com.zljin.gulimall.member.vo.MemberRegistVo;
import com.zljin.gulimall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author leonard
 * @email leoanrd_zou@163.com
 * @date 2024-08-16 15:30:47
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUser socialUser);

    void checkPhoneUnique(String phone) throws PhoneExsitException;

    void checkUsernameUnique(String username) throws UsernameExistException;
}

