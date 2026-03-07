package com.example.websocketchatbacked.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FileCheckItem {
    @NotBlank(message = "文件哈希值不能为空")
    @Size(max = 32, message = "文件哈希值长度不能超过32")
    private String hash;

    @NotBlank(message = "文件名不能为空")
    @Size(max = 255, message = "文件名长度不能超过255")
    private String fileName;

    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
