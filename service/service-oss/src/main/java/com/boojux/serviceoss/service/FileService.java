package com.boojux.serviceoss.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface FileService {

    String upload(MultipartFile file);
}
