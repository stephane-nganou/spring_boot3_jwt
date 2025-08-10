package com.masteranything.security.service;

import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {
    String saveFile(MultipartFile file, Long userId);

}
