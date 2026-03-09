package com.mebius.competition.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

/**
 * 🌟 系统大堂经理 (全局异常处理) 🌟
 * @ControllerAdvice 告诉 Spring Boot 这是一个全局拦截器，盯着所有的 Controller
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 专门拦截“包裹太大”的异常 (比如超出了我们刚才设置的 50MB)
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public Map<String, Object> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "上传失败：您的文件太大了！请确保不要超过 50MB。");
        return result;
    }

    /**
     * 兜底拦截：拦截所有其他的未知报错 (比如空指针、数据库断开等)
     * 不把恐怖的英文错误抛给前端，而是包装成友好的中文提示
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> handleGlobalException(Exception e) {
        // 在后台偷偷打印真实的错误，方便我们自己排查
        e.printStackTrace();

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "服务器开小差了，请稍后再试！(错误代码: " + e.getMessage() + ")");
        return result;
    }
}