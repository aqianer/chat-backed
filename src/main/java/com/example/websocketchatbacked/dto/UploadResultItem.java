package com.example.websocketchatbacked.dto;

public class UploadResultItem {
    private Boolean success;
    private Long documentId;
    private String message;

    public UploadResultItem() {
    }

    public UploadResultItem(Boolean success, Long documentId, String message) {
        this.success = success;
        this.documentId = documentId;
        this.message = message;
    }

    public static UploadResultItem success(Long documentId) {
        return new UploadResultItem(true, documentId, null);
    }

    public static UploadResultItem failed(String message) {
        return new UploadResultItem(false, null, message);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
