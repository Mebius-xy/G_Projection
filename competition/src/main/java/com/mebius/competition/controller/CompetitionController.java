package com.mebius.competition.controller;

import com.mebius.competition.entity.Competition;
import com.mebius.competition.mapper.CompetitionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/competition")
@CrossOrigin
public class CompetitionController {

    @Autowired
    private CompetitionMapper competitionMapper;

    /**
     * 1. 获取所有竞赛列表 (之前已有的)
     */
    @GetMapping("/list")
    public List<Competition> getList() {
        return competitionMapper.selectList(null);
    }

    /**
     * 2. 发布新竞赛
     */
    @PostMapping("/add")
    public Map<String, Object> addCompetition(@RequestBody Competition comp) {
        Map<String, Object> result = new HashMap<>();
        comp.setCreateTime(LocalDateTime.now());
        comp.setStatus(1); // 默认 1 (进行中)

        competitionMapper.insert(comp);
        result.put("success", true);
        result.put("message", "竞赛发布成功！");
        return result;
    }

    /**
     * 3. 修改竞赛信息
     */
    @PostMapping("/update")
    public Map<String, Object> updateCompetition(@RequestBody Competition comp) {
        Map<String, Object> result = new HashMap<>();
        competitionMapper.updateById(comp);
        result.put("success", true);
        result.put("message", "竞赛信息更新成功！");
        return result;
    }

    /**
     * 4. 删除竞赛
     * 使用 @PathVariable 获取网址路径里的 id，例如 /api/competition/delete/5
     */
    @PostMapping("/delete/{id}")
    public Map<String, Object> deleteCompetition(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        competitionMapper.deleteById(id);
        result.put("success", true);
        result.put("message", "竞赛已删除！");
        return result;
    }
}