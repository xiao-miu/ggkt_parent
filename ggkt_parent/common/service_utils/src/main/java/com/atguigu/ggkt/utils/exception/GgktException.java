package com.atguigu.ggkt.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date:  2022/8/12
 */
@Data
@AllArgsConstructor  //有参
@NoArgsConstructor
//自定义异常    必须要手动捕获异常才可以
public class GgktException extends RuntimeException{
    private Integer code;   //异常状态码
    private String msg;     //异常信息
}
