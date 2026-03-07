package com.example.websocketchatbacked.service.impl;

import com.example.websocketchatbacked.entity.FileOperationLog;
import com.example.websocketchatbacked.entity.KbDocument;
import com.example.websocketchatbacked.entity.KbDocumentRelation;
import com.example.websocketchatbacked.repository.FileOperationLogRepository;
import com.example.websocketchatbacked.repository.KbDocumentRelationRepository;
import com.example.websocketchatbacked.repository.KbDocumentRepository;
import com.example.websocketchatbacked.service.FileTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class FileTransactionServiceImpl implements FileTransactionService {

    @Autowired
    private KbDocumentRepository kbDocumentRepository;

    @Autowired
    private KbDocumentRelationRepository kbDocumentRelationRepository;

    @Autowired
    private FileOperationLogRepository fileOperationLogRepository;
    @Override
    @Transactional
    public Long saveFileRecord(MultipartFile file, Long userId, String storagePath, Long kbId, String hash,String fileType) {
        KbDocument kbDocument = new KbDocument();
        kbDocument.setUserId(userId);
        kbDocument.setFileName(file.getOriginalFilename());
        kbDocument.setStoragePath(storagePath);
        kbDocument.setFileSize(file.getSize());
        kbDocument.setFileType(fileType);
        kbDocument.setChunkCount(0);
        kbDocument.setStatus((byte) 1);
        kbDocument.setCurrentStep((byte) 1);
        kbDocument.setCreateTime(LocalDateTime.now());
        kbDocument.setUpdateTime(LocalDateTime.now());
        kbDocument.setFileHash(hash);
        KbDocument savedDocument = kbDocumentRepository.save(kbDocument);
        // 2. 修复 KbDocumentRelation 创建逻辑（关键！）
        KbDocumentRelation relation = new KbDocumentRelation(kbId, savedDocument.getId());
        // 补充创建时间（字段非空，必须赋值）
        relation.setCreateTime(LocalDateTime.now());

        kbDocumentRelationRepository.save(relation);
        logOperation(userId, savedDocument.getId(), "upload", null, "success", null);
        return savedDocument.getId();
    }

    @Override
    public void logOperation(Long userId, Long fileId, String operationType, String ipAddress, String status, String errorMessage) {
        FileOperationLog log = new FileOperationLog();
        log.setUserId(userId);
        log.setDocId(fileId);
        log.setOperationType(operationType);
        log.setOperationTime(LocalDateTime.now());
        log.setIpAddress(ipAddress);
        log.setStatus(status);
        log.setErrorMessage(errorMessage);
        fileOperationLogRepository.save(log);
    }
}
