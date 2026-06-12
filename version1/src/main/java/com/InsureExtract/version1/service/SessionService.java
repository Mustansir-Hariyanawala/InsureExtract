package com.InsureExtract.version1.service;

import com.InsureExtract.version1.model.DocumentCategory;
import com.InsureExtract.version1.model.DocumentChunkEntity;
import com.InsureExtract.version1.model.DocumentEntity;
import com.InsureExtract.version1.model.SessionEntity;
import com.InsureExtract.version1.repository.DocumentChunkRepository;
import com.InsureExtract.version1.repository.DocumentRepository;
import com.InsureExtract.version1.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;

    /**
     * Phase 1: Initialize a new extraction session.
     */
    @Transactional
    public SessionEntity createSession(DocumentCategory category) {
        SessionEntity session = new SessionEntity();
        session.setCategory(category);
        return sessionRepository.save(session);
    }

    /**
     * Phase 2 & 3: Upload files into a session and break them down into chunks.
     * This method shows exactly how Session, Document, and Chunk repositories interact.
     */
    @Transactional
    public SessionEntity uploadAndProcessDocuments(String sessionId, List<MultipartFile> files) {
        // 1. Fetch the target parent session
        SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + sessionId));

        for (MultipartFile file : files) {
            // 2. Create and configure the Document metadata
            DocumentEntity document = new DocumentEntity();
            document.setFileName(file.getOriginalFilename());
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setSession(session); // Establish relational link to Session

            // 3. Extract text content from the file
            String rawText = extractTextFromMultipartFile(file);

            // 4. Split the text into smaller chunks for the Vector DB
            List<String> textParagraphs = dummyTextSplitter(rawText);

            // 5. Turn text paragraphs into DocumentChunkEntities
            for (int i = 0; i < textParagraphs.size(); i++) {
                DocumentChunkEntity chunk = new DocumentChunkEntity();
                chunk.setTextContent(textParagraphs.get(i));
                chunk.setDocument(document); // Establish relational link to Document

                // Construct basic JSON metadata tracking document source and chunk order
                String metadata = String.format("{\"source\":\"%s\", \"chunk_index\":%d}",
                        file.getOriginalFilename(), i);
                chunk.setMetadataJson(metadata);

                // Add the chunk to the document's tracking list
                document.getChunks().add(chunk);
            }

            // Add the fully populated document into the session
            session.getDocuments().add(document);
        }

        // 6. Save the entire tree structure!
        // Thanks to CascadeType.ALL on our Entities, saving the session automatically
        // saves all associated Documents and Chunks in the correct order.
        return sessionRepository.save(session);
    }

    /**
     * Phase 4: Retrieve the processing details of a completed session.
     */
    @Transactional(readOnly = true)
    public SessionEntity getSessionDetails(String sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + sessionId));
    }

    // --- Helper Utilities for the Pipeline ---

    private String extractTextFromMultipartFile(MultipartFile file) {
        try {
            // TODO: In your actual pipeline, integrate Apache Tika or PDFBox here.
            // For now, we simulate reading raw text bytes from the file.
            return new String(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file content", e);
        }
    }

    private List<String> dummyTextSplitter(String text) {
        List<String> chunks = new ArrayList<>();
        // Simple mock splitter that breaks down files by double-newlines (paragraphs).
        // Later, you will swap this out for a token-based recursive character text splitter.
        if (text != null && !text.isBlank()) {
            String[] paragraphs = text.split("\\n\\n");
            for (String p : paragraphs) {
                if (!p.isBlank()) chunks.add(p.trim());
            }
        }
        return chunks;
    }
}