package com.example.websocketchatbacked.repository;

import com.example.websocketchatbacked.dto.DocumentDTO;
import com.example.websocketchatbacked.entity.KbDocumentRelation;
import com.example.websocketchatbacked.entity.KbDocumentRelationId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface KbDocumentRelationRepository extends CrudRepository<KbDocumentRelation, KbDocumentRelationId>,
        PagingAndSortingRepository<KbDocumentRelation, KbDocumentRelationId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM KbDocumentRelation r WHERE r.id.kbId = :kbId AND r.id.documentId = :documentId")
    void deleteByKbIdAndDocumentId(@Param("kbId") Long kbId, @Param("documentId") Long documentId);


    // 1. 无关键词：根据kbId查关联文档（直接返回DocumentDTO）
    @Query(value = "SELECT " +
            "d.id as id, " +
            "d.file_name as fileName, " +
            "'系统' as uploader, " +  // uploader固定值，直接在SQL里写死
            "DATE_FORMAT(d.create_time, '%Y-%m-%d %H:%i:%s') as uploadTime, " +  // 数据库直接格式化时间
            "IFNULL(d.chunk_count, 0) as chunkCount, " +  // 空值默认0
            "CASE WHEN IFNULL(d.chunk_count, 0) > 0 THEN '已向量化' ELSE '未向量化' END as vectorStatus, " +  // 直接计算状态
            "CONCAT(ROUND(d.file_size/1024/1024, 2), ' MB') as fileSize, " +  // 数据库计算文件大小（MB）
            "d.file_type as fileType " +
            "FROM kb_document_relation r " +
            "JOIN kb_document d ON r.document_id = d.id " +
            "WHERE r.kb_id = :kbId",
            countQuery = "SELECT COUNT(*) FROM kb_document_relation r WHERE r.kb_id = :kbId",
            nativeQuery = true)
    Page<DocumentDTO> findDocByKbId(@Param("kbId") Long kbId, Pageable pageable);
}
