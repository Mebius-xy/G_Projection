package com.mebius.competition.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 这个配置类的作用是告诉 Spring Boot：
 * 当有人在浏览器里访问 http://localhost:8080/uploads/xxx.zip 时，
 * 请你不要去拦截它，直接去我电脑硬盘的 uploads 文件夹里把东西拿给他！
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = "file:" + System.getProperty("user.dir") + "/uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}