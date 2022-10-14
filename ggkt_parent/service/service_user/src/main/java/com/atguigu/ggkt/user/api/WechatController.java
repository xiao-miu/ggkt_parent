package com.atguigu.ggkt.user.api;

import com.alibaba.fastjson.JSON;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.user.service.UserInfoService;
import com.atguigu.ggkt.utils.exception.GgktException;
import com.atguigu.ggkt.utils.utils.JwtHelper;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * Date:  2022/9/6
 */
/*@RestController注解，相当于@Controller+@ResponseBody两个注解的结合，
返回json数据不需要在方法前面加@ResponseBody注解了，但使用@RestController这个注解，
就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
 */
@Controller
@RequestMapping("/api/user/wechat")
public class WechatController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private WxMpService wxMpService;
    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    //用户授权跳转
    @GetMapping("authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {
        //把#号转换回guiguketan
        String redirectURL = wxMpService.oauth2buildAuthorizationUrl(userInfoUrl, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl.replace("guiguketan","#")));
        return "redirect:" + redirectURL;
    }
    //returnUrl返回的地址
    @GetMapping("userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl){

        try {
            //拿着code请求
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            //获取openid
            String openId = wxMpOAuth2AccessToken.getOpenId();
            System.out.println("【微信网页授权】openId={}"+openId);
            //获取微信信息（头像等）
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            System.out.println("【微信网页授权】wxMpUser={}"+ JSON.toJSONString(wxMpUser));
            //获取微信信息添加数据库
            UserInfo userInfo = userInfoService.getByOpenid(openId);
            if(null == userInfo) {
                userInfo = new UserInfo();
                userInfo.setOpenId(openId);
                userInfo.setUnionId(wxMpUser.getUnionId());
                userInfo.setNickName(wxMpUser.getNickname());
                userInfo.setAvatar(wxMpUser.getHeadImgUrl());
                userInfo.setSex(wxMpUser.getSexId());
                userInfo.setProvince(wxMpUser.getProvince());
                userInfoService.save(userInfo);
            }
            //授权完成之后，跳转具体功能页面
            //生成token，按照一定规则生成字符串，可以包含用户信息
            String token = JwtHelper.createToken(userInfo.getId(),userInfo.getNickName());
//            如有用&  ,没有参数用？
            if(returnUrl.indexOf("?") == -1) {
                return "redirect:" + returnUrl + "?token=" + token;
            }else {
                return "redirect:" + returnUrl + "&token=" + token;
            }

        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20001,"获取地址异常");
        }
    }
}
