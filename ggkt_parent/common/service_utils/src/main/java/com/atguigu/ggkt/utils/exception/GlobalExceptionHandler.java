package com.atguigu.ggkt.utils.exception;

import com.atguigu.ggkt.utils.result.Result;
import org.springframework.web.bind.annotation.*;


/**
 * Date:  2022/8/12
 */
@ControllerAdvice   //aop思想
public class GlobalExceptionHandler {
    //全局异常处理
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(null).message("执行全局异常处理");
    }
    //特定异常处理    一个整数除以零
    @ResponseBody
    @ExceptionHandler(GgktException.class)
    public Result error(GgktException e){
        e.printStackTrace();
        return Result.fail(null).code(e.getCode()).message(e.getMsg());
    }


}
