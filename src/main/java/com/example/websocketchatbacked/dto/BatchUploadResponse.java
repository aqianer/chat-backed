package com.example.websocketchatbacked.dto;

import java.util.List;

public class BatchUploadResponse {
    private List<UploadResultItem> results;

    public BatchUploadResponse() {
    }

    public BatchUploadResponse(List<UploadResultItem> results) {
        this.results = results;
    }

    public List<UploadResultItem> getResults() {
        return results;
    }

    public void setResults(List<UploadResultItem> results) {
        this.results = results;
    }
}
