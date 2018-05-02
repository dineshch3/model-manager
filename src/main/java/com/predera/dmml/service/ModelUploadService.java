package com.predera.dmml.service;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface ModelUploadService {

    Path store(MultipartFile file, String name, String version);

}
