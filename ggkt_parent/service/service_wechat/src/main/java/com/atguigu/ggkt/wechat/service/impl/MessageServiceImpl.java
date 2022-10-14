package com.atguigu.ggkt.wechat.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.atguigu.ggkt.utils.exception.GgktException;
import com.atguigu.ggkt.vod.course.CourseFeignClient;
import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.wechat.service.MessageService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Date:  2022/9/4
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    private CourseFeignClient courseApiFeignClient;
    @Resource
    private WxMpService wxMpService;
    @Override
    public String receiveMessage(Map<String, String> param) {
        //content 内容
        String content = "";
        //消息的类型
        String msgType = param.get("MsgType");     // TODO MsgType就因为首字母没大写找了两天错误
        //判断什么类型消息
        switch (msgType) {
            case "text"://普通文本类型，输入关键字java\
                content = this.search(param);
                break;
            case "event"://关注 取消关注  点击关于我们

                String event = param.get("Event");
                String eventKey = param.get("EventKey");
                //关注
                if("subscribe".equals(event)) {//关注公众号
                    content = this.subscribe(param);
                } else if("unsubscribe".equals(event)) {//取消关注公众号
                    content = this.unsubscribe(param);
                } else if("CLICK".equals(event) && "aboutUs".equals(eventKey)){
                    content = this.aboutUs(param);
                } else {
                    content = "success";
                }
                break;

            default:  //其他情况
                content = "success";
        }
        return content;
    }

    @Override
    public void pushPayMessage(long orderId) {
        //获取到微信的openid
        String openid = "oOv8k6rSz_O-uEgGs-QP6o3qSKQE";
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openid)//要推送的用户openid
                .templateId("k5HMkLXC1wGsi-f5e-eZW5Su9diG0PBccZ9AHAVOTQc")//模板id
                .url("http://https://5d90c26260.oicp.vip/#/pay/"+orderId)//点击模板消息要访问的网址
                .build();
        //3,如果是正式版发送消息，，这里需要配置你的信息
        templateMessage.addData(new WxMpTemplateData("first", "亲爱的用户：您有一笔订单支付成功。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", "1314520", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", "java基础课程", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword3", "2022-01-11", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword4", "100", "#272727"));
        templateMessage.addData(new WxMpTemplateData("remark", "感谢你购买课程，如有疑问，随时咨询！", "#272727"));
        String msg = null;
        try {
            msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20001,"没有获取到订单信息");
        }
        System.out.println(msg);
    }

    //关于我们
    private String aboutUs(Map<String, String> param) {
        String about = text(param, "圭冠课堂现开设Java、HTML5前端+全栈、大数据、全链路UI/UE设计、人工智能、大数据运维+Python自动化、" +
                "Android+HTML5混合开发等多门课程；" + "同时，通过视频分享、谷粒学苑在线课堂、大厂学苑直播课堂等多种方式，" +
                "满足了全国编程爱好者对多样化学习场景的需求，已经为行业输送了大量IT技术人才。").toString();
        return about;
    }

    //取消关注公众号
    private String unsubscribe(Map<String, String> param) {
        return "about";
    }

    //关注公众号
    private String subscribe(Map<String, String> param) {
        //处理业务
        return this.text(param, "感谢你关注“硅谷课堂”，可以根据关键字搜索您想看的视频教程，" +
                "如：JAVA基础、Spring boot、大数据等").toString();
    }

    /**
     * 处理关键字搜索事件
     * 图文消息个数；当用户发送文本、图片、语音、视频、图文、地理位置这六种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     * @param param
     * @return
     */
    private String search(Map<String, String> param) {
        String fromusername = param.get("FromUserName");
        String tousername = param.get("ToUserName");
        String content = param.get("Content");
        //单位为秒，不是毫秒
//        Long createTime = new Date().getTime() / 1000;
        long createTime = System.currentTimeMillis() / 1000;
        StringBuffer text = new StringBuffer();
        List<Course> courseList = courseApiFeignClient.findByKeyword(content);
        if(CollectionUtils.isEmpty(courseList)) {
            text = this.text(param, "请重新输入关键字，没有匹配到相关视频课程");
        } else {
            //一次只能返回一个
            Random random = new Random();
            int num = random.nextInt(courseList.size());
            Course course = courseList.get(num);
            StringBuffer articles = new StringBuffer();
            articles.append("<item>");
            articles.append("<Title><![CDATA["+course.getTitle()+"]]></Title>");
            articles.append("<Description><![CDATA["+course.getTitle()+"]]></Description>");
            articles.append("<PicUrl><![CDATA["+course.getCover()+"]]></PicUrl>");
            articles.append("<Url><![CDATA[https://5d90c26260.oicp.vip/#/liveInfo/"+course.getId()+"]]></Url>");
            articles.append("</item>");

            text.append("<xml>");
            text.append("<ToUserName><![CDATA["+fromusername+"]]></ToUserName>");
            text.append("<FromUserName><![CDATA["+tousername+"]]></FromUserName>");
            text.append("<CreateTime><![CDATA["+createTime+"]]></CreateTime>");
            text.append("<MsgType><![CDATA[news]]></MsgType>");
            text.append("<ArticleCount><![CDATA[1]]></ArticleCount>");
            text.append("<Articles>");
            text.append(articles);
            text.append("</Articles>");
            text.append("</xml>");
        }
        return text.toString();
    }
    /**
     * 回复文本
     * @param param
     * @param content
     * @return
     */
    private StringBuffer text(Map<String, String> param, String content) {
        String fromusername = param.get("FromUserName");
        String tousername = param.get("ToUserName");
        //单位为秒，不是毫秒
//        Long createTime = new Date().getTime() / 1000;
        long createTime = System.currentTimeMillis() / 1000;
        StringBuffer text = new StringBuffer();
        text.append("<xml>");
        text.append("<ToUserName><![CDATA["+fromusername+"]]></ToUserName>");
        text.append("<FromUserName><![CDATA["+tousername+"]]></FromUserName>");
        text.append("<CreateTime><![CDATA["+createTime+"]]></CreateTime>");
        text.append("<MsgType><![CDATA[text]]></MsgType>");
        text.append("<Content><![CDATA["+content+"]]></Content>");
        text.append("</xml>");
        return text;
    }

}
