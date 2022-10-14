package com.atguigu.ggkt.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.utils.exception.GgktException;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.atguigu.ggkt.wechat.service.MenuService;
import com.atguigu.ggkt.wechat.utils.ConstantPropertiesUtil;
import com.atguigu.ggkt.wechat.utils.HttpClientUtils;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author atguigu
 * @since 2022-09-01
 */
@Api(tags = "公众号菜单")
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {
    @Resource
    private MenuService menuService;
    //同步菜单方法
    @ApiOperation(value = "同步菜单")
    @GetMapping("syncMenu")
    public Result syncMenu() {
        menuService.syncMenu();
        return Result.ok();
    }
    //可以不用写在String menuResult = this.wxMpService.getMenuService().menuCreate(button.toJSONString());已经获取到了
    //获取access_token
    @GetMapping("getAccessToken")
    public Result getAccessToken() {
//        String token = menuService.getToken();
        //拼接请求地址
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://api.weixin.qq.com/cgi-bin/token");
        buffer.append("?grant_type=client_credential");
        buffer.append("&appid=%s");
        buffer.append("&secret=%s");

        //设置路径参数
        String url = String.format(buffer.toString(),
                ConstantPropertiesUtil.ACCESS_KEY_ID,
                ConstantPropertiesUtil.ACCESS_KEY_SECRET);

        //get请求
        try {
//            httpClient就是一个网络请求框架，专门周来发送网络请求的安卓开发用的挺多的
            String tokenString = HttpClientUtils.get(url);
            //获取access_token
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            String token = jsonObject.getString("access_token");
            //返回
            return Result.ok(token);
//            return token;
        } catch (Exception e) {
            throw new GgktException(20008,"token获取异常");
        }
//        return Result.ok(token);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return Result.ok(menu);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.ok();
    }

    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.ok();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        menuService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据id列表删除")     // TODO
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        menuService.removeByIds(idList);
        return Result.ok();
    }

    //删除公众号菜单
    @ApiOperation(value = "删除公众号菜单")
    @DeleteMapping("removeMenu")
    public Result removeMenu(){
        menuService.removeMenu();
        return Result.ok();
    }

    //获取所有菜单，按照一级和二级菜单封装
    @GetMapping("findMenuInfo")
    public Result findMenuInfo() {
        List<MenuVo> menuVoLists = menuService.findMenuInfo();
        return Result.ok(menuVoLists);
    }
    //获取所有一级菜单
    @GetMapping("findOneMenuInfo")
    public Result findOneMenuInfo() {
        List<Menu> menuList = menuService.findMenuOneInfo();
        return Result.ok(menuList);
    }

}

