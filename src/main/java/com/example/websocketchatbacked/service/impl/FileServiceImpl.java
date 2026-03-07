package com.example.websocketchatbacked.service.impl;

import com.example.websocketchatbacked.controller.FileController;
import com.example.websocketchatbacked.dto.*;
import com.example.websocketchatbacked.entity.KbDocument;
import com.example.websocketchatbacked.exception.BusinessException;
import com.example.websocketchatbacked.repository.FileOperationLogRepository;
import com.example.websocketchatbacked.repository.KbDocumentRelationRepository;
import com.example.websocketchatbacked.repository.KbDocumentRepository;
import com.example.websocketchatbacked.service.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class FileServiceImpl implements FileService {

    private static final Set<String> ALLOWED_FILE_TYPES = new HashSet<>(Arrays.asList("doc", "docx", "pdf", "txt", "md"));
    private static final long MAX_SINGLE_FILE_SIZE = 100 * 1024 * 1024;
    private static final int MAX_FILE_COUNT = 300;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload.path}")
    private String uploadPath;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String FILE_UPLOAD_TOKEN_PREFIX = "file:upload:token:";

    @Autowired
    private KbDocumentRepository kbDocumentRepository;

    @Override
    public List<FileCheckResultItem> checkFilesService(BatchFileCheckRequest request, Long userId) {
        if (request.getFiles() == null || request.getFiles().isEmpty()) {
            throw new BusinessException(400, "文件列表不能为空");
        }

        List<FileCheckResultItem> results = new ArrayList<>();

        for (FileCheckItem fileItem : request.getFiles()) {
            if (fileItem.getHash() == null || fileItem.getHash().isEmpty()) {
                results.add(new FileCheckResultItem(fileItem.getHash(), null, null));
                continue;
            }

            Optional<KbDocument> existingDocument = kbDocumentRepository.findByFileHash(fileItem.getHash());
            if (existingDocument.isPresent()) {
                results.add(FileCheckResultItem.exists(fileItem.getHash()));
            } else {
                String token = UUID.randomUUID().toString();
                String tokenKey = FILE_UPLOAD_TOKEN_PREFIX + token;
                String tokenValue = userId + ":" + fileItem.getHash();
                stringRedisTemplate.opsForValue().set(tokenKey, tokenValue, 5, TimeUnit.MINUTES);
                results.add(FileCheckResultItem.notExists(fileItem.getHash(), token));
            }
        }
        return results;
    }

    @Override
    public List<UploadResultItem> filesUploadService(BatchUploadRequest request, Long userId) {
        List<MultipartFile> files = request.getFiles();
        if (files == null || files.isEmpty()) {
            log.warn("文件列表为空");
            throw new BusinessException(400, "请选择要上传的文件");
        }

        log.info("上传文件数量：{}", files.size());

        if (files.size() > MAX_FILE_COUNT) {
            throw new BusinessException(400, "文件数量超出限制，最多支持 " + MAX_FILE_COUNT + " 个文件");
        }

        List<String> hashes = request.getHashes();
        if (hashes == null || hashes.size() != files.size()) {
            throw new BusinessException(400, "哈希值数量与文件数量不匹配");
        }

        List<String> tokens = request.getTokens();
        if (tokens == null || tokens.size() != files.size()) {
            throw new BusinessException(400, "Token数量与文件数量不匹配");
        }

        BatchConfigDTO batchConfig = null;
        String batchConfigJson = request.getBatchConfig();
        if (batchConfigJson != null && !batchConfigJson.isEmpty()) {
            try {
                batchConfig = objectMapper.readValue(batchConfigJson, BatchConfigDTO.class);
            } catch (Exception e) {
                throw new BusinessException(400, "batchConfig 格式错误");
            }
        }
        List<CompletableFuture<UploadResultItem>> futures = new ArrayList<>();

        List<UploadResultItem> results = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String hash = hashes.get(i);
            String token = tokens.get(i);

            if (file.isEmpty()) {
                results.add(UploadResultItem.failed("文件为空"));
                continue;
            }

            try {
                String tokenKey = FILE_UPLOAD_TOKEN_PREFIX + token;
                String tokenValue = stringRedisTemplate.opsForValue().get(tokenKey);
                if (tokenValue == null) {
                    results.add(UploadResultItem.failed("Token无效或已过期"));
                    continue;
                }

                String[] parts = tokenValue.split(":");
                if (parts.length != 2 || !parts[0].equals(String.valueOf(userId)) || !parts[1].equals(hash)) {
                    results.add(UploadResultItem.failed("Token验证失败"));
                    continue;
                }

                validateFile(file);

                // 同步校验通过，调用@Async异步方法处理文件存储（耗时操作）
                assert batchConfig != null;
                CompletableFuture<UploadResultItem> future = asyncTaskService.uploadFile(file, userId, Long.valueOf(batchConfig.getKnowledgeBaseId()), hash, getFileExtension(file.getOriginalFilename()));
                // 把异步任务的Future存到列表里
                futures.add(future);

                stringRedisTemplate.delete(tokenKey);

            } catch (Exception e) {
                // 校验失败，添加到结果列表
                results.add(UploadResultItem.failed(e.getMessage()));
            }
        }

        // 等待所有异步任务完成（阻塞当前线程，直到所有Future都有结果）
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 遍历异步任务的Future列表，获取结果并添加到最终结果列表
        for (CompletableFuture<UploadResultItem> future : futures) {
            // join()会获取异步任务的结果（此时已完成，不会阻塞）
            results.add(future.join());
        }

        return results;

    }

    private void validateFile(MultipartFile file) {
        // 1. 空文件校验
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件为空：" + file.getOriginalFilename());
        }
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_FILE_TYPES.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("不支持的文件类型：" + extension);
        }
        if (file.getSize() > MAX_SINGLE_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小超出限制，最大允许：" + (MAX_SINGLE_FILE_SIZE / 1024 / 1024) + "MB");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}
