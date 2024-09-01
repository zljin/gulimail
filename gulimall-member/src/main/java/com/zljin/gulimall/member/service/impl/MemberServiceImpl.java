package com.zljin.gulimall.member.service.impl;

import com.zljin.gulimall.member.dao.MemberLevelDao;
import com.zljin.gulimall.member.entity.MemberLevelEntity;
import com.zljin.gulimall.member.exception.PhoneExsitException;
import com.zljin.gulimall.member.exception.UsernameExistException;
import com.zljin.gulimall.member.vo.MemberLoginVo;
import com.zljin.gulimall.member.vo.MemberRegistVo;
import com.zljin.gulimall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.Query;

import com.zljin.gulimall.member.dao.MemberDao;
import com.zljin.gulimall.member.entity.MemberEntity;
import com.zljin.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) {
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = new MemberEntity();

        //设置默认等级
//        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(1l);


        //检查用户名和手机号是否唯一。为了让controller能感知异常，异常机制
        checkPhoneUnique(vo.getPhone());
        checkUsernameUnique(vo.getUserName());


        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());

        entity.setNickname(vo.getUserName());

        //密码要进行加密存储。
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);

        //其他的默认信息

        //保存
        memberDao.insert(entity);
    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        String loginacct = vo.getLoginacct();
        String password = vo.getPassword();
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginacct)
                .or().eq("mobile", loginacct));
        if (entity == null) {
            return null;
        } else {
            String passwordDb = entity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(password, passwordDb);
            if (matches) {
                return entity;
            } else {
                return null;
            }
        }
    }

    @Override
    public MemberEntity login(SocialUser socialUser) {
        //登录和注册合并逻辑
        String uid = socialUser.getUid();
        //1、判断当前社交用户是否已经登录过系统；
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));
        if (memberEntity == null) {
            MemberEntity regist = new MemberEntity();
            regist.setUsername(socialUser.getIsRealName());
            regist.setSocialUid(socialUser.getUid());
            this.baseMapper.insert(regist);
            return regist;
        }
        return memberEntity;
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExsitException {
        MemberDao memberDao = this.baseMapper;
        Integer mobile = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (mobile > 0) {
            throw new PhoneExsitException();
        }
    }


    @Override
    public void checkUsernameUnique(String username) throws UsernameExistException {
        MemberDao memberDao = this.baseMapper;
        Integer count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count > 0) {
            throw new UsernameExistException();
        }
    }

}