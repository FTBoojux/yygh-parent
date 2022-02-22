package com.boojux.serviceoss.controller;

import com.boojux.serviceoss.service.FileService;
import com.example.yygh.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {
    @Autowired
    private FileService fileService;

    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){
        String url = fileService.upload(file);
        return Result.ok(url);
    }
}
