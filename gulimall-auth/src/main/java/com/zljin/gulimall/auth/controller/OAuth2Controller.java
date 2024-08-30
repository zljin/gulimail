package com.zljin.gulimall.auth.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zljin.gulimall.auth.feign.MemberFeignService;
import com.zljin.gulimall.auth.vo.SocialUser;
import com.zljin.gulimall.common.utils.R;
import com.zljin.gulimall.common.vo.MemberRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Base64;

/**
 * github社交登录
 * <p>
 * https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps
 */
@Slf4j
@Controller
public class OAuth2Controller {

    @Value("${oauth.github.clientId}")
    private String clientId;

    @Value("${oauth.github.clientSecret}")
    private String clientSecret;

    @Value("${oauth.github.redirectUrl}")
    private String redirectUrl;

    @Value("${oauth.github.oauth2SuccessUrl}")
    private String oauth2SuccessUrl;

    @Value("${oauth.github.oauthOpenApiUrl1}")
    private String oauthOpenApiUrl1;

    @Resource
    MemberFeignService memberFeignService;


    /**
     * 社交登录回调
     * 先获取code --> 然后获取access_token --> 根据token获取用户信息
     *
     * @return
     */
    @GetMapping("/oauth2.0/github/success")
    public String github(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session, HttpServletResponse servletResponse, HttpServletRequest request) {
        String reqUrl1 = oauth2SuccessUrl.replace("CLIENT_ID", clientId)
                .replace("CLIENT_SECRET", new String(Base64.getDecoder().decode(clientSecret)))
                .replace("CALLBACK", redirectUrl)
                .replace("CODE", code);
        log.info("reqUrl1:{}", reqUrl1);
        try {
            String accessToken = getAccessTokenByCode(reqUrl1);
            if(StrUtil.isEmpty(accessToken)){
                return "redirect:http://auth.gulimall.com/login.html";
            }
            //获取到accessToken后
            JSONObject userMsg = getOauthUserMessage(accessToken);
            //获取用户信息
            String login = MapUtil.getStr(userMsg, "login");
            if(StrUtil.isEmpty(login)){
                return "redirect:http://auth.gulimall.com/login.html";
            }
            //登录或者注册这个社交用户
            SocialUser socialUser = genSocialUser(userMsg);
            R oauthlogin = memberFeignService.oauthlogin(socialUser);
            if(oauthlogin.getCode() == 0){
                MemberRespVo data = oauthlogin.getData("data", new TypeReference<MemberRespVo>() {});
                session.setAttribute("loginUser",data);
                return "redirect:http://gulimall.com";
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "redirect:http://auth.gulimall.com/login.html";
    }

    private String getAccessTokenByCode(String reqUrl1){
        String result = HttpRequest.get(reqUrl1)
                .header("Accept", "application/json").execute().body();
        JSONObject entries = JSONUtil.parseObj(result);
        log.info("result1:{}", JSONUtil.toJsonStr(result));
        return MapUtil.getStr(entries, "access_token","");
    }

    private JSONObject getOauthUserMessage(String accessToken){
        String result2 = HttpRequest.get(oauthOpenApiUrl1)
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .execute().body();
        log.info("result2:{}", JSONUtil.toJsonStr(result2));
        return JSONUtil.parseObj(result2);
    }

    private SocialUser genSocialUser(JSONObject userMsg){
        SocialUser socialUser = new SocialUser();
        socialUser.setIsRealName(MapUtil.getStr(userMsg, "name"));
        socialUser.setUid(MapUtil.getStr(userMsg, "id"));
        socialUser.setAvatar(MapUtil.getStr(userMsg, "avatar_url"));
        return socialUser;
    }

    public static void main(String[] args) {
        // 要加密的文本
        String content = "aaa";
        String content2 = "YWFh";
        System.out.println(new String(Base64.getDecoder().decode(content2.getBytes())));
        System.out.println(Base64.getEncoder().encodeToString(content.getBytes()));
    }

}
