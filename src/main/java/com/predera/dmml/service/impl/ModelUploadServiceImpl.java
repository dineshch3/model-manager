package com.predera.dmml.service.impl;

import com.predera.dmml.customexception.FileUploadStorageException;
import com.predera.dmml.service.ModelUploadService;
import com.predera.dmml.service.dto.ModelUploadStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import liquibase.util.file.FilenameUtils;

@Service("modelUploadService")
public class ModelUploadServiceImpl implements ModelUploadService {

    private final Logger log = LoggerFactory.getLogger(ModelUploadServiceImpl.class);
    private Path rootLocation;

    public Path getRootLocation() {
        return rootLocation;
    }

    @Autowired
    public ModelUploadServiceImpl(ModelUploadStorageProperties properties) {
        String folderPath = properties.getLocation();
        log.debug("Model Storage Location: {}", folderPath);
        if (folderPath == null) {
            folderPath = System.getProperty("java.io.tmpdir");
            log.warn("Model Storage Location not found. Falling back to temp dir - {}", folderPath);
        }
        this.rootLocation = Paths.get(folderPath);
    }

    @Override
    public Path store(MultipartFile file, String name, String version) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = FilenameUtils.getExtension(filename);
        String newFilename = name +"_"+ version + "." + extension;

        try {
            if (file.isEmpty()) {
                throw new FileUploadStorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new FileUploadStorageException(
                    "Cannot store file with relative path outside current directory " + filename);
            }
            Path filePath = this.rootLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return  filePath.toAbsolutePath();
        } catch (IOException e) {
            throw new FileUploadStorageException("Failed to store file " + filename, e);
        }
    }
}
