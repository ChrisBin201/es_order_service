package com.chris.orderservice.repo;

import com.chris.data.entity.order.Invoice;
import com.chris.data.entity.order.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceLineRepo extends JpaRepository<InvoiceLine, Long> {
}
