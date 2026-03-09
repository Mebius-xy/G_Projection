package com.mebius.competition.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 🌟 JSON 序列化全局最高级配置 🌟
 * 核心作用：将后端所有长达 19 位的 Long 型 ID，在发给前端时全部穿上 "双引号" 变成 String。
 * 防止前端 JavaScript 接收时因为数字太大而引发精度丢失（四舍五入）。
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 这种写法优先级极高，会直接介入 Spring Boot 的底层序列化过程
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
        };
    }
}