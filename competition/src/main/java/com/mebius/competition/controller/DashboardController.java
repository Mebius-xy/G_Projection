package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.ApplicationMapper;
import com.mebius.competition.mapper.CompetitionMapper;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
public class DashboardController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CompetitionMapper competitionMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    /**
     * 获取用于画图的统计数据
     */
    @GetMapping("/chartsData")
    public Map<String, Object> getChartsData() {
        Map<String, Object> result = new HashMap<>();

        // === 1. 班级人数分布 (给饼图用) ===
        // 在实际应用中，这里应该用 SQL 的 GROUP BY。
        // 为了方便演示，我们在 Java 内存里做个简单的统计
        List<User> students = userMapper.selectList(new QueryWrapper<User>().eq("role", "student"));
        Map<String, Integer> classCountMap = new HashMap<>();
        for (User student : students) {
            String className = student.getClassName();
            if (className == null || className.trim().isEmpty()) {
                className = "未分配班级";
            }
            classCountMap.put(className, classCountMap.getOrDefault(className, 0) + 1);
        }

        // 把 Map 转成 ECharts 需要的 [{name: '计科1班', value: 10}, ...] 格式
        List<Map<String, Object>> pieData = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : classCountMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("value", entry.getValue());
            pieData.add(item);
        }
        result.put("pieData", pieData);

        // === 2. 近期系统活跃度模拟数据 (给折线图用) ===
        // 通常这里需要查询日志表。这里我们生成一组模拟的近期访问量数据。
        List<String> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();
        // 模拟过去 7 天的数据
        for (int i = 6; i >= 0; i--) {
            xData.add("前 " + i + " 天");
            // 生成 50 到 150 之间的随机波动数据
            yData.add((int)(Math.random() * 100) + 50);
        }
        result.put("lineXData", xData);
        result.put("lineYData", yData);

        result.put("success", true);
        return result;
    }
}