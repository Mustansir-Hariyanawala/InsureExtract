package com.InsureExtract.version1.repository;

import com.InsureExtract.version1.model.DocumentChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunkEntity, String> {

    /**
     * Retrieves all text chunks extracted from a specific document file.
     * Useful when re-assembling or inspecting the broken-down parts of an uploaded PDF.
     */
    List<DocumentChunkEntity> findByDocument_DocumentId(String documentId);

    /**
     * Finds a local relational database text chunk by its matching external Vector DB reference ID.
     * Critical for when your Vector DB performs a similarity search and returns a Vector ID,
     * allowing you to instantly fetch the corresponding raw text block back into Spring Boot.
     */
    Optional<DocumentChunkEntity> findByVectorDbId(String vectorDbId);
}