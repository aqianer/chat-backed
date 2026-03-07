package com.example.websocketchatbacked.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class BatchFileCheckRequest {
    @NotEmpty(message = "文件列表不能为空")
    @Valid
    private List<FileCheckItem> files;

    public List<FileCheckItem> getFiles() {
        return files;
    }

    public void setFiles(List<FileCheckItem> files) {
        this.files = files;
    }
}
