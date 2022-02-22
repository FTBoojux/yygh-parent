package com.boojux.serviceoss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.boojux.serviceoss.service.FileService;
import com.boojux.serviceoss.utils.ConstantOssPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file) {
        String ednpoint = ConstantOssPropertiesUtils.EDNPOINT;
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
        String secrect = ConstantOssPropertiesUtils.SECRECT;
        String bucket = ConstantOssPropertiesUtils.BUCKET;
        try{
            System.out.println(ConstantOssPropertiesUtils.ACCESS_KEY_ID);
            OSS oss = new OSSClientBuilder().build(ednpoint, accessKeyId, secrect);
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid + filename;

            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            filename = timeUrl + "/" + filename;
            oss.putObject(bucket,filename,inputStream);
            oss.shutdown();
            String url = "https://" + bucket + "." + ednpoint + "/" + filename;
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
