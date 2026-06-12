package com.InsureExtract.version1.repository;

import com.InsureExtract.version1.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, String> {

    /**
     * Retrieves all individual files associated with a specific session ID.
     * Useful for showing a user all files processed in their current session.
     */
    List<DocumentEntity> findBySession_SessionId(String sessionId);
}