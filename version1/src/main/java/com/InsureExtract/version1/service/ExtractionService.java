package com.InsureExtract.version1.service;

import com.InsureExtract.version1.model.SessionEntity;
import com.InsureExtract.version1.repository.SessionRepository;
import com.InsureExtract.version1.dto.extraction.ExtractionResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExtractionService {

    private final SessionRepository sessionRepository;
    private final ObjectMapper objectMapper; // Spring Boot automatically provides this

    /**
     * Saves any specific extraction type (Finance, Policy, etc.) into the common session
     */
    @Transactional
    public void saveStructuredOutput(String sessionId, ExtractionResult result) {
        SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        try {
            // Converts the specific Java Record into a raw JSON String
            String jsonOutput = objectMapper.writeValueAsString(result);
            session.setStructuredOutput(jsonOutput);

            sessionRepository.save(session);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize extraction results", e);
        }
    }
}