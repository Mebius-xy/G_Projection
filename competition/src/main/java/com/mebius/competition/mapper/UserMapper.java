package com.mebius.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mebius.competition.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 继承了 BaseMapper<User> 后，MyBatis-Plus 自动帮我们写好了增删改查的方法
 * 就像系统送了你一把万能铲子，你不需要自己写 SQL 语句就能查 User 表了
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}