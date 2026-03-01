package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.QuestionBank;
import com.mebius.competition.mapper.QuestionBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/study")
@CrossOrigin
public class StudyController {

    @Autowired
    private QuestionBankMapper questionBankMapper;

    /**
     * 获取所有题库数据
     * (后续如果题目太多，也可以像之前一样改成分页接口)
     */
    @GetMapping("/questions")
    public List<QuestionBank> getQuestions() {
        // 按照浏览量(views)从高到低倒序排列，让热门题目在最上面
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("views");

        return questionBankMapper.selectList(queryWrapper);
    }
}