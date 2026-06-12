package com.InsureExtract.version1.dto.extraction;

import java.time.LocalDate;
import java.util.List;

// Marker interface to group your extractions together
public interface ExtractionResult {}

record MedicalExtraction(
        String hospitalName,
        LocalDate admissionDate,
        List<String> treatments,
        Double totalClaimAmount
) implements ExtractionResult {}

record PolicyExtraction(
        String policyNumber,
        String insuredName,
        Double premium,
        LocalDate expiryDate
) implements ExtractionResult {}

record FinanceExtraction(
        String invoiceNumber,
        String vendorName,
        Double totalAmount,
        LocalDate dueDate,
        List<LineItem> items
) implements ExtractionResult {}

record LineItem(String description, Integer quantity, Double unitPrice) {}