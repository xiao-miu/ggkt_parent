package com.atguigu.ggkt.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.utils.exception.GgktException;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.atguigu.ggkt.wechat.mapper.MenuMapper;
import com.atguigu.ggkt.wechat.service.MenuService;
import com.atguigu.ggkt.wechat.utils.ConstantPropertiesUtil;
import com.atguigu.ggkt.wechat.utils.HttpClientUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jdk.nashorn.internal.ir.annotations.Reference;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-01
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Resource
    private WxMpService wxMpService;
    //获取所有的菜单数据
    @Override
    public List<MenuVo> findMenuInfo() {
        //1创建List集合﹐用户最终数据封装
        List<MenuVo> finallist = new ArrayList<>();
        //2查询所有菜单数据（包含一级和二级)
        List<Menu> menuList = baseMapper.selectList(null);
        //3从所有菜单数据获取所有一级菜单数据（ parent_id=0)      filter筛选
        List<Menu> oneMenuList = menuList.stream().filter(menu -> menu.getParentId().longValue() == 0)
                .collect(Collectors.toList());
        //4封装一级菜单数据﹐放到最终数据list集合
        //遍历一级菜单List集合
        for (Menu oneMenu : oneMenuList) {
//            从menu转换成  menuVo
            MenuVo oneMenuVo = new MenuVo();
            //menu里的数据放到 nenuVo里
            BeanUtils.copyProperties(oneMenu, oneMenuVo);

            //5封装二级菜单数据（判断一级菜单id和二级菜单parent_id是否相同)
            // 如果相同﹐把二级菜单数据放到一级菜单里面
            List<Menu> towMenuList = menuList.stream().filter(menu -> {
                return menu.getParentId().longValue() == oneMenu.getId();
            }).collect(Collectors.toList());
            //把获取到的数据放到 子菜单里
            ArrayList<MenuVo> children = new ArrayList<>();
            for (Menu towMenu : towMenuList) {
                //把二级菜单放到一级里面
                MenuVo towMenuVo = new MenuVo();
                BeanUtils.copyProperties(towMenu, towMenuVo);
                children.add(towMenuVo);
            }
            //把二级菜单放到一级菜单里
            oneMenuVo.setChildren(children);
            //把oneMenuVo放到最终List集合
            finallist.add(oneMenuVo);
        }
        return finallist;
    }

    //获取所有一级菜单
    @Override
    public List<Menu> findMenuOneInfo() {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        //parent_id  =  0的时候是一级菜单
        wrapper.eq("parent_id",0);
        List<Menu> enumList = baseMapper.selectList(wrapper);
        return enumList;
    }

    @Override //可以不用写在String menuResult = this.wxMpService.getMenuService().menuCreate(button.toJSONString());已经获取到了
    public String getToken() {
        //拼接请求地址
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://api.weixin.qq.com/cgi-bin/token");
        buffer.append("?grant_type=client_credential");
        buffer.append("&appid=%s");
        buffer.append("&secret=%s");

        //设置路径参数
        String url = String.format(buffer.toString(), ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

        //get请求
        try {
//            httpClient就是一个网络请求框架，专门周来发送网络请求的安卓开发用的挺多的
            String tokenString = HttpClientUtils.get(url);
            //获取access_token
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            String access_token = jsonObject.getString("access_token");
            //返回
            return access_token;
        } catch (Exception e) {
            throw new GgktException(20008,"token获取异常");
        }
    }
    //同步菜单方法
    @Override
    public void syncMenu() {

        //获取所有菜单数据
        List<MenuVo> menuInfoList = this.findMenuInfo();
        //封装button里面结构，数组格式
        JSONArray bottionList = new JSONArray();
        for (MenuVo oneMenuVo : menuInfoList) {
            //json对象（一级菜单）
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            //二级菜单
            JSONArray subButton = new JSONArray();
            for (MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                view.put("type", twoMenuVo.getType());
                if(twoMenuVo.getType().equals("view")) {
                    view.put("name", twoMenuVo.getName());
                    view.put("url", "https://5d90c26260.oicp.vip/#"
                            +twoMenuVo.getUrl());
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey());
                }
                subButton.add(view);
            }
            one.put("sub_button", subButton);
            bottionList.add(one);
        }
        //封装最外层button部分
        JSONObject button = new JSONObject();
        button.put("button", bottionList);


        //菜单同步创建
        try {
            String menuResult = this.wxMpService.getMenuService().menuCreate(button.toJSONString());
            System.out.println("menu菜单id："+menuResult);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20008,"菜单同步失败异常");
        }
    }
    //删除公众号菜单
    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20009,"公众号菜单删除失败");
        }
    }
}
