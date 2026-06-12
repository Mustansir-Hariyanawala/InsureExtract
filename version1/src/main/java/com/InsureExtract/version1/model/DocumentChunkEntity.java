package com.InsureExtract.version1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DocumentChunkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String chunkId;

    @Column(columnDefinition = "TEXT")
    private String textContent;

    // The ID returned by your Vector Database (Pinecone, pgvector, etc.)
    // to map relational text with its vector embedding
    private String vectorDbId;

    // Store metadata context cleanly as a JSON or structured string
    @Column(columnDefinition = "TEXT")
    private String metadataJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private DocumentEntity document;
}