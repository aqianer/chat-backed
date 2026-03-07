package com.example.websocketchatbacked.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "kb_document_relation")
@IdClass(KbDocumentRelationId.class)
public class KbDocumentRelation {

    // 复合主键字段1：kb_id（只在主键里映射，实体类不单独定义）
    @Id
    @Column(name = "kb_id", insertable = true, updatable = false)
    private Long kbId;

    // 复合主键字段2：document_id（只在主键里映射，实体类不单独定义）
    @Id
    @Column(name = "document_id", insertable = true, updatable = false)
    private Long documentId;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    public KbDocumentRelation(Long kbId, Long id) {
        this.kbId = kbId;
        this.documentId = id;
    }
}
