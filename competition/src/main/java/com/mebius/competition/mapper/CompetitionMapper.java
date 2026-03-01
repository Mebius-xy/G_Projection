package com.mebius.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mebius.competition.entity.Competition;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompetitionMapper extends BaseMapper<Competition> {
    // 继承后，自动拥有对 sys_competition 表的增删改查能力
}