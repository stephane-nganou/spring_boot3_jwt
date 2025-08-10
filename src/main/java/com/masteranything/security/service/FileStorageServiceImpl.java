package com.masteranything.security.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.masteranything.security.exception.GeneralException;

import jakarta.annotation.Nonnull;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final static String UPLOADING_ERROR_MSG = "An Error occured while uploading the file";

    private final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Value("${spring.file.upload.photos-output-path}")
    private String fileUploadPath;

    @Override
    public String saveFile(@Nonnull MultipartFile file, @Nonnull Long userId) {
        
        final String fileUploadSubPath = "users" + File.separator + userId;

        return uploadFile(file, fileUploadSubPath);
    }

    private String uploadFile(
        MultipartFile file,
        @Nonnull String fileUploadSubPath
    ){
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        final File targetFolder = new File(finalUploadPath);
        if(!targetFolder.exists()) {
            final boolean folderCreated = targetFolder.mkdir();
            if(!folderCreated){
                logger.info("Failed to create the target folder");
                throw new GeneralException(
                    UPLOADING_ERROR_MSG,
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }

        final String fileExtension = getFileExtension(file.getOriginalFilename());
        final String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        final Path targePath = Paths.get(targetFilePath);
        try {
            Files.write(targePath, file.getBytes());
            logger.info("File saved successfully");
            return targetFilePath;
        } catch (IOException e) {
            logger.error("Error occured while saving the File: {}", e);
            throw new GeneralException(
                    UPLOADING_ERROR_MSG,
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
        }
        
    }

    private String getFileExtension(String fileName){
        if(null == fileName || fileName.isEmpty()){
            logger.error("fileName is null or empty");
            throw new GeneralException(
                UPLOADING_ERROR_MSG,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        final int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1){
            logger.error("No extension found in fileName: {}", fileName);
            throw new GeneralException(
                UPLOADING_ERROR_MSG,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }



}
