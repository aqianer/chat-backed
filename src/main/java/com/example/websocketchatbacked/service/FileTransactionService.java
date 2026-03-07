package com.example.websocketchatbacked.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileTransactionService {
    Long saveFileRecord(MultipartFile file, Long userId, String storagePath, Long kbId, String hash,String fileType);

    void logOperation(Long userId, Long fileId, String operationType, String ipAddress, String status, String errorMessage);
}
