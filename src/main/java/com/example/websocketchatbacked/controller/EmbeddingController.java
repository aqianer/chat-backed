package com.example.websocketchatbacked.controller;

import com.example.websocketchatbacked.dto.ApiResponse;
import com.example.websocketchatbacked.dto.DocumentIdsRequestDTO;
import com.example.websocketchatbacked.service.impl.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmbeddingController {

    @Autowired
    private AsyncTaskService asyncTaskService;

    @PostMapping("/embedding")
    public ApiResponse<String> generateEmbedding(@RequestBody DocumentIdsRequestDTO request) {
        request.getDocumentIds().forEach(documentId -> {
            asyncTaskService.generateVectorsForDocuments(documentId);
        });
        return ApiResponse.success("向量化任务已提交，正在后台处理");
    }

}
