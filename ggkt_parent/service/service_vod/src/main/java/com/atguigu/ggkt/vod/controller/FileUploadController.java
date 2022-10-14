package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.utils.result.Result;
import com.atguigu.ggkt.vod.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Date:  2022/8/15
 */
@Api(tags = "长传头像接口")
@RestController
@RequestMapping("/admin/vod/file/")
//@CrossOrigin    //为了解决跨域问题
public class FileUploadController {
    @Resource
    private FileService fileService;
    @ApiOperation("文件上传")
    @PostMapping("upload")
    public Result upload(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        String uploadUrl = fileService.upload(file);
        return Result.ok(uploadUrl).message("文件上传成功");
    }
}
