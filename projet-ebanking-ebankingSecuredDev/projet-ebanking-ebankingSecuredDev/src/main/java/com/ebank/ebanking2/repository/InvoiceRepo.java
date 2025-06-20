package com.ebank.ebanking2.repository;

import com.ebank.ebanking2.model.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByClientIdAndProviderAndReferenceNumberAndDueDateAfter(Long clientId, String provider, String referenceNumber, LocalDateTime dueDateAfter);
    Invoice findByClientIdAndProviderAndReferenceNumber(Long clientId, String provider, String referenceNumber);
    Page<Invoice> findByCompteId(Long id, Pageable pageable);
}