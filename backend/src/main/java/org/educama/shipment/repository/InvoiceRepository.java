package org.educama.shipment.repository;

import org.educama.shipment.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for accessing invoice entities.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Invoice findOneByInvoiceNumber(String invoiceNumber);
}
