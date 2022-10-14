package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.utils.exception.GgktException;
import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vod.service.VodService;
import com.atguigu.ggkt.vod.util.ConstantPropertiesUtil;
import com.atguigu.ggkt.vod.util.Signature;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Date:  2022/8/25
 */
@Api(tags = "视频点播功能")
@RestController
@RequestMapping("/admin/vod/")
//@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;

    //返回客户端上传视频签名
    @GetMapping("sign")
    public Result sign(){
        Signature sign = new Signature();
        // 设置 App 的云 API 密钥
        sign.setSecretId(ConstantPropertiesUtil.ACCESS_KEY_ID);
        sign.setSecretKey(ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天
        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
            return Result.ok(signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
            throw new GgktException(20008,"签名失败");
        }

    }

    //上传视频
    @PostMapping("upload")
    public Result upload(MultipartFile file) throws IOException {
//            String findId = vodService.updateVideo();
        String updateVideo = vodService.updateVideo();
        return Result.ok(updateVideo);
    }
//    删除视频
    @DeleteMapping("remove/{fileId}")
    public Result remove(@PathVariable("fileId") String fileId){
        vodService.removeVideo(fileId);
        return Result.ok();
    }
}
