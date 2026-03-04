package com.mebius.competition.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@CrossOrigin
public class FileController {

    // 获取当前项目的根目录，并在里面建一个 uploads 文件夹作为“储物间”
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        if (file.isEmpty()) {
            result.put("success", false);
            result.put("message", "上传失败，文件为空！");
            return result;
        }

        // 检查储物间存不存在，不存在就建一个
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            // 给文件重命名，防止别人上传了两个同名的文件导致覆盖 (用 UUID 充当随机流水号)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + extension;

            // 把文件真正存进硬盘
            File dest = new File(UPLOAD_DIR + newFilename);
            file.transferTo(dest);

            // 返回一张“提货单” (文件的访问网址) 给前端
            String fileUrl = "http://localhost:8080/uploads/" + newFilename;

            result.put("success", true);
            result.put("url", fileUrl);
            result.put("message", "文件上传成功！");
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "服务器存储文件失败！");
        }

        return result;
    }
}