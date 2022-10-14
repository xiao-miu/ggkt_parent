package com.atguigu.ggkt.vod.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Date:  2022/8/15
 */
public interface FileService {
    String upload(MultipartFile file);
}
