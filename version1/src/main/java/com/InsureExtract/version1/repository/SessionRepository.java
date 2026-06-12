package com.InsureExtract.version1.repository;

import com.InsureExtract.version1.model.SessionEntity;
import com.InsureExtract.version1.model.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, String> {

    /**
     * Finds all processing sessions belonging to a specific industry category
     * (e.g., FINANCE_ACCOUNTING_PROCUREMENT).
     */
    List<SessionEntity> findByCategory(DocumentCategory category);
}