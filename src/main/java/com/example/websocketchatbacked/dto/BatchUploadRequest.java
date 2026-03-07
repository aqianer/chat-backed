package com.example.websocketchatbacked.dto;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class BatchUploadRequest {
    private List<MultipartFile> files;
    private List<String> hashes;
    private List<String> tokens;
    private String batchConfig;

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public List<String> getHashes() {
        return hashes;
    }

    public void setHashes(List<String> hashes) {
        this.hashes = hashes;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public String getBatchConfig() {
        return batchConfig;
    }

    public void setBatchConfig(String batchConfig) {
        this.batchConfig = batchConfig;
    }
}
