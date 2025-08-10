package com.masteranything.security.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.masteranything.security.exception.GeneralException;


public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static byte[] readFileFromLocation(String fileUrl){
        if (fileUrl.isBlank()){
            return null;
        }

        try {
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        } catch (IOException ex) {
            logger.error("No file found in path {}", fileUrl);
            throw new GeneralException("No file found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
