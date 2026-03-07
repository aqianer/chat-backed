package com.example.websocketchatbacked.service;

import com.example.websocketchatbacked.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<FileCheckResultItem> checkFilesService(BatchFileCheckRequest request, Long userId);

    List<UploadResultItem> filesUploadService(BatchUploadRequest request, Long userId);
}
