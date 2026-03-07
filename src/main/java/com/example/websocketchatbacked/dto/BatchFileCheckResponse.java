package com.example.websocketchatbacked.dto;

import java.util.List;

public class BatchFileCheckResponse {
    private List<FileCheckResultItem> results;

    public BatchFileCheckResponse() {
    }

    public BatchFileCheckResponse(List<FileCheckResultItem> results) {
        this.results = results;
    }

    public List<FileCheckResultItem> getResults() {
        return results;
    }

    public void setResults(List<FileCheckResultItem> results) {
        this.results = results;
    }
}
