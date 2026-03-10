package com.example.websocketchatbacked.dto;

import java.util.List;

public class DocumentIdsRequestDTO {
    private List<Long> documentIds;

    public List<Long> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<Long> documentIds) {
        this.documentIds = documentIds;
    }
}
