package com.example.websocketchatbacked.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

// 复合主键必须实现Serializable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KbDocumentRelationId implements Serializable {
    // 字段名要和实体类的主键字段名一致（kbId），列名对应kb_id
    private Long kbId;

    // 字段名要和实体类的主键字段名一致（documentId），列名对应document_id
    private Long documentId;

    // 手动重写equals和hashCode（避免lombok自动生成的可能不兼容）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KbDocumentRelationId that = (KbDocumentRelationId) o;
        return Objects.equals(kbId, that.kbId) && Objects.equals(documentId, that.documentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kbId, documentId);
    }
}