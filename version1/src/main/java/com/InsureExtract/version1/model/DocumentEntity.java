package com.InsureExtract.version1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String documentId;

    private String fileName;
    private String fileType; // e.g., application/pdf
    private long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private SessionEntity session;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentChunkEntity> chunks = new ArrayList<>();

    @CreatedDate
    private Date uploadedAt;
}