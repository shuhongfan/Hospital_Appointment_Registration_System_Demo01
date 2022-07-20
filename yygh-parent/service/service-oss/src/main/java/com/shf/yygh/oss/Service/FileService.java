package com.shf.yygh.oss.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 上传文件到阿里云oss
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
